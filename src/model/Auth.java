
package src.model;

import java.util.ArrayList;
import java.util.Scanner;

public class Auth {

    // Temporary account storage
    private static ArrayList<Account> accounts = new ArrayList<>();

    // Account class
    private static class Account {

        Student student;
        String password;
        String yearLevel;
        String classification;
        String role;

        Account(Student student, String password,
                String yearLevel, String classification,
                String role) {

            this.student = student;
            this.password = password;
            this.yearLevel = yearLevel;
            this.classification = classification;
            this.role = role;
        }
    }

// Sign Up Auth

    public static void signUp(Scanner input) {

        System.out.println("\n========== STUDENT REGISTRATION ==========");

        System.out.print("Enter Full Name: ");
        String name = input.nextLine().trim();

        System.out.print("Enter SR Code: ");
        String srCode = input.nextLine().trim();

        if (!srCode.matches("\\d{2}-\\d{5}")) {

            System.out.println("\nERROR: Invalid SR Code format!");
            System.out.println("Required format: 24-12345");
            return;
        }
        
        // Check duplicate SR Code

        if (findAccount(srCode) != null) {

            System.out.println("\nERROR: SR Code already exists!");
            return;
        }

        System.out.print("Enter Year Level (1st-4th): ");
        String yearLevel = input.nextLine().trim();

        // Validate year level

        if (!yearLevel.matches("1st|2nd|3rd|4th")) {

            System.out.println("\nERROR: Invalid year level!");
            return;
        }

        System.out.print("Enter Classification (Regular/Irregular): ");
        String classification = input.nextLine().trim();
    
        // Validate classification

        if (!classification.equalsIgnoreCase("Regular") &&
            !classification.equalsIgnoreCase("Irregular")) {

            System.out.println("\nERROR: Invalid classification!");
            return;
        }

        System.out.print("Enter Block: ");
        String block = input.nextLine().trim();

        System.out.print("Enter Program (CS/IT): ");
        String program = input.nextLine().trim().toUpperCase();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        // Validate password requirements

        if (!isValidPassword(password)) {

            System.out.println("\nERROR: Password does not meet requirements!");
            System.out.println("- At least 8 characters");
            System.out.println("- One uppercase letter");
            System.out.println("- One lowercase letter");
            System.out.println("- One number");
            return;
        }


        System.out.print("Confirm Password: ");
        String confirmPassword = input.nextLine();
        
        // Validate password confirmation

        if (!password.equals(confirmPassword)) {

            System.out.println("\nERROR: Passwords do not match!");
            return;
        }

        // Check required fields

        if (name.isEmpty() || srCode.isEmpty() ||
            yearLevel.isEmpty() || classification.isEmpty() ||
            block.isEmpty() || program.isEmpty() ||
            password.isEmpty() || confirmPassword.isEmpty()) {

            System.out.println("\nERROR: All fields are required!");
            return;
        }

        // 8. Validate program

        if (!program.equals("CS") && !program.equals("IT")) {

            System.out.println("\nERROR: Invalid program!");
            return;
        }

        // Create Student object

        Student student = new Student(
            name,
            srCode,
            block,
            program
        );

        // Create account
        // Default role: RegularStudent

        Account newAccount = new Account(
            student,
            password,
            yearLevel,
            classification,
            "RegularStudent"
        );

        accounts.add(newAccount);

        System.out.println("\n================================");
        System.out.println("ACCOUNT SUCCESSFULLY CREATED!");
        System.out.println("================================");

        redirectDashboard(newAccount);
    }

    // Login Auth

    public static void login(Scanner input) {

        System.out.println("\n============== LOGIN ==============");

        System.out.print("Enter SR Code: ");
        String srCode = input.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        Account account = findAccount(srCode);

        // Check credentials

        if (account == null || !account.password.equals(password)) {

            System.out.println("\nIncorrect credentials.");
            System.out.println("Please try again!");
            return;
        }

        System.out.println("\nLogin Successful!");

        redirectDashboard(account);
    }

    // Find Account

    private static Account findAccount(String srCode) {

        for (Account account : accounts) {

            if (account.student.getStudentID().equals(srCode)) {
                return account;
            }
        }

        return null;
    }

    // Password Validation

    private static boolean isValidPassword(String password) {

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            }

            if (Character.isLowerCase(character)) {
                hasLowercase = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }
        }

        return password.length() >= 8 &&
               hasUppercase &&
               hasLowercase &&
               hasNumber;
    }

    // Dashboard Redirection

    private static void redirectDashboard(Account account) {

        switch (account.role) {

            case "President":
                officerDashboard("PRESIDENT", account);
                break;

            case "Treasurer":
                officerDashboard("TREASURER", account);
                break;

            case "Auditor":
                officerDashboard("AUDITOR", account);
                break;

            case "Secretary":
                officerDashboard("SECRETARY", account);
                break;

            case "Adviser":
                officerDashboard("ADVISER", account);
                break;

            default:
                studentDashboard(account);
                break;
        }
    }

    // Student Das

    private static void studentDashboard(Account account) {

        System.out.println("\n================================");
        System.out.println("       STUDENT DASHBOARD");
        System.out.println("================================");

        account.student.displayInfo();

        System.out.println("Year Level: " + account.yearLevel);
        System.out.println("Classification: " + account.classification);

        System.out.println("\nWelcome to OrgMate!");
    }

    // ==========================================
    // OFFICER DASHBOARD
    // ==========================================

    private static void officerDashboard(
            String position, Account account) {

        System.out.println("\n================================");
        System.out.println("       " + position + " DASHBOARD");
        System.out.println("================================");

        account.student.displayInfo();

        System.out.println("Position: " + position);
        System.out.println("Year Level: " + account.yearLevel);
        System.out.println("Classification: " + account.classification);

        System.out.println("\nWelcome to OrgMate!");
    }
}
