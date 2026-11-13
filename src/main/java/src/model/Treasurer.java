
package src.model;

import java.util.ArrayList;
import src.services.PaymentStorage;

public class Treasurer extends Officer {

    public Treasurer(
            String studentName,
            String studentID,
            String block,
            String program,
            String officerID) {

        super(
            studentName,
            studentID,
            block,
            program,
            officerID,
            "Treasurer"
        );
    }

    // ==========================================
    // REVIEW PAYMENT RECEIPT
    // ==========================================

    public void reviewReceipt(Payment payment) {

        System.out.println("\n===== REVIEW PAYMENT =====");
        System.out.println("Ref No: " + payment.getReferenceNo());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Date: " + payment.getPaymentDate());
        System.out.println("Status: " + payment.getStatus());
    }

    // ==========================================
    // UPDATE STATUS AND SAVE TO SUPABASE
    // ==========================================

    private boolean saveTicketStatus(
            PaymentTicket ticket,
            String newStatus,
            String action,
            String remarks) {

        if (ticket == null) {
            System.out.println("Payment ticket not found.");
            return false;
        }

        String oldStatus = ticket.getStatus();
        String oldRemarks = ticket.getOfficerRemarks();

        // Temporarily update the Java object.
        ticket.setStatus(
            newStatus,
            getStudentName(),
            remarks
        );

        // Save status and audit log to Supabase.
        boolean saved = PaymentStorage.updateTicketStatus(
            ticket,
            getStudentID(),
            getStudentName(),
            action,
            remarks
        );

        if (!saved) {

            // Restore previous status if database update fails.
            ticket.setStatus(oldStatus);
            ticket.setOfficerRemarks(oldRemarks);

            System.out.println(
                "Failed to save status change. Please try again."
            );

            return false;
        }

        System.out.println(
            "Payment status successfully saved to Supabase."
        );

        return true;
    }

    // ==========================================
    // VERIFY PAYMENT TICKET
    // ==========================================

    public void verifyPayment(PaymentTicket ticket) {

        String remarks =
            "Payment verified and successfully reflected.";

        if (saveTicketStatus(
                ticket,
                "Reflected",
                "Payment Verified",
                remarks)) {

            System.out.println(
                "Payment ticket " + ticket.getTicketID()
                + " reflected."
            );
        }
    }

    // ==========================================
    // REJECT PAYMENT TICKET
    // ==========================================

    public void rejectPayment(
            PaymentTicket ticket,
            String reason) {

        if (reason == null || reason.trim().isEmpty()) {
            System.out.println("Rejection reason is required.");
            return;
        }

        if (saveTicketStatus(
                ticket,
                "Rejected",
                "Payment Rejected",
                reason)) {

            System.out.println(
                "Payment ticket " + ticket.getTicketID()
                + " rejected."
            );
        }
    }

    // ==========================================
    // REQUEST STUDENT UPDATE
    // ==========================================

    public void requestStudentUpdate(
            PaymentTicket ticket,
            String instructions) {

        if (instructions == null || instructions.trim().isEmpty()) {
            System.out.println("Update instructions are required.");
            return;
        }

        if (saveTicketStatus(
                ticket,
                "Needs Update",
                "Student Update Requested",
                instructions)) {

            System.out.println(
                "Update request sent for ticket "
                + ticket.getTicketID()
            );
        }
    }

    // ==========================================
    // MARK PAYMENT UNDER REVIEW
    // ==========================================

    public void markUnderReview(PaymentTicket ticket) {

        String remarks =
            "Your payment ticket is currently being reviewed.";

        if (saveTicketStatus(
                ticket,
                "Under Review",
                "Payment Under Review",
                remarks)) {

            System.out.println(
                "Ticket " + ticket.getTicketID()
                + " is now under review."
            );
        }
    }

    // ==========================================
    // GET TOTAL COLLECTED
    // ==========================================

    public double getTotalCollected(ArrayList<Payment> payments) {

        double total = 0;

        for (Payment payment : payments) {

            if (payment.getStatus().equalsIgnoreCase("Reflected")) {
                total += payment.getAmount();
            }
        }

        return total;
    }

    // ==========================================
    // GENERATE PAYMENT REPORT
    // ==========================================

    public void generatePaymentReport(ArrayList<Payment> payments) {

        System.out.println("\n===== PAYMENT REPORT =====");

        for (Payment payment : payments) {

            System.out.println(
                payment.getReferenceNo() + " | " +
                payment.getPaymentDate() + " | " +
                payment.getAmount() + " | " +
                payment.getStatus()
            );
        }

        System.out.println("--------------------------");

        System.out.printf(
            "Total Collected: PHP %.2f%n",
            getTotalCollected(payments)
        );

        System.out.println("==========================");
    }

    // ==========================================
    // OFFICER DUTY
    // ==========================================

    @Override
    public void performDuty() {

        System.out.println(
            getStudentName() +
            " (Treasurer) is managing and verifying membership fee payments."
        );
    }
}
