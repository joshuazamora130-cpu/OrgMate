
package src.ui;

import java.util.Scanner;

import src.authentication.Auth;
import src.model.*;
import src.services.AccountStorage;

public class SignUp {

    // ==========================================
    // SIGN UP MENU
    // ==========================================

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

    // ==========================================
    // COLLECT STUDENT INFORMATION
    // ==========================================

    private static StudentInfo collectStudentInfo(Scanner input) {

        System.out.println("\n========== STUDENT INFORMATION ==========");

        String srCode = getInput(input, "SR Code (00-00000): ");

        if (!Auth.isValidSRCode(srCode)) {
            System.out.println("Invalid SR Code format.");
            return null;
        }

        if (AccountStorage.accountExists(srCode)) {
            System.out.println("An account with this SR Code already exists.");
            return null;
        }

        String name = getInput(input, "Full Name: ");

        System.out.println("\nSelect Program:");
        System.out.println("[1] BS Computer Science");
        System.out.println("[2] BS Information Technology");

        int programChoice = getChoice(input, "Enter choice: ");

        String program;

        switch (programChoice) {
            case 1:
                program = "BS Computer Science";
                break;

            case 2:
                program = "BS Information Technology";
                break;

            default:
                System.out.println("Invalid program selection.");
                return null;
        }

        System.out.println("\nSelect Year Level:");
        System.out.println("[1] 1st Year");
        System.out.println("[2] 2nd Year");
        System.out.println("[3] 3rd Year");
        System.out.println("[4] 4th Year");

        int yearChoice = getChoice(input, "Enter choice: ");

        if (yearChoice < 1 || yearChoice > 4) {
            System.out.println("Invalid year level.");
            return null;
        }

        String yearLevel = yearChoice + switch (yearChoice) {
            case 1 -> "st Year";
            case 2 -> "nd Year";
            case 3 -> "rd Year";
            default -> "th Year";
        };

        String block = getInput(input, "Block: ");

        return new StudentInfo(
                srCode, name, program, yearLevel, block
        );
    }

    // ==========================================
    // REGULAR STUDENT REGISTRATION
    // ==========================================

    private static void registerStudent(Scanner input) {

        System.out.println("\n========== STUDENT REGISTRATION ==========");

        StudentInfo info = collectStudentInfo(input);

        if (info == null) {
            return;
        }

        String password = getPassword(input);

        if (password == null) {
            return;
        }

        Student student = new RegularStudent(
                info.name,
                info.srCode,
                info.block,
                info.program
        );

        Account account = new Account(
                student,
                password,
                info.yearLevel,
                "Regular Student"
        );

        saveAccount(account);
    }

    // ==========================================
    // OFFICER REGISTRATION
    // ==========================================

    private static void registerOfficer(Scanner input) {

        System.out.println("\n========== OFFICER REGISTRATION ==========");

        StudentInfo info = collectStudentInfo(input);

        if (info == null) {
            return;
        }

        System.out.println("\nSelect Officer Position:");
        System.out.println("[1] Secretary");
        System.out.println("[2] Treasurer");
        System.out.println("[3] President");
        System.out.println("[4] Auditor");

        int position = getChoice(input, "Enter choice: ");

        if (position < 1 || position > 4) {
            System.out.println("Invalid officer position.");
            return;
        }

        String officerCode =
                getInput(input, "Officer Registration Code: ");

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
                        info.name, info.srCode,
                        info.block, info.program, officerID
                );
                break;

            case 2:
                officer = new Treasurer(
                        info.name, info.srCode,
                        info.block, info.program, officerID
                );
                break;

            case 3:
                officer = new President(
                        info.name, info.srCode,
                        info.block, info.program, officerID
                );
                break;

            case 4:
                officer = new Auditor(
                        info.name, info.srCode,
                        info.block, info.program, officerID
                );
                break;

            default:
                return;
        }

        Account account = new Account(
                officer,
                password,
                info.yearLevel,
                officer.getPosition()
        );

        saveAccount(account);
    }

    // ==========================================
    // SAVE ACCOUNT TO SUPABASE
    // ==========================================

    private static void saveAccount(Account account) {

        boolean registered = AccountStorage.addAccount(account);

        if (registered) {

            System.out.println("\n=================================");
            System.out.println("REGISTRATION SUCCESSFUL!");
            System.out.println("Your account has been saved.");
            System.out.println("You can now log in using your SR Code.");
            System.out.println("=================================");

        } else {

            System.out.println("\nRegistration failed.");
            System.out.println("Please check your information and try again.");
        }
    }

    // ==========================================
    // PASSWORD VALIDATION
    // ==========================================

    private static String getPassword(Scanner input) {

        System.out.println("\n========== CREATE PASSWORD ==========");
        System.out.println("Requirements:");
        System.out.println("- At least 8 characters");
        System.out.println("- At least one uppercase letter");
        System.out.println("- At least one lowercase letter");
        System.out.println("- At least one number");

        String password = getInput(input, "Create Password: ");

        if (!Auth.isValidPassword(password)) {
            System.out.println("Password does not meet the requirements.");
            return null;
        }

        String confirmPassword =
                getInput(input, "Confirm Password: ");

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return null;
        }

        return password;
    }

    // ==========================================
    // INPUT HELPERS
    // ==========================================

    private static String getInput(
            Scanner input, String message) {

        String value;

        do {
            System.out.print(message);
            value = input.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("This field is required.");
            }

        } while (value.isEmpty());

        return value;
    }

    private static int getChoice(
            Scanner input, String message) {

        System.out.print(message);

        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ==========================================
    // STUDENT INFORMATION HOLDER
    // ==========================================

    private static class StudentInfo {

        String srCode;
        String name;
        String program;
        String yearLevel;
        String block;

        StudentInfo(
                String srCode,
                String name,
                String program,
                String yearLevel,
                String block) {

            this.srCode = srCode;
            this.name = name;
            this.program = program;
            this.yearLevel = yearLevel;
            this.block = block;
        }
    }
}
