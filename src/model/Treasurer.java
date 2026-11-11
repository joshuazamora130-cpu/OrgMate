
package src.model;

import java.util.ArrayList;

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

    // REVIEW PAYMENT RECEIPT

    public void reviewReceipt(Payment payment) {

        System.out.println("\n===== REVIEW PAYMENT =====");
        System.out.println("Ref No: " + payment.getReferenceNo());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Date: " + payment.getPaymentDate());
        System.out.println("Status: " + payment.getStatus());
    }

    // VERIFY PAYMENT TICKET

    public void verifyPayment(PaymentTicket ticket) {

        ticket.setStatus(
            "Reflected",
            getStudentName(),
            "Payment verified and successfully reflected."
        );

        System.out.println(
            "Payment ticket " + ticket.getTicketID() + " reflected."
        );
    }

    // REJECT PAYMENT TICKET

    public void rejectPayment(
            PaymentTicket ticket,
            String reason) {

        if (reason == null || reason.trim().isEmpty()) {
            System.out.println("Rejection reason is required.");
            return;
        }

        ticket.setStatus(
            "Rejected",
            getStudentName(),
            reason
        );

        System.out.println(
            "Payment ticket " + ticket.getTicketID() + " rejected."
        );
    }

    // REQUEST STUDENT UPDATE

    public void requestStudentUpdate(
            PaymentTicket ticket,
            String instructions) {

        if (instructions == null || instructions.trim().isEmpty()) {
            System.out.println("Update instructions are required.");
            return;
        }

        ticket.setStatus(
            "Needs Update",
            getStudentName(),
            instructions
        );

        System.out.println(
            "Update request sent for ticket " + ticket.getTicketID()
        );
    }

    // MARK PAYMENT UNDER REVIEW

    public void markUnderReview(PaymentTicket ticket) {

        ticket.setStatus(
            "Under Review",
            getStudentName(),
            "Your payment ticket is currently being reviewed."
        );

        System.out.println(
            "Ticket " + ticket.getTicketID() + " is now under review."
        );
    }

    // GET TOTAL COLLECTED

    public double getTotalCollected(ArrayList<Payment> payments) {

        double total = 0;

        for (Payment payment : payments) {

            if (payment.getStatus().equalsIgnoreCase("Reflected")) {
                total += payment.getAmount();
            }
        }

        return total;
    }

    // GENERATE PAYMENT REPORT

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

    // OFFICER DUTY

    @Override
    public void performDuty() {

        System.out.println(
            getStudentName() +
            " (Treasurer) is managing and verifying membership fee payments."
        );
    }
}
