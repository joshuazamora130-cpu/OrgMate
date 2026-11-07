
package src.model;

import java.util.ArrayList;

public class Treasurer extends Officer {

    public Treasurer(String studentName, String studentID, String block,
                     String program, String officerID) {
        super(studentName, studentID, block, program, officerID, "Treasurer");
    }

    public void reviewReceipt(Payment payment) {
        System.out.println("\n===== REVIEW PAYMENT =====");
        System.out.println("Ref No: " + payment.getReferenceNo());
        System.out.println("Amount: " + payment.getAmount());
        System.out.println("Date: " + payment.getPaymentDate());
        System.out.println("Status: " + payment.getStatus());
    }

    public void verifyPayment(Payment payment) {
        payment.setStatus("Verified");
        System.out.println("Payment " + payment.getReferenceNo() + " verified.");
    }

    public void rejectPayment(Payment payment) {
        payment.setStatus("Rejected");
        System.out.println("Payment " + payment.getReferenceNo() + " rejected.");
    }

    public double getTotalCollected(ArrayList<Payment> payments) {
        double total = 0;

        for (Payment p : payments) {
            if (p.getStatus().equalsIgnoreCase("Verified")) {
                total += p.getAmount();
            }
        }

        return total;
    }

    public void generatePaymentReport(ArrayList<Payment> payments) {
        System.out.println("\n===== PAYMENT REPORT =====");

        for (Payment p : payments) {
            System.out.println(
                p.getReferenceNo() + " | " +
                p.getPaymentDate() + " | " +
                p.getAmount() + " | " +
                p.getStatus()
            );
        }

        System.out.println("--------------------------");
        System.out.println("Total Collected: P" + getTotalCollected(payments));
        System.out.println("==========================");
    }

    @Override
    public void performDuty() {
        System.out.println(
            getStudentName() +
            " (Treasurer) is managing and verifying membership fee payments."
        );
    }
}
