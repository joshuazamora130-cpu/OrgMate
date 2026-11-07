
package src.model;

public class Adviser extends Officer {

    // Constructor accepting studentID and manually entered officerID
    public Adviser(String studentName, String studentID,
                   String block, String program, String officerID) {
        super(studentName, studentID, block, program, officerID, "Adviser");
    }

    // Adviser-specific methods
    public void viewRecords() {
        System.out.println("[Adviser]: Viewing organizational records...");
    }

    public void viewPaymentLog() {
        System.out.println("[Adviser]: Reviewing financial payment logs...");
    }

    public void addRemarks(String remarks) {
        System.out.println("[Adviser]: Added remarks/guidance: \"" + remarks + "\"");
    }

    @Override
    public void performDuty() {
        System.out.println("\n--- Executing Adviser Duties ---");
        viewRecords();
        viewPaymentLog();
        addRemarks("Approved quarterly report with feedback.");
    }
}
