package src.model;

import java.util.ArrayList;

public class Auditor extends Officer {

    public Auditor(String studentName, String studentID, String block,
                   String program, String officerID) {
        // Same order as Joshua's Adviser: name, ID, block, program, officerID, position
        super(studentName, studentID, block, program, officerID, "Auditor");
    }

    public void auditTransactions(ArrayList<Payment> payments) {
        System.out.println("===== AUDITING TRANSACTIONS =====");
        for (Payment p : payments) {
            System.out.println(p.getReferenceNo() + " | " + p.getAmount() + " | " + p.getStatus());
        }
        System.out.println("Total transactions reviewed: " + payments.size());
    }

    public void flagDiscrepancy(Payment payment, String reason) {
        payment.setStatus("Flagged");
        System.out.println("Payment " + payment.getReferenceNo() + " flagged. Reason: " + reason);
    }

    public void generateAuditReport(ArrayList<Payment> payments) {
        int flagged = 0;
        for (Payment p : payments) {
            if (p.getStatus().equalsIgnoreCase("Flagged")) {
                flagged++;
            }
        }
        System.out.println("===== AUDIT REPORT =====");
        System.out.println("Total transactions: " + payments.size());
        System.out.println("Flagged discrepancies: " + flagged);
        System.out.println("========================");
    }

    @Override
    public void performDuty() {
        System.out.println(getStudentName() + " (Auditor) is reviewing fee transactions.");
    }
}
