
package src.model;

import java.util.ArrayList;
import java.util.Scanner;

public class Auth {

    // In-memory storage only; accounts are cleared when the application exits.
    private static final ArrayList<Account> accounts = new ArrayList<>();

    // Change this code and keep it accessible only to authorized administrators.
    private static final String OFFICER_REGISTRATION_CODE = "ORGMATE-ADMIN-2026";

    private static class Account {
        private final Student student;
        private final String password;
        private final String yearLevel;
        private final String classification;

        private Account(Student student, String password,
                        String yearLevel, String classification) {
            this.student = student;
            this.password = password;
            this.yearLevel = yearLevel;
            this.classification = classification;
        }
    }

    // ==========================================
    // REGULAR STUDENT SIGN UP
    // ==========================================

    public static void signUp(Scanner input) {
        System.out.println("\n========== STUDENT REGISTRATION ==========");

        System.out.print("Enter Full Name: ");
        String name = input.nextLine().trim();

        System.out.print("Enter SR Code: ");
        String srCode = input.nextLine().trim();

        if (!isValidSRCode(srCode)) {
            System.out.println("\nERROR: Invalid SR Code format! Required format: 24-12345");
            return;
        }

        if (findAccount(srCode) != null) {
            System.out.println("\nERROR: SR Code already exists!");
            return;
        }

        String yearLevel = getYearLevel(input);
        if (yearLevel == null) return;

        String classification = getClassification(input);
        if (classification == null) return;

        System.out.print("Enter Block: ");
        String block = input.nextLine().trim();

        System.out.print("Enter Program (CS/IT): ");
        String program = input.nextLine().trim().toUpperCase();

        if (!isValidProgram(program)) {
            System.out.println("\nERROR: Invalid program!");
            return;
        }

        if (name.isEmpty() || block.isEmpty()) {
            System.out.println("\nERROR: All fields are required!");
            return;
        }

        String password = getPassword(input);
        if (password == null) return;

        // A normal signup always creates a RegularStudent model object.
        Student student = new RegularStudent(name, srCode, block, program);

        Account newAccount = new Account(
                student, password, yearLevel, classification
        );

        accounts.add(newAccount);

        System.out.println("\n================================");
        System.out.println("ACCOUNT SUCCESSFULLY CREATED!");
        System.out.println("ACCOUNT TYPE: REGULAR STUDENT");
        System.out.println("================================");

        redirectDashboard(newAccount, input);
    }

    // ==========================================
    // OFFICER REGISTRATION
    // ==========================================

    public static void registerOfficer(Scanner input) {
        System.out.println("\n========== OFFICER REGISTRATION ==========");

        // Authorization
        System.out.print("Enter Officer Registration Code: ");
        String registrationCode = input.nextLine();

        if (!registrationCode.equals(OFFICER_REGISTRATION_CODE)) {
            System.out.println("\nERROR: Unauthorized officer registration!");
            return;
        }

        System.out.println("\nAuthorization successful!");

        // Student information
        System.out.print("Enter Full Name: ");
        String name = input.nextLine().trim();

        System.out.print("Enter SR Code: ");
        String srCode = input.nextLine().trim();

        if (!isValidSRCode(srCode)) {
            System.out.println("\nERROR: Invalid SR Code format! Required format: 24-12345");
            return;
        }

        if (findAccount(srCode) != null) {
            System.out.println("\nERROR: SR Code already exists!");
            return;
        }

        String yearLevel = getYearLevel(input);
        if (yearLevel == null) return;

        String classification = getClassification(input);
        if (classification == null) return;

        System.out.print("Enter Block: ");
        String block = input.nextLine().trim();

        System.out.print("Enter Program (CS/IT): ");
        String program = input.nextLine().trim().toUpperCase();

        if (!isValidProgram(program)) {
            System.out.println("\nERROR: Invalid program!");
            return;
        }

        if (name.isEmpty() || block.isEmpty()) {
            System.out.println("\nERROR: All fields are required!");
            return;
        }

        // Officer position selection
        System.out.println("\n========== SELECT OFFICER POSITION ==========");
        System.out.println("[1] President");
        System.out.println("[2] Secretary");
        System.out.println("[3] Treasurer");
        System.out.println("[4] Auditor");
        System.out.println("[5] Adviser");
        System.out.print("Enter position: ");

        String positionChoice = input.nextLine().trim();

        // All officer positions require a manually entered Officer ID.
        System.out.print("Enter Officer ID: ");
        String officerID = input.nextLine().trim();

        if (officerID.isEmpty()) {
            System.out.println("\nERROR: Officer ID is required!");
            return;
        }

        // Create the appropriate Officer subclass.
        Officer officer = createOfficer(
                positionChoice, name, srCode, block, program, officerID
        );

        if (officer == null) {
            System.out.println("\nERROR: Invalid officer position!");
            return;
        }

        // Password creation
        String password = getPassword(input);
        if (password == null) return;

        Account newAccount = new Account(
                officer, password, yearLevel, classification
        );

        accounts.add(newAccount);

        System.out.println("\n================================");
        System.out.println("OFFICER ACCOUNT SUCCESSFULLY CREATED!");
        System.out.println("POSITION: " + officer.getPosition());
        System.out.println("OFFICER ID: " + officer.getOfficerID());
        System.out.println("================================");

        redirectDashboard(newAccount, input);
    }

