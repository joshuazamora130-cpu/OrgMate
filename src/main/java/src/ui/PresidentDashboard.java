
package src.ui;

import java.util.Scanner;

import src.model.President;

public class PresidentDashboard {

    public static void show(Scanner input, President president) {

        int choice;

        do {
            System.out.println("\n======================================");
            System.out.println("          PRESIDENT DASHBOARD");
            System.out.println("======================================");
            System.out.println("Welcome, " + president.getStudentName());
            System.out.println("[1] View Overall Summary");
            System.out.println("[2] View Membership Report");
            System.out.println("[3] View Payment Collection Summary");
            System.out.println("[4] View Officer Information");
            System.out.println("[5] Logout");
            System.out.println("======================================");

            choice = getChoice(input);

            switch (choice) {

                case 1:
                    System.out.println(president.viewOverallSummary());
                    break;

                case 2:
                    System.out.println(president.viewMembershipReport());
                    break;

                case 3:
                    System.out.println(
                        president.viewPaymentCollectionSummary()
                    );
                    break;

                case 4:
                    president.displayInfo();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);
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
