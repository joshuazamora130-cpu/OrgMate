
package src.model;

public class Account {

    // Attributes
    private Student student;
    private String password;
    private String yearLevel;
    private String classification;

    // Constructor
    public Account(Student student, String password,
                   String yearLevel, String classification) {

        this.student = student;
        this.password = password;
        this.yearLevel = yearLevel;
        this.classification = classification;
    }

    // Getters
    public Student getStudent() {
        return student;
    }

    public String getPassword() {
        return password;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getClassification() {
        return classification;
    }

    // Setters
    public void setPassword(String password) {
        this.password = password;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
