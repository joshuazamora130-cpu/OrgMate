
package src.model;

public class FacultyAccount {

    private Adviser adviser;
    private String institutionalEmail;
    private String password;

    public FacultyAccount(
            Adviser adviser,
            String institutionalEmail,
            String password) {

        this.adviser = adviser;
        this.institutionalEmail = institutionalEmail;
        this.password = password;
    }

    public Adviser getAdviser() {
        return adviser;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
