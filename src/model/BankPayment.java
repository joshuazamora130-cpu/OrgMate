
package src.model;

public class BankPayment extends Payment {

    public BankPayment(double amount, String paymentDate,
                       String status, String referenceNo) {
        super(amount, paymentDate, status, referenceNo);
    }

    @Override
    public void processPayment() {
        System.out.println("\n--- Processing Bank Payment ---");
        System.out.println("Reference No: " + getReferenceNo());
        System.out.println("Amount: P" + getAmount());
        System.out.println("Date: " + getPaymentDate());
        System.out.println("Status: " + getStatus());
    }
}
