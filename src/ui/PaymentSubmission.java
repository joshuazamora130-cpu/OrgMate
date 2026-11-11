
package src.ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Locale;

import src.model.RegularStudent;
import src.model.Payment;
import src.model.GCashPayment;
import src.model.MayaPayment;
import src.model.BankPayment;
import src.model.CashPayment;
import src.model.PaymentTicket;

import src.services.PaymentStorage;
import src.services.MembershipFeeCalculator;

public class PaymentSubmission {

    public static void show(Scanner input, RegularStudent student) {

        System.out.println("\n====================================");
        System.out.println("       MEMBERSHIP FEE SUBMISSION");
        System.out.println("====================================");

        ArrayList<String> organizations;

        try {
            organizations = MembershipFeeCalculator.getOrganizations(
                student.getProgram()
            );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Check for existing active payment ticket
        if (PaymentStorage.hasActiveTicket(student.getStudentID())) {
            System.out.println(
                "You already have a payment ticket awaiting review or update."
            );
            System.out.println("Please check your Payment History.");
            return;
        }

        // Display required organizations
        System.out.println("\nRequired organizations:");

        for (String organization : organizations) {
            System.out.println("- " + organization);
        }

        // Optional JPCS membership
        System.out.print(
            "\nWould you like to join JPCS for PHP 100.00? (Y/N): "
        );

        String jpcsChoice = input.nextLine().trim();

        if (!jpcsChoice.equalsIgnoreCase("Y")
                && !jpcsChoice.equalsIgnoreCase("N")) {

            System.out.println("Invalid choice. Please enter Y or N.");
            return;
        }

        boolean includeJPCS = jpcsChoice.equalsIgnoreCase("Y");

        if (includeJPCS) {
            organizations.add("JPCS");
        }

        // Calculate membership fees
        MembershipFeeCalculator.displayFeeBreakdown(
            student.getProgram(), includeJPCS
        );

        double amount = MembershipFeeCalculator.calculateTotal(
            student.getProgram(), includeJPCS
        );

        // Select payment method
        System.out.println("\n========== PAYMENT METHOD ==========");
        System.out.println("[1] GCash");
        System.out.println("[2] Maya");
        System.out.println("[3] Bank");
        System.out.println("[4] Cash");

        int method;

        try {
            System.out.print("Enter choice: ");
            method = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid payment method.");
            return;
        }

        if (method < 1 || method > 4) {
            System.out.println("Invalid payment method.");
            return;
        }

        // Common payment information
        System.out.print("\nPayment Date (MM/DD/YYYY): ");
        String paymentDate = input.nextLine().trim();

        if (paymentDate.isEmpty()) {
            System.out.println("Payment date is required.");
            return;
        }

        System.out.print("Reference / Receipt Number: ");
        String referenceNo = input.nextLine().trim();

        if (referenceNo.isEmpty()) {
            System.out.println("Reference / Receipt Number is required.");
            return;
        }

        Payment payment;

        // Collect method-specific information
        switch (method) {

            case 1:
                System.out.print("GCash Number: ");
                String gcashNo = input.nextLine().trim();

                if (gcashNo.isEmpty()) {
                    System.out.println("GCash number is required.");
                    return;
                }

                payment = new GCashPayment(
                    amount,
                    paymentDate,
                    "Submitted",
                    referenceNo,
                    gcashNo
                );
                break;

            case 2:
                System.out.print("Maya Number: ");
                String mayaNo = input.nextLine().trim();

                System.out.print("Maya Transaction ID: ");
                String transactionID = input.nextLine().trim();

                if (mayaNo.isEmpty() || transactionID.isEmpty()) {
                    System.out.println("Maya details are required.");
                    return;
                }

                payment = new MayaPayment(
                    amount,
                    paymentDate,
                    "Submitted",
                    referenceNo,
                    mayaNo,
                    transactionID
                );
                break;

            case 3:
                System.out.print("Bank Name: ");
                String bankName = input.nextLine().trim();

                if (bankName.isEmpty()) {
                    System.out.println("Bank name is required.");
                    return;
                }

                // Uses your existing BankPayment constructor.
                // If your constructor accepts bankName, use that
                // existing signature instead.
                payment = new BankPayment(
                    amount,
                    paymentDate,
                    "Submitted",
                    referenceNo
                );
                break;

            case 4:
                payment = new CashPayment(
                    amount,
                    paymentDate,
                    "Submitted",
                    referenceNo
                );
                break;

            default:
                return;
        }

        // Cash collector information
        String cashCollectorName = null;

        if (payment instanceof CashPayment) {

            System.out.println("\n========== CASH PAYMENT ==========");

            System.out.print(
                "Name of Person Who Collected the Cash: "
            );

            cashCollectorName = input.nextLine().trim();

            if (cashCollectorName.isEmpty()) {
                System.out.println("Cash collector name is required.");
                return;
            }
        }

        // Receipt image upload (console file path)
        System.out.println("\n========== RECEIPT UPLOAD ==========");
        System.out.println("Accepted formats: JPG, JPEG, PNG");
        System.out.println(
            "Enter the full path of your payment receipt image."
        );

        System.out.print("Receipt Image Path: ");
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

        String fileName = receiptFile.getName().toLowerCase(Locale.ROOT);

        if (!(fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".png"))) {

            System.out.println(
                "Invalid receipt format. Please use JPG, JPEG, or PNG."
            );
            return;
        }

        // Display submission summary
        System.out.println("\n========== REVIEW YOUR SUBMISSION ==========");

        System.out.println("Student: " + student.getStudentName());
        System.out.println("SR Code: " + student.getStudentID());
        System.out.println("Program: " + student.getProgram());

        System.out.println("\nOrganizations:");

        for (String organization : organizations) {
            System.out.println("- " + organization);
        }

        System.out.printf("\nTotal Amount: PHP %.2f%n", amount);

        System.out.println("Payment Method: "
            + payment.getClass().getSimpleName());

        System.out.println("Payment Date: " + paymentDate);
        System.out.println("Reference Number: " + referenceNo);

        if (cashCollectorName != null) {
            System.out.println(
                "Cash Collected By: " + cashCollectorName
            );
        }

        System.out.println("Receipt Image: " + receiptFile.getName());

        // Final confirmation
        System.out.print(
            "\nSubmit this payment ticket? (Y/N): "
        );

        String confirmation = input.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Payment submission cancelled.");
            return;
        }

        // Generate and save payment ticket
        String ticketID = PaymentStorage.generateTicketID();

        PaymentTicket ticket = new PaymentTicket(
            ticketID,
            student,
            payment,
            organizations,
            receiptPath,
            cashCollectorName
        );

        PaymentStorage.addTicket(ticket);

        // Submission confirmation
        System.out.println("\n====================================");
        System.out.println("   PAYMENT TICKET SUBMITTED!");
        System.out.println("====================================");

        System.out.println("Ticket ID: " + ticketID);
        System.out.printf("Amount: PHP %.2f%n", amount);
        System.out.println("Status: Submitted");

        System.out.println(
            "\nYour payment ticket is now locked."
        );

        System.out.println(
            "Please wait for the Treasurer to review your submission."
        );

        System.out.println("====================================");
    }
}
