
package src.ui;

import java.util.Scanner;

import src.model.Secretary;
import src.model.ImportedStudent;

public class SecretaryDashboard {

    public static void show(Scanner input, Secretary secretary) {

        int choice = 0;

        do {
            System.out.println("\n================================");
            System.out.println("       SECRETARY DASHBOARD");
            System.out.println("================================");
            System.out.println("[1] Register / Update Member Information");
            System.out.println("[2] Search and Manage Member Records");
            System.out.println("[3] Check Membership Status");
            System.out.println("[4] Generate Membership Report");
            System.out.println("[5] View Officer Information");
            System.out.println("[6] Logout");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {

                case 1:
                    registerUpdateMenu(input, secretary);
                    break;

                case 2:
                    searchManageMenu(input, secretary);
                    break;

                case 3:
                    System.out.print("Enter SR Code: ");
                    String statusID = input.nextLine().trim();

                    System.out.println(
                        secretary.checkMembershipStatus(statusID)
                    );
                    break;

                case 4:
                    System.out.println(
                        secretary.generateMembershipReport()
                    );
                    break;

                case 5:
                    secretary.displayInfo();
                    break;

                case 6:
                    System.out.println("\nLogging out...");
                    break;

                default:
                    System.out.println("\nInvalid choice.");
            }

        } while (choice != 6);
    }

    // ==========================================
    // REGISTER / UPDATE MENU
    // ==========================================

    private static void registerUpdateMenu(
            Scanner input, Secretary secretary) {

        System.out.println("\n===== REGISTER / UPDATE MEMBER =====");
        System.out.println("[1] Register Member");
        System.out.println("[2] Update Member Information");
        System.out.println("[3] Back");
        System.out.print("Enter choice: ");

        int choice;

        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            choice = -1;
        }

        switch (choice) {

            case 1:
                registerMember(input, secretary);
                break;

            case 2:
                updateMember(input, secretary);
                break;

            case 3:
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void registerMember(
            Scanner input, Secretary secretary) {

        System.out.println("\n===== REGISTER MEMBER =====");

        System.out.print("Enter SR Code: ");
        String studentID = input.nextLine().trim();

        System.out.print("Enter Student Name: ");
        String name = input.nextLine().trim();

        System.out.print("Enter Program: ");
        String program = input.nextLine().trim();

        System.out.print("Enter Year Level: ");
        String yearLevel = input.nextLine().trim();

        System.out.print("Enter Block: ");
        String block = input.nextLine().trim();

        ImportedStudent student = new ImportedStudent(
            studentID, name, program, yearLevel, block
        );

        if (secretary.registerMember(student)) {
            System.out.println("Student successfully registered!");
        } else {
            System.out.println(
                "Registration failed. Check the SR Code or existing record."
            );
        }
    }

    private static void updateMember(
            Scanner input, Secretary secretary) {

        System.out.print("Enter SR Code: ");
        String studentID = input.nextLine().trim();

        ImportedStudent student =
                secretary.searchMemberRecords(studentID);

        if (student == null) {
            System.out.println("Student record not found.");
            return;
        }

        System.out.println("\nCurrent Information:");
        student.displayInfo();

        System.out.println("\nEnter Updated Information:");

        System.out.print("Student Name: ");
        String name = input.nextLine().trim();

        System.out.print("Program: ");
        String program = input.nextLine().trim();

        System.out.print("Year Level: ");
        String yearLevel = input.nextLine().trim();

        System.out.print("Block: ");
        String block = input.nextLine().trim();

        if (secretary.updateMemberInfo(
                studentID, name, program, yearLevel, block)) {

            System.out.println("Member information updated successfully!");

        } else {
            System.out.println("Unable to update member information.");
        }
    }

    // ==========================================
    // SEARCH / MANAGE MEMBER RECORDS
    // ==========================================

    private static void searchManageMenu(
            Scanner input, Secretary secretary) {

        System.out.println("\n===== SEARCH / MANAGE MEMBER RECORDS =====");
        System.out.println("[1] Search Member Records");
        System.out.println("[2] Manage Member Records");
        System.out.println("[3] View All Member Records");
        System.out.println("[4] Back");
        System.out.print("Enter choice: ");

        int choice;

        try {
            choice = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            choice = -1;
        }

        switch (choice) {

            case 1:
                System.out.print("Enter SR Code: ");
                String searchID = input.nextLine().trim();

                ImportedStudent student =
                        secretary.searchMemberRecords(searchID);

                if (student != null) {
                    student.displayInfo();
                } else {
                    System.out.println("Student record not found.");
                }
                break;

            case 2:
                manageMemberRecords(input, secretary);
                break;

            case 3:
                viewAllRecords(secretary);
                break;

            case 4:
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void manageMemberRecords(
            Scanner input, Secretary secretary) {

        System.out.print("Enter SR Code: ");
        String studentID = input.nextLine().trim();

        ImportedStudent student =
                secretary.searchMemberRecords(studentID);

        if (student == null) {
            System.out.println("Student record not found.");
            return;
        }

        int choice = 0;

        do {
            System.out.println("\n===== MANAGE MEMBER RECORD =====");
            student.displayInfo();

            System.out.println("\n[1] Update Information");
            System.out.println("[2] Delete Record");
            System.out.println("[3] Back");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {

                case 1:
                    updateMember(input, secretary);
                    student = secretary.searchMemberRecords(studentID);
                    break;

                case 2:
                    System.out.print(
                        "Confirm deletion? (Y/N): "
                    );

                    String confirmation =
                            input.nextLine().trim();

                    if (confirmation.equalsIgnoreCase("Y")) {

                        if (secretary.deleteMemberRecord(studentID)) {
                            System.out.println("Record deleted successfully.");
                            return;
                        } else {
                            System.out.println("Unable to delete record.");
                        }

                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                    break;

                case 3:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

            if (student == null) {
                System.out.println("Student record no longer exists.");
                return;
            }

        } while (choice != 3);
    }

    private static void viewAllRecords(Secretary secretary) {

        System.out.println("\n===== ALL MEMBER RECORDS =====");

        for (ImportedStudent student : secretary.getAllMemberRecords()) {
            System.out.println("----------------------------");
            student.displayInfo();
        }

        System.out.println("----------------------------");
        System.out.println(
            "Total Records: " + secretary.getAllMemberRecords().size()
        );
    }
}
