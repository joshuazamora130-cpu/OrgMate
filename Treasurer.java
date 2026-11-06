package src.model;

import java.util.ArrayList;

public class Treasurer extends Officer {

    public Treasurer(String studentName, String studentID, String block,
                     String program, String officerID) {
        // Same order as Joshua's Adviser: name, ID, block, program, officerID, position
        super(studentName, studentID, block, program, officerID, "Treasurer");
    }

    public void reviewReceipt(Payment payment) {
        System.out.println("Reviewing receipt...");
        System.out.println("Ref No: " + payment.getReferenceNo()
                + " | Amount: " + payment.getAmount()
                + " | Date: " + payment.getPaymentDate()
                + " | Status: " + payment.getStatus());
    }

    public void verifyPayment(Payment payment) {
        payment.setStatus("Verified");
        System.out.println("Payment " + payment.getReferenceNo() + " verified.");
    }

    public void rejectPayment(Payment payment) {
        payment.setStatus("Rejected");
        System.out.println("Payment " + payment.getReferenceNo() + " rejected.");
    }

    // Only verified payments count toward the total.
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
        System.out.println("===== PAYMENT REPORT =====");
        for (Payment p : payments) {
            System.out.println(p.getReferenceNo() + " | " + p.getPaymentDate()
                    + " | " + p.getAmount() + " | " + p.getStatus());
        }
        System.out.println("Total Collected: " + getTotalCollected(payments));
        System.out.println("==========================");
    }

    @Override
    public void performDuty() {
        System.out.println(getStudentName() + " (Treasurer) is managing and verifying membership fee payments.");
    }
}
