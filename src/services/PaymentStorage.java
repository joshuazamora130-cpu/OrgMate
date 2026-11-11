
package src.services;

import java.util.ArrayList;
import src.model.PaymentTicket;

public class PaymentStorage {

    private static final ArrayList<PaymentTicket> tickets =
            new ArrayList<>();

    private static int ticketCounter = 1;

    public static String generateTicketID() {
        return String.format("ORG-%04d", ticketCounter++);
    }

    public static void addTicket(PaymentTicket ticket) {
        tickets.add(ticket);
    }

    public static ArrayList<PaymentTicket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    public static PaymentTicket findTicket(String ticketID) {
        for (PaymentTicket ticket : tickets) {
            if (ticket.getTicketID().equalsIgnoreCase(ticketID)) {
                return ticket;
            }
        }
        return null;
    }

    public static ArrayList<PaymentTicket> getTicketsByStudent(
            String studentID) {

        ArrayList<PaymentTicket> result = new ArrayList<>();

        for (PaymentTicket ticket : tickets) {
            if (ticket.getStudent().getStudentID().equals(studentID)) {
                result.add(ticket);
            }
        }

        return result;
    }

    // Checks whether the student has an active payment ticket
    public static boolean hasActiveTicket(String studentID) {

        for (PaymentTicket ticket : tickets) {

            if (ticket.getStudent().getStudentID().equals(studentID)) {

                String status = ticket.getStatus();

                if (status.equalsIgnoreCase("Submitted")
                        || status.equalsIgnoreCase("Under Review")
                        || status.equalsIgnoreCase("Needs Update")) {
                    return true;
                }
            }
        }

        return false;
    }

    // Displays all payment tickets belonging to a student
    public static void displayStudentTickets(String studentID) {

        ArrayList<PaymentTicket> studentTickets =
                getTicketsByStudent(studentID);

        System.out.println("\n========== PAYMENT HISTORY ==========");

        if (studentTickets.isEmpty()) {
            System.out.println("No payment tickets found.");
            return;
        }

        for (PaymentTicket ticket : studentTickets) {
            ticket.displayTicket();
        }
    }
}
