
package src.model;

import java.util.ArrayList;

import src.services.StudentRosterStorage;
import src.services.AccountStorage;
import src.services.PaymentStorage;

public class President extends Officer {

    public President(String studentName, String studentID,
                     String block, String program, String officerID) {
        super(studentName, studentID, block, program, officerID, "President");
    }

    // View overall organizational summary
    public String viewOverallSummary() {

        int totalStudents = StudentRosterStorage.getTotalStudents();
        int totalAccounts = AccountStorage.getAllAccounts().size();

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        int pending = 0;
        int underReview = 0;
        int reflected = 0;
        int rejected = 0;
        int needsUpdate = 0;

        for (PaymentTicket ticket : tickets) {

            String status = ticket.getStatus();

            if ("Pending".equalsIgnoreCase(status)) {
                pending++;
            } else if ("Under Review".equalsIgnoreCase(status)) {
                underReview++;
            } else if ("Reflected".equalsIgnoreCase(status)) {
                reflected++;
            } else if ("Rejected".equalsIgnoreCase(status)) {
                rejected++;
            } else if ("Needs Update".equalsIgnoreCase(status)) {
                needsUpdate++;
            }
        }

        StringBuilder summary = new StringBuilder();

        summary.append("\n======================================\n");
        summary.append("       OVERALL ORGANIZATIONAL SUMMARY\n");
        summary.append("======================================\n");

        summary.append("Official Student Population: ")
               .append(totalStudents).append("\n");

        summary.append("Registered OrgMate Accounts: ")
               .append(totalAccounts).append("\n");

        summary.append("\n--- PAYMENT TICKET SUMMARY ---\n");

        summary.append("Total Payment Tickets: ")
               .append(tickets.size()).append("\n");

        summary.append("Pending: ").append(pending).append("\n");
        summary.append("Under Review: ").append(underReview).append("\n");
        summary.append("Reflected: ").append(reflected).append("\n");
        summary.append("Rejected: ").append(rejected).append("\n");
        summary.append("Needs Update: ").append(needsUpdate).append("\n");

        summary.append("======================================\n");

        return summary.toString();
    }

    // View membership participation report
    public String viewMembershipReport() {

        Secretary secretary = new Secretary(
                getStudentName(),
                getStudentID(),
                getBlock(),
                getProgram(),
                getOfficerID()
        );

        return secretary.generateMembershipReport();
    }

    // View payment collection summary
    public String viewPaymentCollectionSummary() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        int reflectedCount = 0;
        double totalCollected = 0.0;

        for (PaymentTicket ticket : tickets) {

            if ("Reflected".equalsIgnoreCase(ticket.getStatus())) {

                reflectedCount++;

                if (ticket.getPayment() != null) {
                    totalCollected += ticket.getPayment().getAmount();
                }
            }
        }

        StringBuilder report = new StringBuilder();

        report.append("\n======================================\n");
        report.append("       PAYMENT COLLECTION SUMMARY\n");
        report.append("======================================\n");

        report.append("Verified Transactions: ")
              .append(reflectedCount).append("\n");

        report.append(String.format(
                "Total Reflected Amount: PHP %.2f%n",
                totalCollected
        ));

        report.append("======================================\n");

        return report.toString();
    }

    @Override
    public void performDuty() {

        System.out.println("\n--- President Overview ---");

        System.out.println(viewOverallSummary());
    }
}
