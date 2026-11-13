
package src.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PaymentTicket {

    private String ticketID;
    private RegularStudent student;
    private Payment payment;
    private ArrayList<String> organizations;

    private String status;
    private String officerRemarks;
    private String submittedDate;

    private String receiptPath;
    private String cashCollectorName;

    // AUDIT TRAIL
    private ArrayList<PaymentAuditLog> auditLogs;

    // CONSTRUCTORS

    public PaymentTicket(
            String ticketID,
            RegularStudent student,
            Payment payment,
            ArrayList<String> organizations) {

        this(ticketID, student, payment, organizations, null, null);
    }

    public PaymentTicket(
            String ticketID,
            RegularStudent student,
            Payment payment,
            ArrayList<String> organizations,
            String receiptPath,
            String cashCollectorName) {

        this.ticketID = ticketID;
        this.student = student;
        this.payment = payment;
        this.organizations = new ArrayList<>(organizations);

        this.receiptPath = receiptPath;
        this.cashCollectorName = cashCollectorName;

        this.status = "Submitted";
        this.officerRemarks = "No remarks yet.";

        this.submittedDate = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
        );

        this.payment.setStatus(status);

        this.auditLogs = new ArrayList<>();

        addAuditLog(
            student.getStudentName(),
            "Payment Submitted",
            "Initial membership payment submitted."
        );
    }

    // GETTERS

    public String getTicketID() {
        return ticketID;
    }

    public RegularStudent getStudent() {
        return student;
    }

    public Payment getPayment() {
        return payment;
    }

    public ArrayList<String> getOrganizations() {
        return new ArrayList<>(organizations);
    }

    public String getStatus() {
        return status;
    }

    public String getOfficerRemarks() {
        return officerRemarks;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public String getReceiptPath() {
        return receiptPath;
    }

    public String getCashCollectorName() {
        return cashCollectorName;
    }

    public ArrayList<PaymentAuditLog> getAuditLogs() {
        return new ArrayList<>(auditLogs);
    }

    // REPLACE WITH DATABASE AUDIT HISTORY

    public void setAuditLogs(ArrayList<PaymentAuditLog> auditLogs) {
        this.auditLogs = auditLogs == null
                ? new ArrayList<>()
                : new ArrayList<>(auditLogs);
    }

    // AUDIT LOG MANAGEMENT

    public void addAuditLog(
            String actor,
            String action,
            String remarks) {

        PaymentAuditLog log = new PaymentAuditLog(
            actor,
            action,
            remarks
        );

        auditLogs.add(log);
    }

    public void displayAuditTrail() {

        System.out.println("\n==================================");
        System.out.println("       PAYMENT AUDIT TRAIL");
        System.out.println("==================================");

        System.out.println("Ticket ID: " + ticketID);
        System.out.println("Student: " + student.getStudentName());

        if (auditLogs.isEmpty()) {
            System.out.println("No audit records available.");
        } else {
            for (PaymentAuditLog log : auditLogs) {
                log.displayLog();
            }
        }

        System.out.println("==================================");
    }

    // STATUS MANAGEMENT

    public void setStatus(String status) {
        this.status = status;
        this.payment.setStatus(status);
    }

    public void setStatus(
            String status,
            String actor,
            String remarks) {

        this.status = status;
        this.payment.setStatus(status);
        this.officerRemarks = remarks;

        addAuditLog(
            actor,
            "Status Changed: " + status,
            remarks
        );
    }

    public void setOfficerRemarks(String officerRemarks) {
        this.officerRemarks = officerRemarks;
    }

    // STUDENT EDIT PERMISSION

    public boolean canStudentEdit() {
        return "Needs Update".equalsIgnoreCase(status);
    }

    // UPDATE PAYMENT DETAILS

    public void updatePaymentDetails(
            String paymentDate,
            String referenceNo) {

        if (!canStudentEdit()) {
            throw new IllegalStateException(
                "This ticket cannot be edited."
            );
        }

        payment.setPaymentDate(paymentDate);
        payment.setReferenceNo(referenceNo);
    }

    // UPDATE RECEIPT

    public void updateReceiptPath(String receiptPath) {

        if (!canStudentEdit()) {
            throw new IllegalStateException(
                "This ticket cannot be edited."
            );
        }

        this.receiptPath = receiptPath;
    }

    // UPDATE CASH COLLECTOR

    public void updateCashCollectorName(String name) {

        if (!canStudentEdit()) {
            throw new IllegalStateException(
                "This ticket cannot be edited."
            );
        }

        this.cashCollectorName = name;
    }

    // RESUBMIT CORRECTED TICKET

    public void resubmit() {

        if (!canStudentEdit()) {
            throw new IllegalStateException(
                "Only tickets marked Needs Update can be resubmitted."
            );
        }

        setStatus(
            "Submitted",
            student.getStudentName(),
            "Corrections submitted. Awaiting officer review."
        );
    }

    // DISPLAY PAYMENT TICKET

    public void displayTicket() {

        System.out.println("\n==================================");
        System.out.println("       MEMBERSHIP PAYMENT TICKET");
        System.out.println("==================================");

        System.out.println("Ticket ID: " + ticketID);
        System.out.println("Student: " + student.getStudentName());
        System.out.println("SR Code: " + student.getStudentID());

        System.out.println("\nOrganizations:");

        for (String organization : organizations) {
            System.out.println("- " + organization);
        }

        System.out.printf(
            "\nPayment Amount: PHP %.2f%n",
            payment.getAmount()
        );

        System.out.println("Payment Method: "
                + payment.getClass().getSimpleName());

        System.out.println("Payment Date: "
                + payment.getPaymentDate());

        System.out.println("Reference Number: "
                + payment.getReferenceNo());

        if (cashCollectorName != null
                && !cashCollectorName.isEmpty()) {

            System.out.println(
                "Cash Collected By: " + cashCollectorName
            );
        }

        System.out.println("Receipt Image: "
                + (receiptPath == null ? "Not provided" : receiptPath));

        System.out.println("Submission Date: " + submittedDate);
        System.out.println("Status: " + status);
        System.out.println("Officer Remarks: " + officerRemarks);

        System.out.println("==================================");
    }
}
