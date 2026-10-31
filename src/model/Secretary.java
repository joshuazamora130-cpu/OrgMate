package src.model;

public class Secretary extends Officer {

    // Constructor passing studentID as both studentID and officerID
    public Secretary(String studentName, String studentID, String block, String program) {
        super(studentName, studentID, block, program, studentID, "Secretary");
    }

    // Secretary-specific methods
    public void addMember(String name) {
        System.out.println("[Secretary]: Registering new member: " + name);
    }

    public void updateMember(String name) {
        System.out.println("[Secretary]: Updating profile for member: " + name);
    }

    public void deleteRecord(String targetID) {
        System.out.println("[Secretary]: Removing record for Student ID: " + targetID);
    }

    public void generateMembershipReport() {
        System.out.println("[Secretary]: Generating detailed membership report...");
    }

    @Override
    public void performDuty() {
        System.out.println("\n--- Executing Secretary Duties ---");
        generateMembershipReport();
    }
}