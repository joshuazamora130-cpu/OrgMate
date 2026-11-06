package OrgMate.src.model;

public abstract class Officer extends Student {
    // Attributes
    private String officerID;
    private String position;

    // Constructor
    public Officer(String studentName, String studentID, String block, String program, String officerID, String position) {
        super(studentName, studentID, block, program);
        this.officerID = officerID;
        this.position = position;
    }

    // Getters - retrieves a value from a private attribute
    public String getOfficerID() {
        return officerID;
    }

    public String getPosition() {
        return position;
    }

    // Setters - sets or updates a value in a private attribute
    public void setOfficerID(String officerID) {
        this.officerID = officerID;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // Common Officer Methods
    public void searchRecords() {
        System.out.println("[" + position + "]: Searching member records...");
    }

    // Abstract method implemented by specific officer roles
    public abstract void performDuty();

    // Display officer information
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Officer ID: " + officerID);
        System.out.println("Position: " + position);
    }
}