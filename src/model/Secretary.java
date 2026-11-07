
package src.model;

import java.util.Scanner;

public class Secretary extends Officer {

    // Constructor passing manually entered Officer ID
    public Secretary(String studentName, String studentID,
                     String block, String program, String officerID) {
        super(studentName, studentID, block, program, officerID, "Secretary");
    }

    // 1. Register Member Information
    public void registerMember(String name) {
        System.out.println("[Secretary]: Registering member information for " + name);
    }

    // 1. Update Member Information
    public void updateMemberInfo(String studentID) {
        System.out.println("[Secretary]: Updating member information for " + studentID);
    }

    // 2. Search Member Records
    public void searchMemberRecords(String studentID) {
        System.out.println("[Secretary]: Searching member records for " + studentID);
    }

    // 2. Manage Member Records
    public void manageMemberRecords(String studentID) {
        System.out.println("[Secretary]: Managing member records for " + studentID);
    }

    // 3. Check Membership Status
    public void checkMembershipStatus(String studentID) {
        System.out.println("[Secretary]: Checking membership status of " + studentID);
    }

    // 4. Generate Membership Report
    public void generateMembershipReport() {
        System.out.println("[Secretary]: Generating membership report...");
    }

    // ==========================================
    // SECRETARY DASHBOARD
    // ==========================================

    public void showDashboard(Scanner input) {

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
                    registerUpdateMenu(input);
                    break;

                case 2:
                    searchManageMenu(input);
                    break;

                case 3:
                    System.out.print("Enter Student ID: ");
                    String statusID = input.nextLine().trim();
                    checkMembershipStatus(statusID);
                    break;

                case 4:
                    generateMembershipReport();
                    break;

                case 5:
                    displayInfo();
                    break;

                case 6:
                    System.out.println("\nLogging out...");
                    break;

                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }

        } while (choice != 6);
    }

    // ==========================================
    // REGISTER / UPDATE MENU
    // ==========================================

    private void registerUpdateMenu(Scanner input) {

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
                System.out.print("Enter Member Name: ");
                String name = input.nextLine().trim();
                registerMember(name);
                break;

            case 2:
                System.out.print("Enter Student ID: ");
                String studentID = input.nextLine().trim();
                updateMemberInfo(studentID);
                break;

            case 3:
                System.out.println("Returning to Secretary Dashboard...");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // ==========================================
    // SEARCH / MANAGE RECORDS MENU
    // ==========================================

    private void searchManageMenu(Scanner input) {

        System.out.println("\n===== SEARCH / MANAGE MEMBER RECORDS =====");
        System.out.println("[1] Search Member Records");
        System.out.println("[2] Manage Member Records");
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
                System.out.print("Enter Student ID: ");
                String searchID = input.nextLine().trim();
                searchMemberRecords(searchID);
                break;

            case 2:
                System.out.print("Enter Student ID: ");
                String manageID = input.nextLine().trim();
                manageMemberRecords(manageID);
                break;

            case 3:
                System.out.println("Returning to Secretary Dashboard...");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    @Override
    public void performDuty() {
        System.out.println("\n===== SECRETARY FUNCTIONS =====");
        System.out.println("[1] Register / Update Member Information");
        System.out.println("[2] Search and Manage Member Records");
        System.out.println("[3] Check Membership Status");
        System.out.println("[4] Generate Membership Report");
    }
}
