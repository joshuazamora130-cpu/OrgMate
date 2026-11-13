
package src.ui;

import java.util.Scanner;

import src.authentication.Auth;
import src.model.Account;
import src.model.FacultyAccount;
import src.model.Student;
import src.model.RegularStudent;
import src.model.Officer;
import src.model.President;
import src.model.Secretary;
import src.model.Treasurer;
import src.model.Auditor;
import src.model.Adviser;

public class Login {

    public static void show(Scanner input) {

        System.out.println("\n================================");
        System.out.println("             LOGIN");
        System.out.println("================================");

        System.out.print("Enter SR Code or Institutional Email: ");
        String identifier = input.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        if (identifier.isEmpty() || password.isEmpty()) {
            System.out.println("Please complete all fields.");
            return;
        }

        // FACULTY ADVISER LOGIN
        if (identifier.contains("@")) {

            FacultyAccount facultyAccount =
                    Auth.authenticateFaculty(identifier, password);

            if (facultyAccount == null) {
                System.out.println(
                    "Incorrect Institutional Email or Password."
                );
                return;
            }

            Adviser adviser = facultyAccount.getAdviser();

            System.out.println("\nFaculty login successful!");
            System.out.println(
                "Welcome, " + adviser.getFacultyName() + "!"
            );

            AdviserDashboard.show(input, adviser);
            return;
        }

        // STUDENT AND OFFICER LOGIN
        String srCode = identifier;

        if (!Auth.isValidSRCode(srCode)) {
            System.out.println(
                "Invalid SR Code format. Expected XX-XXXXX."
            );
            return;
        }

        Account account = Auth.authenticate(srCode, password);

        if (account == null) {
            System.out.println("Incorrect SR Code or Password.");
            return;
        }

        System.out.println("\nLogin successful!");
        System.out.println(
            "Welcome, " + account.getStudent().getStudentName() + "!"
        );

        redirectDashboard(input, account);
    }

    // ROLE-BASED DASHBOARD REDIRECTION

    private static void redirectDashboard(
            Scanner input,
            Account account) {

        Student student = account.getStudent();

        if (student instanceof President) {

            PresidentDashboard.show(input, (President) student);

        } else if (student instanceof Secretary) {

            SecretaryDashboard.show(input, (Secretary) student);

        } else if (student instanceof Treasurer) {

            TreasurerDashboard.show(input, (Treasurer) student);

        } else if (student instanceof Auditor) {

            AuditorDashboard.show(input, (Auditor) student);

        } else if (student instanceof RegularStudent) {

            StudentDashboard.show(input, account);

        } else if (student instanceof Officer) {

            officerDashboard(input, (Officer) student);

        } else {

            student.displayInfo();
        }
    }

    // GENERAL OFFICER DASHBOARD

    private static void officerDashboard(
            Scanner input,
            Officer officer) {

        int choice;

        do {

            System.out.println("\n================================");
            System.out.println(
                "       " + officer.getPosition().toUpperCase() +
                " DASHBOARD"
            );
            System.out.println("================================");

            System.out.println("[1] View Officer Information");
            System.out.println("[2] Perform Officer Duties");
            System.out.println("[3] Logout");

            choice = getChoice(input);

            switch (choice) {

                case 1:
                    officer.displayInfo();
                    break;

                case 2:
                    officer.performDuty();
                    break;

                case 3:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 3);
    }

    // INPUT VALIDATION

    private static int getChoice(Scanner input) {

        try {
            System.out.print("Enter choice: ");
            return Integer.parseInt(input.nextLine().trim());

        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
