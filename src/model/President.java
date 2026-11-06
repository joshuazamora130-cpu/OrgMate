package OrgMate.src.model;

public class President extends Officer {

    // Constructor passing studentID as both studentID and officerID
    public President(String studentName, String studentID, String block, String program) {
        super(studentName, studentID, block, program, studentID, "President");
    }

    // President-specific methods
    public void approveReport() {
        System.out.println("[President]: Approving final organizational reports...");
    }

    public void viewOverallSummary() {
        System.out.println("[President]: Reviewing overall membership & fee collection summary...");
    }

    @Override
    public void performDuty() {
        System.out.println("\n--- Executing President Duties ---");
        viewOverallSummary();
        approveReport();
    }
}