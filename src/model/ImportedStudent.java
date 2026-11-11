
package src.model;

public class ImportedStudent {

    private String studentID;
    private String studentName;
    private String program;
    private String yearLevel;
    private String block;

    public ImportedStudent(String studentID, String studentName,
                           String program, String yearLevel, String block) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.program = program;
        this.yearLevel = yearLevel;
        this.block = block;
    }

    // Getters
    public String getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getProgram() {
        return program;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getBlock() {
        return block;
    }

    // Setters
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void displayInfo() {
        System.out.println("SR Code: " + studentID);
        System.out.println("Student Name: " + studentName);
        System.out.println("Program: " + program);
        System.out.println("Year Level: " + yearLevel);
        System.out.println("Block: " + block);
    }
}
