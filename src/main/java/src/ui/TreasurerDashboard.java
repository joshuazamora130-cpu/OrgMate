
package src.ui;

import java.util.ArrayList;
import java.util.Scanner;

import src.model.Treasurer;
import src.model.PaymentTicket;
import src.services.PaymentStorage;

public class TreasurerDashboard {

    public static void show(Scanner input, Treasurer treasurer) {

        int choice;

        do {
            System.out.println("\n======================================");
            System.out.println("          TREASURER DASHBOARD");
            System.out.println("======================================");
            System.out.println("Welcome, " + treasurer.getStudentName());
            System.out.println("[1] View All Payment Tickets");
            System.out.println("[2] Search Payment Ticket");
            System.out.println("[3] Review Payment Ticket");
            System.out.println("[4] View Payment Report");
            System.out.println("[5] Logout");
            System.out.println("======================================");

            choice = getChoice(input, "Enter choice: ");

            switch (choice) {

                case 1:
                    viewAllTickets();
                    break;

                case 2:
                    searchTicket(input);
                    break;

                case 3:
                    reviewTicket(input, treasurer);
                    break;

                case 4:
                    displayPaymentReport();
                    break;

                case 5:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);
    }

    // VIEW ALL PAYMENT TICKETS

    private static void viewAllTickets() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        System.out.println("\n========== ALL PAYMENT TICKETS ==========");

        if (tickets.isEmpty()) {
            System.out.println("No payment tickets have been submitted.");
            return;
        }

        for (PaymentTicket ticket : tickets) {

            System.out.println("--------------------------------------");
            System.out.println("Ticket ID: " + ticket.getTicketID());
            System.out.println("Student: "
                    + ticket.getStudent().getStudentName());
            System.out.println("SR Code: "
                    + ticket.getStudent().getStudentID());

            System.out.printf(
                "Amount: PHP %.2f%n",
                ticket.getPayment().getAmount()
            );

            System.out.println("Payment Method: "
                    + ticket.getPayment().getClass().getSimpleName());

            System.out.println("Status: " + ticket.getStatus());
        }

        System.out.println("--------------------------------------");
    }

    // SEARCH PAYMENT TICKET

    private static void searchTicket(Scanner input) {

        System.out.print("\nEnter Ticket ID: ");
        String ticketID = input.nextLine().trim();

        PaymentTicket ticket =
                PaymentStorage.findTicket(ticketID);

        if (ticket == null) {
            System.out.println("Payment ticket not found.");
            return;
        }

        ticket.displayTicket();
    }

    // REVIEW PAYMENT TICKET

    private static void reviewTicket(
            Scanner input,
            Treasurer treasurer) {

        System.out.print("\nEnter Ticket ID to review: ");
        String ticketID = input.nextLine().trim();

        PaymentTicket ticket =
                PaymentStorage.findTicket(ticketID);

        if (ticket == null) {
            System.out.println("Payment ticket not found.");
            return;
        }

        if (ticket.getStatus().equalsIgnoreCase("Reflected")) {
            System.out.println(
                "This ticket is already reflected and cannot be changed."
            );

            ticket.displayTicket();
            return;
        }

        if (ticket.getStatus().equalsIgnoreCase("Rejected")) {
            System.out.println(
                "This ticket has already been rejected."
            );

            ticket.displayTicket();
            return;
        }

        System.out.println("\n========== REVIEW PAYMENT TICKET ==========");

        ticket.displayTicket();

        System.out.println("\nSelect Review Action:");
        System.out.println("[1] Mark as Under Review");
        System.out.println("[2] Mark as Reflected");
        System.out.println("[3] Reject Payment");
        System.out.println("[4] Request Student Update");
        System.out.println("[5] Cancel");

        int choice = getChoice(input, "Enter choice: ");

        switch (choice) {

            case 1:

                treasurer.markUnderReview(ticket);

                break;

            case 2:

                treasurer.verifyPayment(ticket);

                break;

            case 3:

                System.out.print("Enter rejection reason: ");
                String rejectionReason = input.nextLine().trim();

                if (rejectionReason.isEmpty()) {
                    System.out.println(
                        "A rejection reason is required."
                    );
                    return;
                }

                treasurer.rejectPayment(
                    ticket,
                    rejectionReason
                );

                break;

            case 4:

                System.out.print(
                    "Enter the information the student needs to update: "
                );

                String updateInstructions = input.nextLine().trim();

                if (updateInstructions.isEmpty()) {
                    System.out.println(
                        "Update instructions are required."
                    );
                    return;
                }

                treasurer.requestStudentUpdate(
                    ticket,
                    updateInstructions
                );

                break;

            case 5:

                System.out.println("Review cancelled.");
                return;

            default:

                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("\nUpdated Ticket Information:");
        ticket.displayTicket();
    }

    // PAYMENT REPORT

    private static void displayPaymentReport() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        double totalReflected = 0;

        int submitted = 0;
        int underReview = 0;
        int rejected = 0;
        int needsUpdate = 0;
        int reflected = 0;

        System.out.println("\n========== PAYMENT REPORT ==========");

        for (PaymentTicket ticket : tickets) {

            String status = ticket.getStatus();

            if (status.equalsIgnoreCase("Reflected")) {

                reflected++;
                totalReflected += ticket.getPayment().getAmount();

            } else if (status.equalsIgnoreCase("Submitted")) {

                submitted++;

            } else if (status.equalsIgnoreCase("Under Review")) {

                underReview++;

            } else if (status.equalsIgnoreCase("Rejected")) {

                rejected++;

            } else if (status.equalsIgnoreCase("Needs Update")) {

                needsUpdate++;
            }
        }

        System.out.println("Total Tickets: " + tickets.size());
        System.out.println("Submitted: " + submitted);
        System.out.println("Under Review: " + underReview);
        System.out.println("Needs Update: " + needsUpdate);
        System.out.println("Rejected: " + rejected);
        System.out.println("Reflected: " + reflected);

        System.out.println("------------------------------------");

        System.out.printf(
            "Total Reflected Payments: PHP %.2f%n",
            totalReflected
        );

        System.out.println("====================================");
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
