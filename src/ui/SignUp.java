
package src.ui;

import java.util.Scanner;

import src.authentication.Auth;
import src.model.Account;
import src.model.Student;
import src.model.RegularStudent;
import src.model.Officer;
import src.model.Secretary;
import src.model.Treasurer;
import src.model.President;
import src.model.Adviser;
import src.model.Auditor;
import src.services.AccountStorage;

public class SignUp {

    public static void show(Scanner input) {

        System.out.println("\n=================================");
        System.out.println("       ORGMATE SIGN UP");
        System.out.println("=================================");

        System.out.println("[1] Register as Regular Student");
        System.out.println("[2] Register as Officer");
        System.out.println("[3] Back");

        int choice = getChoice(input, "Select account type: ");

        switch (choice) {
            case 1:
                registerStudent(input);
                break;

            case 2:
                registerOfficer(input);
                break;

            case 3:
                System.out.println("Returning to main menu...");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void registerStudent(Scanner input) {

        System.out.println("\n========== STUDENT REGISTRATION ==========");

        String name = getInput(input, "Full Name: ");
        String srCode = getInput(input, "SR Code (00-00000): ");

        if (!Auth.isValidSRCode(srCode)) {
            System.out.println("Invalid SR Code format.");
            return;
        }

        if (AccountStorage.accountExists(srCode)) {
            System.out.println("An account with this SR Code already exists.");
            return;
        }

        String block = getInput(input, "Block/Section: ");
        String program = getInput(input, "Program: ");
        String yearLevel = getInput(input, "Year Level: ");

        String password = getPassword(input);

        if (password == null) {
            return;
        }

        Student student = new RegularStudent(
            name,
            srCode,
            block,
            program
        );

        Account account = new Account(
            student,
            password,
            yearLevel,
            "Regular Student"
        );

        AccountStorage.addAccount(account);

        System.out.println("\nRegistration successful!");
        System.out.println("You can now log in using your SR Code.");
    }

    private static void registerOfficer(Scanner input) {

        System.out.println("\n========== OFFICER REGISTRATION ==========");

        String name = getInput(input, "Full Name: ");
        String srCode = getInput(input, "SR Code (00-00000): ");

        if (!Auth.isValidSRCode(srCode)) {
            System.out.println("Invalid SR Code format.");
            return;
        }

        if (AccountStorage.accountExists(srCode)) {
            System.out.println("An account with this SR Code already exists.");
            return;
        }

        String block = getInput(input, "Block/Section: ");
        String program = getInput(input, "Program: ");
        String yearLevel = getInput(input, "Year Level: ");

        System.out.println("\nOfficer Position:");
        System.out.println("[1] Secretary");
        System.out.println("[2] Treasurer");
        System.out.println("[3] President");
        System.out.println("[4] Adviser");
        System.out.println("[5] Auditor");

        int position = getChoice(input, "Select position: ");

        if (position < 1 || position > 5) {
            System.out.println("Invalid officer position.");
            return;
        }

        String officerCode = getInput(input, "Officer Registration Code: ");

        if (!Auth.authorizeOfficer(officerCode)) {
            System.out.println("Invalid officer registration code.");
            return;
        }

        String officerID = getInput(input, "Officer ID: ");

        String password = getPassword(input);

        if (password == null) {
            return;
        }

        Officer officer;

        switch (position) {
            case 1:
                officer = new Secretary(
                    name, srCode, block, program, officerID
                );
                break;

            case 2:
                officer = new Treasurer(
                    name, srCode, block, program, officerID
                );
                break;

            case 3:
                officer = new President(
                    name, srCode, block, program, officerID
                );
                break;

            case 4:
                officer = new Adviser(
                    name, srCode, block, program, officerID
                );
                break;

            case 5:
                officer = new Auditor(
                    name, srCode, block, program, officerID
                );
                break;

            default:
                System.out.println("Invalid position.");
                return;
        }

        Account account = new Account(
            officer,
            password,
            yearLevel,
            officer.getPosition()
        );

        AccountStorage.addAccount(account);

        System.out.println("\nOfficer registration successful!");
        System.out.println("You can now log in using your SR Code.");
    }

    private static String getPassword(Scanner input) {

        System.out.println("\nPassword Requirements:");
        System.out.println("- At least 8 characters");
        System.out.println("- At least one uppercase letter");
        System.out.println("- At least one lowercase letter");
        System.out.println("- At least one number");

        String password = getInput(input, "Create Password: ");

        if (!Auth.isValidPassword(password)) {
            System.out.println("Password does not meet the requirements.");
            return null;
        }

        String confirmPassword = getInput(input, "Confirm Password: ");

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return null;
        }

        return password;
    }

    private static String getInput(Scanner input, String message) {

        String value;

        do {
            System.out.print(message);
            value = input.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("This field cannot be empty.");
            }

        } while (value.isEmpty());

        return value;
    }

    private static int getChoice(Scanner input, String message) {

        System.out.print(message);

        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
