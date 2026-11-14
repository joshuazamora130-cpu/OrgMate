
package src.model;

import java.util.ArrayList;
import src.services.PaymentStorage;

public class Auditor extends Officer {

    public Auditor(
            String studentName,
            String studentID,
            String block,
            String program,
            String officerID) {

        super(
            studentName,
            studentID,
            block,
            program,
            officerID,
            "Auditor"
        );
    }

    // DUTY 1: REVIEW PAYMENT TRANSACTIONS

    public void reviewPaymentTransactions() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        System.out.println("\n========== PAYMENT TRANSACTION REVIEW ==========");

        if (tickets.isEmpty()) {
            System.out.println("No payment transactions available.");
            return;
        }

        for (PaymentTicket ticket : tickets) {

            System.out.println("\n--------------------------------------");

            System.out.println("Ticket ID: " + ticket.getTicketID());
            System.out.println("Student: "
                    + ticket.getStudent().getStudentName());
            System.out.println("SR Code: "
                    + ticket.getStudent().getStudentID());

            System.out.printf(
                "Amount: PHP %.2f%n",
                ticket.getPayment().getAmount()
            );

            System.out.println("Payment Date: "
                    + ticket.getPayment().getPaymentDate());

            System.out.println("Reference Number: "
                    + ticket.getPayment().getReferenceNo());

            System.out.println("Payment Status: "
                    + ticket.getStatus());

            System.out.println("Receipt: "
                    + ticket.getReceiptPath());

            System.out.println("Reviewed by Auditor: "
                    + getStudentName());

            System.out.println("\nTransaction Audit History:");

            ticket.displayAuditTrail();
        }

        System.out.println("\n========== REVIEW COMPLETED ==========");
    }

    // DUTY 2: GENERATE PAYMENT REPORT

    public void generatePaymentReport() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        int totalTickets = tickets.size();
        int submitted = 0;
        int underReview = 0;
        int reflected = 0;
        int rejected = 0;
        int needsUpdate = 0;

        double totalReflected = 0;
        double totalSubmitted = 0;

        for (PaymentTicket ticket : tickets) {

            String status = ticket.getStatus();
            double amount = ticket.getPayment().getAmount();

            if (status.equalsIgnoreCase("Submitted")) {
                submitted++;
                totalSubmitted += amount;

            } else if (status.equalsIgnoreCase("Under Review")) {
                underReview++;

            } else if (status.equalsIgnoreCase("Reflected")) {
                reflected++;
                totalReflected += amount;

            } else if (status.equalsIgnoreCase("Rejected")) {
                rejected++;

            } else if (status.equalsIgnoreCase("Needs Update")) {
                needsUpdate++;
            }
        }

        System.out.println("\n========== AUDIT PAYMENT REPORT ==========");

        System.out.println("Prepared by: " + getStudentName());

        System.out.println("\nTRANSACTION SUMMARY");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Submitted: " + submitted);
        System.out.println("Under Review: " + underReview);
        System.out.println("Reflected: " + reflected);
        System.out.println("Rejected: " + rejected);
        System.out.println("Needs Update: " + needsUpdate);

        System.out.println("\nFINANCIAL SUMMARY");

        System.out.printf(
            "Total Submitted Amount: PHP %.2f%n",
            totalSubmitted
        );

        System.out.printf(
            "Total Reflected Amount: PHP %.2f%n",
            totalReflected
        );

        System.out.println("\n==========================================");
    }

    // DUTY 3: CHECK PAYMENT REPORT ACCURACY

    public void checkReportAccuracy() {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getAllTickets();

        int checked = 0;
        int accurate = 0;
        int withDiscrepancies = 0;

        System.out.println("\n========== PAYMENT ACCURACY AUDIT ==========");

        if (tickets.isEmpty()) {
            System.out.println("No records available for auditing.");
            return;
        }

        for (PaymentTicket ticket : tickets) {

            checked++;

            ArrayList<String> issues = new ArrayList<>();

            if (ticket.getStudent() == null) {
                issues.add("Missing student information.");
            }

            if (ticket.getPayment() == null) {
                issues.add("Missing payment information.");
            } else {

                if (ticket.getPayment().getAmount() <= 0) {
                    issues.add("Invalid payment amount.");
                }

                if (ticket.getPayment().getPaymentDate() == null
                        || ticket.getPayment().getPaymentDate().trim().isEmpty()) {
                    issues.add("Missing payment date.");
                }

                if (ticket.getPayment().getReferenceNo() == null
                        || ticket.getPayment().getReferenceNo().trim().isEmpty()) {
                    issues.add("Missing payment reference number.");
                }

                if (!ticket.getStatus().equalsIgnoreCase(
                        ticket.getPayment().getStatus())) {
                    issues.add("Ticket and payment statuses do not match.");
                }
            }

            if (ticket.getReceiptPath() == null
                    || ticket.getReceiptPath().trim().isEmpty()) {
                issues.add("Missing receipt information.");
            }

            if (ticket.getOrganizations() == null
                    || ticket.getOrganizations().isEmpty()) {
                issues.add("No organization records found.");
            }

            System.out.println("\nTicket ID: " + ticket.getTicketID());

            if (issues.isEmpty()) {

                accurate++;

                System.out.println("Result: No discrepancies detected.");

            } else {

                withDiscrepancies++;

                System.out.println("Result: Discrepancies detected.");

                for (String issue : issues) {
                    System.out.println("- " + issue);
                }
            }
        }

        System.out.println("\n========== AUDIT SUMMARY ==========");
        System.out.println("Records Checked: " + checked);
        System.out.println("No Discrepancies: " + accurate);
        System.out.println("With Discrepancies: " + withDiscrepancies);
        System.out.println("===================================");
    }

    // OFFICER DUTY

    @Override
    public void performDuty() {

        System.out.println("\n==================================");
        System.out.println("       AUDITOR DUTY");
        System.out.println("==================================");

        System.out.println("Auditor: " + getStudentName());

        // Performs an actual audit operation.
        checkReportAccuracy();
    }
}
