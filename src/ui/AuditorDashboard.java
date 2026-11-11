
package src.ui;

import java.util.Scanner;
import src.model.Auditor;

public class AuditorDashboard {

    public static void show(Scanner input, Auditor auditor) {

        int choice;

        do {
            System.out.println("\n======================================");
            System.out.println("           AUDITOR DASHBOARD");
            System.out.println("======================================");
            System.out.println("Welcome, " + auditor.getStudentName());
            System.out.println("Officer ID: " + auditor.getOfficerID());
            System.out.println("--------------------------------------");

            System.out.println("[1] Review Payment Transactions");
            System.out.println("[2] Generate Payment Reports");
            System.out.println("[3] Check Report Accuracy");
            System.out.println("[4] Logout");

            System.out.println("======================================");

            choice = getChoice(input, "Enter choice: ");

            switch (choice) {

                case 1:
                    reviewTransactions(auditor);
                    break;

                case 2:
                    generateReport(auditor);
                    break;

                case 3:
                    checkReportAccuracy(auditor);
                    break;

                case 4:
                    System.out.println(
                        "Logging out from Auditor Dashboard..."
                    );
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (choice >= 1 && choice <= 3) {
                System.out.print("\nPress ENTER to continue...");
                input.nextLine();
            }

        } while (choice != 4);
    }

    // REVIEW PAYMENT TRANSACTIONS

    private static void reviewTransactions(Auditor auditor) {

        auditor.reviewPaymentTransactions();
    }

    // GENERATE PAYMENT REPORT

    private static void generateReport(Auditor auditor) {

        auditor.generatePaymentReport();
    }

    // CHECK REPORT ACCURACY

    private static void checkReportAccuracy(Auditor auditor) {

        auditor.checkReportAccuracy();
    }

    // INPUT VALIDATION

    private static int getChoice(
            Scanner input,
            String message) {

        System.out.print(message);

        try {
            return Integer.parseInt(input.nextLine().trim());

        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
