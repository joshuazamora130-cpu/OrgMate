package src.model;

public class Student {
    // Attributes
    private String studentName;
    private String studentID; // will need ng validation whether ang format is correct (2x-xxxxx)
    private String block;
    // I erased Department because we will limit it to CICS department only
    private String program; // whether IT or CS

    // Constructor
    public Student(String studentName, String studentID, String block, String program) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.block = block;
        this.program = program;
    }

    // Getters - gets or retrieves a value from a private attribute.
    public String getStudentName() {
        return studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getBlock() {
        return block;
    }

    public String getProgram() {
        return program;
    }

    // Setters - setter sets or updates a value in a private attribute.
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    // Display student information
    public void displayInfo() {
        System.out.println("\n===== OrgMate Student Information =====");
        System.out.println("Student Name: " + studentName);
        System.out.println("Student ID: " + studentID);
        System.out.println("Block: " + block);
        System.out.println("Program: " + program);
    }
}