
package src.ui;

import java.util.Scanner;

import src.model.Account;
import src.model.RegularStudent;

public class StudentDashboard {

    public static void show(Scanner input, Account account) {

        RegularStudent student =
                (RegularStudent) account.getStudent();

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("       STUDENT DASHBOARD");
            System.out.println("================================");
            System.out.println("[1] View Student Information");
            System.out.println("[2] Submit Membership Payment");
            System.out.println("[3] View Payment History");
            System.out.println("[4] Logout");
            System.out.println("================================");

            choice = getChoice(input);

            switch (choice) {

                case 1:
                    displayStudentInformation(account);
                    break;

                case 2:
                    PaymentSubmission.show(input, student);
                    break;

                    case 3:
                    PaymentHistory.show(input, student);
                break;

                case 4:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);
    }

    private static void displayStudentInformation(Account account) {

        RegularStudent student =
                (RegularStudent) account.getStudent();

        System.out.println("\n================================");
        System.out.println("       STUDENT INFORMATION");
        System.out.println("================================");

        System.out.println("Full Name: " + student.getStudentName());
        System.out.println("SR Code: " + student.getStudentID());
        System.out.println("Block/Section: " + student.getBlock());
        System.out.println("Program: " + student.getProgram());
        System.out.println("Year Level: " + account.getYearLevel());
        System.out.println("Classification: " + account.getClassification());

        System.out.println("================================");
    }

    private static int getChoice(Scanner input) {
        try {
            System.out.print("Enter choice: ");
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
