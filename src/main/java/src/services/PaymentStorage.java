
package src.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import src.DatabaseConnection;
import src.model.*;

public class PaymentStorage {

    private static final DateTimeFormatter INPUT_DATE =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // GENERATE UUID TICKET ID

    public static String generateTicketID() {
        return UUID.randomUUID().toString();
    }


    // ADD PAYMENT TICKET TO SUPABASE

    public static boolean addTicket(PaymentTicket ticket) {

        if (ticket == null || ticket.getStudent() == null
                || ticket.getPayment() == null) {

            System.out.println("Invalid payment ticket.");
            return false;
        }

        String ticketSQL = """
                INSERT INTO payment_tickets
                (ticket_id, sr_code, organizations, status,
                 officer_remarks, receipt_path, cash_collector_name)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String paymentSQL = """
                INSERT INTO payments
                (ticket_id, amount, payment_method,
                 payment_date, reference_number, bank_name,
                 gcash_number, maya_number, maya_transaction_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String auditSQL = """
                INSERT INTO audit_logs
                (ticket_id, actor_sr_code, actor_name, action, remarks)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                UUID ticketID = UUID.fromString(ticket.getTicketID());

                Student student = ticket.getStudent();
                Payment payment = ticket.getPayment();

                // 1. Save payment ticket

                try (PreparedStatement statement =
                        connection.prepareStatement(ticketSQL)) {

                    statement.setObject(1, ticketID);
                    statement.setString(2, student.getStudentID());

                    Array organizations = connection.createArrayOf(
                            "text",
                            ticket.getOrganizations().toArray()
                    );

                    try {
                        statement.setArray(3, organizations);
                        statement.setString(4, "Pending");
                        statement.setString(5, ticket.getOfficerRemarks());
                        statement.setString(6, ticket.getReceiptPath());
                        statement.setString(7, ticket.getCashCollectorName());

                        statement.executeUpdate();

                    } finally {
                        organizations.free();
                    }
                }

                // 2. Save payment details

                String paymentMethod = getPaymentMethod(payment);

                LocalDate parsedDate = LocalDate.parse(
                        payment.getPaymentDate(),
                        INPUT_DATE
                );

                String bankName = null;
                String gcashNumber = null;
                String mayaNumber = null;
                String mayaTransactionID = null;

                if (payment instanceof BankPayment) {
                    bankName = ((BankPayment) payment).getBankName();
                }

                if (payment instanceof GCashPayment) {
                    gcashNumber = ((GCashPayment) payment).getGcashNo();
                }

                if (payment instanceof MayaPayment) {
                    MayaPayment maya = (MayaPayment) payment;
                    mayaNumber = maya.getMayaNumber();
                    mayaTransactionID = maya.getTransactionID();
                }

                try (PreparedStatement statement =
                        connection.prepareStatement(paymentSQL)) {

                    statement.setObject(1, ticketID);
                    statement.setDouble(2, payment.getAmount());
                    statement.setString(3, paymentMethod);
                    statement.setDate(4, Date.valueOf(parsedDate));
                    statement.setString(5, payment.getReferenceNo());

                    statement.setString(6, bankName);
                    statement.setString(7, gcashNumber);
                    statement.setString(8, mayaNumber);
                    statement.setString(9, mayaTransactionID);

                    statement.executeUpdate();
                }

                // 3. Save initial audit log

                try (PreparedStatement statement =
                        connection.prepareStatement(auditSQL)) {

                    statement.setObject(1, ticketID);
                    statement.setString(2, student.getStudentID());
                    statement.setString(3, student.getStudentName());
                    statement.setString(4, "Payment Submitted");
                    statement.setString(
                            5,
                            "Initial membership payment submitted."
                    );

                    statement.executeUpdate();
                }

                // 4. Commit transaction

                connection.commit();

                System.out.println(
                        "Payment ticket successfully saved to Supabase."
                );

                return true;

            } catch (Exception e) {

                connection.rollback();

                System.out.println(
                        "Failed to save payment ticket: " + e.getMessage()
                );

                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database connection error: " + e.getMessage()
            );

            return false;
        }
    }

    // DETERMINE PAYMENT METHOD

    private static String getPaymentMethod(Payment payment) {

        if (payment instanceof GCashPayment) {
            return "GCash";
        }

        if (payment instanceof MayaPayment) {
            return "Maya";
        }

        if (payment instanceof BankPayment) {
            return "Bank";
        }

        if (payment instanceof CashPayment) {
            return "Cash";
        }

        throw new IllegalArgumentException(
                "Unsupported payment method."
        );
    }

    // GET ALL PAYMENT TICKETS

    public static ArrayList<PaymentTicket> getAllTickets() {

        String sql = """
                SELECT
                    pt.ticket_id,
                    pt.sr_code,
                    pt.organizations,
                    pt.status,
                    pt.officer_remarks,
                    pt.submitted_date,
                    pt.receipt_path,
                    pt.cash_collector_name,
                    s.student_name,
                    s.program,
                    s.block,
                    p.amount,
                    p.payment_method,
                    p.payment_date,
                    p.reference_number,
                    p.bank_name,
                    p.gcash_number,
                    p.maya_number,
                    p.maya_transaction_id
                FROM payment_tickets pt
                INNER JOIN student_roster s
                    ON pt.sr_code = s.sr_code
                INNER JOIN payments p
                    ON pt.ticket_id = p.ticket_id
                ORDER BY pt.submitted_date DESC
                """;

        ArrayList<PaymentTicket> tickets = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                PaymentTicket ticket = createTicket(result);

                // Load actual audit history from Supabase.
                loadAuditLogs(ticket, connection);

                tickets.add(ticket);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to retrieve payment tickets: "
                    + e.getMessage()
            );
        }

        return tickets;
    }

    // RECONSTRUCT PAYMENT TICKET

    private static PaymentTicket createTicket(ResultSet result)
            throws SQLException {

        String ticketID = result.getObject(
                "ticket_id", UUID.class
        ).toString();

        String srCode = result.getString("sr_code");
        String studentName = result.getString("student_name");
        String program = result.getString("program");
        String block = result.getString("block");

        RegularStudent student = new RegularStudent(
                studentName,
                srCode,
                block,
                program
        );

        double amount = result.getDouble("amount");

        String paymentMethod = result.getString("payment_method");

        String paymentDate = result.getDate("payment_date")
                .toLocalDate()
                .format(INPUT_DATE);

        String referenceNo = result.getString("reference_number");

        // Retrieve payment method-specific details.

        String bankName = result.getString("bank_name");
        String gcashNumber = result.getString("gcash_number");
        String mayaNumber = result.getString("maya_number");
        String mayaTransactionID =
                result.getString("maya_transaction_id");

        Payment payment;

        switch (paymentMethod.toLowerCase()) {

            case "gcash":

                payment = new GCashPayment(
                        amount,
                        paymentDate,
                        "Submitted",
                        referenceNo,
                        gcashNumber
                );

                break;

            case "maya":

                payment = new MayaPayment(
                        amount,
                        paymentDate,
                        "Submitted",
                        referenceNo,
                        mayaNumber,
                        mayaTransactionID
                );

                break;

            case "bank":

                payment = new BankPayment(
                        amount,
                        paymentDate,
                        "Submitted",
                        referenceNo,
                        bankName
                );

                break;

            case "cash":

                payment = new CashPayment(
                        amount,
                        paymentDate,
                        "Submitted",
                        referenceNo
                );

                break;

            default:

                throw new SQLException(
                        "Unknown payment method: " + paymentMethod
                );
        }

        // Retrieve organizations.

        Array sqlOrganizations = result.getArray("organizations");

        ArrayList<String> organizations = new ArrayList<>();

        if (sqlOrganizations != null) {

            try {
                Object[] values = (Object[]) sqlOrganizations.getArray();

                for (Object value : values) {
                    organizations.add(value.toString());
                }

            } finally {
                sqlOrganizations.free();
            }
        }

        String receiptPath = result.getString("receipt_path");
        String cashCollector = result.getString("cash_collector_name");

        PaymentTicket ticket = new PaymentTicket(
                ticketID,
                student,
                payment,
                organizations,
                receiptPath,
                cashCollector
        );

        // Convert database Pending status to Java Submitted status.

        String databaseStatus = result.getString("status");

        String javaStatus = databaseStatus.equalsIgnoreCase("Pending")
                ? "Submitted"
                : databaseStatus;

        ticket.setStatus(javaStatus);
        payment.setStatus(javaStatus);

        ticket.setOfficerRemarks(
                result.getString("officer_remarks")
        );

        return ticket;
    }

    // LOAD AUDIT HISTORY FROM SUPABASE

    private static void loadAuditLogs(
            PaymentTicket ticket,
            Connection connection) throws SQLException {

        String sql = """
                SELECT actor_name, action, remarks, created_at
                FROM audit_logs
                WHERE ticket_id = ?
                ORDER BY created_at ASC
                """;

        ArrayList<PaymentAuditLog> auditLogs = new ArrayList<>();

        try (PreparedStatement statement =
                connection.prepareStatement(sql)) {

            statement.setObject(
                    1,
                    UUID.fromString(ticket.getTicketID())
            );

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    java.sql.Timestamp createdAt =
                            result.getTimestamp("created_at");

                    auditLogs.add(new PaymentAuditLog(
                            result.getString("actor_name"),
                            result.getString("action"),
                            result.getString("remarks"),
                            createdAt == null
                                    ? null
                                    : createdAt.toLocalDateTime()
                    ));
                }
            }
        }

        // Replace temporary constructor log with database records.

        ticket.setAuditLogs(auditLogs);
    }

    // FIND PAYMENT TICKET

    public static PaymentTicket findTicket(String ticketID) {

        if (ticketID == null || ticketID.isBlank()) {
            return null;
        }

        try {
            UUID.fromString(ticketID);

        } catch (IllegalArgumentException e) {
            return null;
        }

        for (PaymentTicket ticket : getAllTickets()) {

            if (ticket.getTicketID().equalsIgnoreCase(ticketID)) {
                return ticket;
            }
        }

        return null;
    }

    // GET TICKETS BY STUDENT

    public static ArrayList<PaymentTicket> getTicketsByStudent(
            String studentID) {

        ArrayList<PaymentTicket> result = new ArrayList<>();

        if (studentID == null || studentID.isBlank()) {
            return result;
        }

        for (PaymentTicket ticket : getAllTickets()) {

            if (ticket.getStudent().getStudentID()
                    .equalsIgnoreCase(studentID)) {

                result.add(ticket);
            }
        }

        return result;
    }

    // CHECK ACTIVE PAYMENT TICKET

    public static boolean hasActiveTicket(String studentID) {

        if (studentID == null || studentID.isBlank()) {
            return false;
        }

        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM payment_tickets
                    WHERE LOWER(sr_code) = LOWER(?)
                    AND status IN (
                        'Pending',
                        'Under Review',
                        'Needs Update'
                    )
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, studentID.trim());

            try (ResultSet result = statement.executeQuery()) {
                return result.next() && result.getBoolean(1);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to check active ticket: " + e.getMessage()
            );

            return false;
        }
    }


    // RESUBMIT CORRECTED PAYMENT TICKET

    public static boolean resubmitTicket(
            PaymentTicket ticket,
            String actorSRCode,
            String actorName,
            String remarks) {

        if (ticket == null || ticket.getPayment() == null
                || actorSRCode == null || actorName == null) {
            return false;
        }

        if (!ticket.canStudentEdit()) {
            System.out.println(
                    "Only tickets marked Needs Update can be resubmitted."
            );
            return false;
        }

        String updateTicketSQL = """
                UPDATE payment_tickets
                SET status = 'Pending',
                    officer_remarks = NULL,
                    receipt_path = ?,
                    cash_collector_name = ?,
                    updated_at = NOW()
                WHERE ticket_id = ?
                  AND sr_code = ?
                  AND status = 'Needs Update'
                """;

        String updatePaymentSQL = """
                UPDATE payments
                SET payment_date = ?,
                    reference_number = ?,
                    bank_name = ?,
                    gcash_number = ?,
                    maya_number = ?,
                    maya_transaction_id = ?
                WHERE ticket_id = ?
                """;

        String auditSQL = """
                INSERT INTO audit_logs
                (ticket_id, actor_sr_code, actor_name, action, remarks)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                UUID ticketID = UUID.fromString(ticket.getTicketID());

                Payment payment = ticket.getPayment();

                LocalDate parsedDate = LocalDate.parse(
                        payment.getPaymentDate(),
                        INPUT_DATE
                );

                String bankName = null;
                String gcashNumber = null;
                String mayaNumber = null;
                String mayaTransactionID = null;

                if (payment instanceof BankPayment) {
                    bankName = ((BankPayment) payment).getBankName();
                }

                if (payment instanceof GCashPayment) {
                    gcashNumber = ((GCashPayment) payment).getGcashNo();
                }

                if (payment instanceof MayaPayment) {
                    MayaPayment maya = (MayaPayment) payment;
                    mayaNumber = maya.getMayaNumber();
                    mayaTransactionID = maya.getTransactionID();
                }

                // 1. Update ticket information.
                try (PreparedStatement statement =
                        connection.prepareStatement(updateTicketSQL)) {

                    statement.setString(1, ticket.getReceiptPath());
                    statement.setString(2, ticket.getCashCollectorName());
                    statement.setObject(3, ticketID);
                    statement.setString(4, actorSRCode);

                    int rows = statement.executeUpdate();

                    if (rows != 1) {
                        connection.rollback();
                        System.out.println(
                                "Ticket could not be resubmitted. "
                                + "Check its current status and ownership."
                        );
                        return false;
                    }
                }

                // 2. Update payment information.
                try (PreparedStatement statement =
                        connection.prepareStatement(updatePaymentSQL)) {

                    statement.setDate(1, Date.valueOf(parsedDate));
                    statement.setString(2, payment.getReferenceNo());
                    statement.setString(3, bankName);
                    statement.setString(4, gcashNumber);
                    statement.setString(5, mayaNumber);
                    statement.setString(6, mayaTransactionID);
                    statement.setObject(7, ticketID);

                    int rows = statement.executeUpdate();

                    if (rows != 1) {
                        connection.rollback();
                        System.out.println(
                                "Payment details could not be updated."
                        );
                        return false;
                    }
                }

                // 3. Save resubmission audit record.
                try (PreparedStatement statement =
                        connection.prepareStatement(auditSQL)) {

                    statement.setObject(1, ticketID);
                    statement.setString(2, actorSRCode);
                    statement.setString(3, actorName);
                    statement.setString(4, "Payment Resubmitted");
                    statement.setString(5, remarks);

                    statement.executeUpdate();
                }

                // 4. Commit all database changes.
                connection.commit();

                // Update the Java object only after successful commit.
                ticket.setStatus("Submitted");
                ticket.setOfficerRemarks(null);
                ticket.addAuditLog(
                        actorName,
                        "Payment Resubmitted",
                        remarks
                );

                System.out.println(
                        "Payment ticket successfully resubmitted!"
                );

                return true;

            } catch (Exception e) {

                connection.rollback();

                System.out.println(
                        "Failed to resubmit payment: " + e.getMessage()
                );

                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database connection error: " + e.getMessage()
            );

            return false;
        }
    }


    // DISPLAY STUDENT PAYMENT HISTORY

    public static void displayStudentTickets(String studentID) {

        ArrayList<PaymentTicket> studentTickets =
                getTicketsByStudent(studentID);

        System.out.println("\n========== PAYMENT HISTORY ==========");

        if (studentTickets.isEmpty()) {
            System.out.println("No payment tickets found.");
            return;
        }

        for (PaymentTicket ticket : studentTickets) {
            ticket.displayTicket();
        }
    }

    // UPDATE PAYMENT TICKET STATUS

    public static boolean updateTicketStatus(
            PaymentTicket ticket,
            String actorSRCode,
            String actorName,
            String action,
            String remarks) {

        if (ticket == null || actorSRCode == null
                || actorName == null) {
            return false;
        }

        String databaseStatus = ticket.getStatus();

        if (databaseStatus.equalsIgnoreCase("Submitted")) {
            databaseStatus = "Pending";
        }

        String updateSQL = """
                UPDATE payment_tickets
                SET status = ?,
                    officer_remarks = ?,
                    updated_at = NOW()
                WHERE ticket_id = ?
                """;

        String auditSQL = """
                INSERT INTO audit_logs
                (ticket_id, actor_sr_code, actor_name, action, remarks)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                UUID ticketID = UUID.fromString(ticket.getTicketID());

                // 1. Update ticket status.

                try (PreparedStatement statement =
                        connection.prepareStatement(updateSQL)) {

                    statement.setString(1, databaseStatus);
                    statement.setString(2, remarks);
                    statement.setObject(3, ticketID);

                    int rows = statement.executeUpdate();

                    if (rows == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                // 2. Record action in audit logs.

                try (PreparedStatement statement =
                        connection.prepareStatement(auditSQL)) {

                    statement.setObject(1, ticketID);
                    statement.setString(2, actorSRCode);
                    statement.setString(3, actorName);
                    statement.setString(4, action);
                    statement.setString(5, remarks);

                    statement.executeUpdate();
                }

                connection.commit();
                return true;

            } catch (Exception e) {

                connection.rollback();

                System.out.println(
                        "Failed to update payment status: "
                        + e.getMessage()
                );

                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage()
            );

            return false;
        }
    }
}
