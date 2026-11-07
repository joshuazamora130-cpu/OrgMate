
package src.model;

public class MayaPayment extends Payment {

    private String mayaNumber;
    private String transactionID;

    public MayaPayment(double amount, String paymentDate,
                       String status, String referenceNo,
                       String mayaNumber, String transactionID) {
        super(amount, paymentDate, status, referenceNo);
        this.mayaNumber = mayaNumber;
        this.transactionID = transactionID;
    }

    public String getMayaNumber() {
        return mayaNumber;
    }

    public void setMayaNumber(String mayaNumber) {
        this.mayaNumber = mayaNumber;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public void processPayment() {
        System.out.println("\n--- Processing Maya Payment ---");
        System.out.println("Maya Number: " + mayaNumber);
        System.out.println("Transaction ID: " + transactionID);
        System.out.println("Reference No: " + getReferenceNo());
        System.out.println("Amount: P" + getAmount());
        System.out.println("Date: " + getPaymentDate());

        setStatus("Pending");
        System.out.println("Status: " + getStatus());
    }
}