    // ==========================================
    // OFFICER OBJECT CREATION
    // ==========================================

    private static Officer createOfficer(
            String choice,
            String name,
            String srCode,
            String block,
            String program,
            String officerID) {

        switch (choice) {
            case "1":
                return new President(
                        name, srCode, block, program, officerID
                );

            case "2":
                return new Secretary(
                        name, srCode, block, program, officerID
                );

            case "3":
                return new Treasurer(
                        name, srCode, block, program, officerID
                );

            case "4":
                return new Auditor(
                        name, srCode, block, program, officerID
                );

            case "5":
                return new Adviser(
                        name, srCode, block, program, officerID
                );

            default:
                return null;
        }
    }

    // ==========================================
    // LOGIN
    // ==========================================

    public static void login(Scanner input) {
        System.out.println("\n============== LOGIN ==============");

        System.out.print("Enter SR Code: ");
        String srCode = input.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        Account account = findAccount(srCode);

        if (account == null || !account.password.equals(password)) {
            System.out.println("\nIncorrect credentials.\nPlease try again!");
            return;
        }

        System.out.println("\nLogin Successful!");

        redirectDashboard(account, input);
    }

    // ==========================================
    // ACCOUNT SEARCH
    // ==========================================

    private static Account findAccount(String srCode) {
        for (Account account : accounts) {
            if (account.student.getStudentID().equals(srCode)) {
                return account;
            }
        }

        return null;
    }

    // ==========================================
    // VALIDATION METHODS
    // ==========================================

    private static boolean isValidSRCode(String srCode) {
        return srCode.matches("\\d{2}-\\d{5}");
    }

    private static boolean isValidProgram(String program) {
        return program.equals("CS") || program.equals("IT");
    }

    private static String getYearLevel(Scanner input) {
        System.out.print("Enter Year Level (1st-4th): ");
        String yearLevel = input.nextLine().trim();

        if (!yearLevel.matches("1st|2nd|3rd|4th")) {
            System.out.println("\nERROR: Invalid year level!");
            return null;
        }

        return yearLevel;
    }

    private static String getClassification(Scanner input) {
        System.out.print("Enter Classification (Regular/Irregular): ");
        String classification = input.nextLine().trim();

        if (!classification.equalsIgnoreCase("Regular")
                && !classification.equalsIgnoreCase("Irregular")) {
            System.out.println("\nERROR: Invalid classification!");
            return null;
        }

        return classification;
    }

    private static String getPassword(Scanner input) {
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        if (!isValidPassword(password)) {
            System.out.println("\nERROR: Password does not meet requirements!");
            System.out.println("- At least 8 characters");
            System.out.println("- One uppercase letter");
            System.out.println("- One lowercase letter");
            System.out.println("- One number");
            return null;
        }

        System.out.print("Confirm Password: ");
        String confirmPassword = input.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("\nERROR: Passwords do not match!");
            return null;
        }

        return password;
    }

    private static boolean isValidPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) hasUppercase = true;
            if (Character.isLowerCase(character)) hasLowercase = true;
            if (Character.isDigit(character)) hasNumber = true;
        }

        return password.length() >= 8
                && hasUppercase
                && hasLowercase
                && hasNumber;
    }

    // ==========================================
    // DASHBOARD REDIRECTION
    // ==========================================

    private static void redirectDashboard(Account account, Scanner input) {

        // Secretary has her own dashboard inside Secretary.java.
        if (account.student instanceof Secretary) {
            ((Secretary) account.student).showDashboard(input);

        } else if (account.student instanceof Officer) {
            Officer officer = (Officer) account.student;

            officerDashboard(
                    officer.getPosition().toUpperCase(), account, input
            );

        } else {
            studentDashboard(account, input);
        }
    }

    // ==========================================
    // STUDENT DASHBOARD
    // ==========================================

    private static void studentDashboard(Account account, Scanner input) {

        int choice = 0;

        do {
            System.out.println("\n================================");
            System.out.println("       STUDENT DASHBOARD");
            System.out.println("================================");
            System.out.println("[1] View Student Information");
            System.out.println("[2] View Membership Status");
            System.out.println("[3] Submit Membership Fee");
            System.out.println("[4] View Payment Status");
            System.out.println("[5] Logout");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    account.student.displayInfo();
                    System.out.println("Year Level: " + account.yearLevel);
                    System.out.println("Classification: " + account.classification);
                    break;

                case 2:
                    if (account.student instanceof RegularStudent) {
                        ((RegularStudent) account.student).viewMembership();
                    }
                    break;

                case 3:
                    System.out.println("\nPayment submission feature coming soon.");
                    break;

                case 4:
                    System.out.println("\nPayment status feature coming soon.");
                    break;

                case 5:
                    System.out.println("\nLogging out...");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 5);
    }

    // ==========================================
    // GENERAL OFFICER DASHBOARD
    // ==========================================

    private static void officerDashboard(
            String position, Account account, Scanner input) {

        int choice = 0;

        do {
            System.out.println("\n================================");
            System.out.println("       " + position + " DASHBOARD");
            System.out.println("================================");
            System.out.println("[1] View Officer Information");
            System.out.println("[2] Perform Officer Duties");
            System.out.println("[3] Logout");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    account.student.displayInfo();
                    break;

                case 2:
                    ((Officer) account.student).performDuty();
                    break;

                case 3:
                    System.out.println("\nLogging out...");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 3);
    }
}
