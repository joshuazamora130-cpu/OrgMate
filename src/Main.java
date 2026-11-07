
package src;

import java.util.Scanner;
import src.model.Auth;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int choice;

        do {

            // MAIN MENU
            System.out.println("\n================================");
            System.out.println("       CICS ORGMATE SYSTEM");
            System.out.println("================================");
            System.out.println("[1] Login");
            System.out.println("[2] Sign Up");
            System.out.println("[3] Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {

                case 1:
                    Auth.login(input);
                    break;

                case 2:
                    signUpMenu(input);
                    break;

                case 3:
                    System.out.println("\nThank you for using OrgMate!");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 3);

        input.close();
    }

    // SIGN UP MENU
    private static void signUpMenu(Scanner input) {

        int choice;

        System.out.println("\n================================");
        System.out.println("          SIGN UP");
        System.out.println("================================");
        System.out.println("[1] Student Registration");
        System.out.println("[2] Officer Registration");
        System.out.println("[3] Back to Main Menu");
        System.out.print("Enter choice: ");

        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            choice = -1;
        }

        switch (choice) {

            case 1:
                Auth.signUp(input);
                break;

            case 2:
                Auth.registerOfficer(input);
                break;

            case 3:
                System.out.println("\nReturning to Main Menu...");
                break;

            default:
                System.out.println("\nInvalid choice. Please try again.");
        }
    }
}
