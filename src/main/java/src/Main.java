
package src;

import java.util.Scanner;
import src.ui.Login;
import src.ui.SignUp;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int choice;

        do {

            System.out.println("\n================================");
            System.out.println("       CICS ORGMATE SYSTEM");
            System.out.println("================================");
            System.out.println("[1] Login");
            System.out.println("[2] Sign Up");
            System.out.println("[3] Exit");
            System.out.println("================================");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {

                case 1:
                    Login.show(input);
                    break;

                case 2:
                    SignUp.show(input);
                    break;

                case 3:
                    System.out.println(
                        "\nThank you for using CICS OrgMate!"
                    );
                    break;

                default:
                    System.out.println(
                        "\nInvalid choice. Please try again."
                    );
            }

        } while (choice != 3);

        input.close();
    }
}