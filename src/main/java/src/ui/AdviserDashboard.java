
package src.ui;

import java.util.Scanner;

import src.model.Adviser;

public class AdviserDashboard {

    public static void show(Scanner input, Adviser adviser) {

        int choice;

        do {

            System.out.println("\n======================================");
            System.out.println("           ADVISER DASHBOARD");
            System.out.println("======================================");

            System.out.println("Welcome, " + adviser.getFacultyName());

            System.out.println("\n[1] View Payment Logs");
            System.out.println("[2] Add Adviser Remarks");
            System.out.println("[3] View Adviser Remarks");
            System.out.println("[4] View Adviser Information");
            System.out.println("[5] Logout");

            System.out.println("======================================");

            choice = getChoice(input);

            switch (choice) {

                case 1:
                    System.out.println(adviser.viewPaymentLog());
                    break;

                case 2:
                    System.out.print("Enter your remarks: ");
                    String remarks = input.nextLine();

                    if (adviser.addRemarks(remarks)) {
                        System.out.println(
                            "Adviser remarks added successfully!"
                        );
                    } else {
                        System.out.println(
                            "Remarks cannot be empty."
                        );
                    }
                    break;

                case 3:
                    System.out.println(adviser.viewAdviserRemarks());
                    break;

                case 4:
                    adviser.displayInfo();
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
