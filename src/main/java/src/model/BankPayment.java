
package src.model;

public class BankPayment extends Payment {

    private String bankName;

    public BankPayment(
            double amount,
            String paymentDate,
            String status,
            String referenceNo,
            String bankName) {

        super(amount, paymentDate, status, referenceNo);
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public void processPayment() {

        System.out.println("\n--- Processing Bank Payment ---");
        System.out.println("Bank Name: " + bankName);
        System.out.println("Reference No: " + getReferenceNo());
        System.out.println("Amount: PHP " + getAmount());
        System.out.println("Date: " + getPaymentDate());
        System.out.println("Status: " + getStatus());
    }
}
