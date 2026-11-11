
package src.ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Locale;

import src.model.PaymentTicket;
import src.model.RegularStudent;
import src.model.GCashPayment;
import src.model.MayaPayment;
import src.model.CashPayment;

import src.services.PaymentStorage;

public class PaymentHistory {

    public static void show(
            Scanner input, RegularStudent student) {

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getTicketsByStudent(
                    student.getStudentID()
                );

        System.out.println("\n====================================");
        System.out.println("          PAYMENT HISTORY");
        System.out.println("====================================");

        if (tickets.isEmpty()) {
            System.out.println("No payment tickets found.");
            return;
        }

        for (PaymentTicket ticket : tickets) {
            ticket.displayTicket();
        }

        System.out.print(
            "\nEnter Ticket ID to view or update (0 to return): "
        );

        String ticketID = input.nextLine().trim();

        if (ticketID.equals("0")) {
            return;
        }

        PaymentTicket ticket =
                PaymentStorage.findTicket(ticketID);

        // VERIFY TICKET OWNERSHIP

        if (ticket == null ||
                !ticket.getStudent().getStudentID()
                .equals(student.getStudentID())) {

            System.out.println("Ticket not found.");
            return;
        }

        ticket.displayTicket();

        // ONLY NEEDS UPDATE TICKETS CAN BE EDITED

        if (!ticket.canStudentEdit()) {
            System.out.println(
                "\nThis ticket is locked. You cannot edit it."
            );
            return;
        }

        System.out.println("\n========== UPDATE REQUEST ==========");
        System.out.println("Officer Remarks:");
        System.out.println(ticket.getOfficerRemarks());

        System.out.println("\n[1] Update Payment Information");
        System.out.println("[2] Return");

        System.out.print("Enter choice: ");
        String choice = input.nextLine().trim();

        if (!choice.equals("1")) {
            return;
        }

        updateTicket(input, ticket, student);
    }

    // UPDATE PAYMENT TICKET

    private static void updateTicket(
            Scanner input,
            PaymentTicket ticket,
            RegularStudent student) {

        System.out.println("\n====================================");
        System.out.println("       UPDATE PAYMENT TICKET");
        System.out.println("====================================");

        // CORRECTED PAYMENT DATE

        System.out.print("New Payment Date (MM/DD/YYYY): ");
        String date = input.nextLine().trim();

        if (date.isEmpty()) {
            System.out.println("Payment date is required.");
            return;
        }

        // CORRECTED REFERENCE NUMBER

        System.out.print("New Reference / Receipt Number: ");
        String reference = input.nextLine().trim();

        if (reference.isEmpty()) {
            System.out.println("Reference number is required.");
            return;
        }

        // METHOD-SPECIFIC INFORMATION

        String gcashNumber = null;
        String mayaNumber = null;
        String mayaTransactionID = null;
        String cashCollector = null;

        if (ticket.getPayment() instanceof GCashPayment) {

            System.out.print("New GCash Number: ");
            gcashNumber = input.nextLine().trim();

            if (gcashNumber.isEmpty()) {
                System.out.println("GCash number is required.");
                return;
            }

        } else if (ticket.getPayment() instanceof MayaPayment) {

            System.out.print("New Maya Number: ");
            mayaNumber = input.nextLine().trim();

            System.out.print("New Maya Transaction ID: ");
            mayaTransactionID = input.nextLine().trim();

            if (mayaNumber.isEmpty()
                    || mayaTransactionID.isEmpty()) {

                System.out.println("All Maya details are required.");
                return;
            }

        } else if (ticket.getPayment() instanceof CashPayment) {

            System.out.print(
                "New Name of Person Who Collected the Cash: "
            );

            cashCollector = input.nextLine().trim();

            if (cashCollector.isEmpty()) {
                System.out.println("Cash collector name is required.");
                return;
            }
        }

        // REPLACEMENT RECEIPT

        System.out.println("\n========== RECEIPT UPDATE ==========");
        System.out.println("Accepted formats: JPG, JPEG, PNG");

        System.out.print("New Receipt Image Path: ");
        String receiptPath = input.nextLine().trim();

        if (receiptPath.isEmpty()) {
            System.out.println("Receipt image is required.");
            return;
        }

        File receiptFile = new File(receiptPath);

        if (!receiptFile.isFile()) {
            System.out.println("Receipt image file does not exist.");
            return;
        }

        String fileName =
                receiptFile.getName().toLowerCase(Locale.ROOT);

        if (!(fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".png"))) {

            System.out.println(
                "Invalid receipt format. Use JPG, JPEG, or PNG."
            );
            return;
        }

        // CORRECTION SUMMARY

        System.out.println("\n========== CORRECTION SUMMARY ==========");
        System.out.println("Payment Date: " + date);
        System.out.println("Reference Number: " + reference);

        if (gcashNumber != null) {
            System.out.println("GCash Number: " + gcashNumber);
        }

        if (mayaNumber != null) {
            System.out.println("Maya Number: " + mayaNumber);
            System.out.println(
                "Maya Transaction ID: " + mayaTransactionID
            );
        }

        if (cashCollector != null) {
            System.out.println("Cash Collected By: " + cashCollector);
        }

        System.out.println("New Receipt: " + receiptFile.getName());

        System.out.print(
            "\nResubmit these corrections for officer review? (Y/N): "
        );

        String confirmation = input.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Update cancelled. No changes saved.");
            return;
        }

        // APPLY CORRECTIONS

        ticket.updatePaymentDetails(date, reference);
        ticket.updateReceiptPath(receiptPath);

        if (cashCollector != null) {
            ticket.updateCashCollectorName(cashCollector);
        }

        if (ticket.getPayment() instanceof GCashPayment) {

            ((GCashPayment) ticket.getPayment())
                    .setGcashNo(gcashNumber);

        } else if (ticket.getPayment() instanceof MayaPayment) {

            ((MayaPayment) ticket.getPayment())
                    .setMayaNumber(mayaNumber);

            ((MayaPayment) ticket.getPayment())
                    .setTransactionID(mayaTransactionID);
        }

        // RECORD CORRECTIONS IN AUDIT TRAIL

        StringBuilder auditRemarks = new StringBuilder();

        auditRemarks.append(
            "Student corrected payment date and reference number."
        );

        auditRemarks.append(
            " Updated payment method details where applicable."
        );

        auditRemarks.append(
            " Replacement receipt: " + receiptFile.getName() + "."
        );

        if (cashCollector != null) {
            auditRemarks.append(
                " Updated cash collector information."
            );
        }

        ticket.addAuditLog(
            student.getStudentName(),
            "Payment Details Updated",
            auditRemarks.toString()
        );

        // RESUBMIT CORRECTED TICKET

        ticket.resubmit();

        System.out.println("\n====================================");
        System.out.println("   PAYMENT TICKET RESUBMITTED!");
        System.out.println("====================================");

        System.out.println("Ticket ID: " + ticket.getTicketID());
        System.out.println("New Status: Submitted");

        System.out.println(
            "Your corrections have been sent to the Treasurer."
        );
    }
}
