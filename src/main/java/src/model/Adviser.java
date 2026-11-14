
package src.model;

import java.util.ArrayList;

import src.services.PaymentStorage;

public class Adviser {

    private String facultyName;
    private String facultyID;

    private ArrayList<String> adviserRemarks;

    // CONSTRUCTOR
    public Adviser(String facultyName, String facultyID) {

        this.facultyName = facultyName;
        this.facultyID = facultyID;

        this.adviserRemarks = new ArrayList<>();
    }

    // FACULTY INFORMATION
    public String getFacultyName() {
        return facultyName;
    }

    public String getFacultyID() {
        return facultyID;
    }

    public void displayInfo() {

        System.out.println("\n======================================");
        System.out.println("          FACULTY ADVISER");
        System.out.println("======================================");

        System.out.println("Faculty Name: " + facultyName);
        System.out.println("Faculty ID: " + facultyID);
        System.out.println("Role: Faculty Adviser");

        System.out.println("======================================");
    }

    // VIEW PAYMENT LOGS
    public String viewPaymentLog() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        StringBuilder logs = new StringBuilder();

        logs.append("\n======================================\n");
        logs.append("          FINANCIAL PAYMENT LOGS\n");
        logs.append("======================================\n");

        if (tickets.isEmpty()) {
            return logs.append(
                    "No payment records available.\n"
            ).toString();
        }

        for (PaymentTicket ticket : tickets) {

            logs.append("\nTicket ID: ")
                    .append(ticket.getTicketID());

            logs.append("\nStudent: ")
                    .append(ticket.getStudent().getStudentName());

            logs.append("\nSR Code: ")
                    .append(ticket.getStudent().getStudentID());

            logs.append("\nPayment Status: ")
                    .append(ticket.getStatus());

            if (ticket.getPayment() != null) {

                logs.append("\nPayment Amount: PHP ")
                        .append(String.format(
                                "%.2f",
                                ticket.getPayment().getAmount()
                        ));

                logs.append("\nPayment Method: ")
                        .append(ticket.getPayment()
                                .getClass().getSimpleName());
            }

            logs.append("\n--------------------------------------\n");
        }

        logs.append("Total Payment Tickets: ")
                .append(tickets.size()).append("\n");

        return logs.toString();
    }

    // ADD ADVISER REMARKS
    public boolean addRemarks(String remarks) {

        if (remarks == null || remarks.trim().isEmpty()) {
            return false;
        }

        adviserRemarks.add(remarks.trim());

        return true;
    }

    // GET ADVISER REMARKS
    public ArrayList<String> getAdviserRemarks() {

        return new ArrayList<>(adviserRemarks);
    }

    // VIEW ADVISER REMARKS
    public String viewAdviserRemarks() {

        StringBuilder result = new StringBuilder();

        result.append("\n======================================\n");
        result.append("           ADVISER REMARKS\n");
        result.append("======================================\n");

        if (adviserRemarks.isEmpty()) {
            return result.append(
                    "No adviser remarks available.\n"
            ).toString();
        }

        for (int i = 0; i < adviserRemarks.size(); i++) {

            result.append(i + 1)
                    .append(". ")
                    .append(adviserRemarks.get(i))
                    .append("\n");
        }

        return result.toString();
    }
}
