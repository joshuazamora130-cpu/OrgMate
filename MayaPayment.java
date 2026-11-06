package src.model;

public class MayaPayment extends Payment {
    private String mayaNumber;   // Maya account / mobile number
    private String transactionID; // Maya transaction ID

    public MayaPayment(double amount, String paymentDate, String status,
                       String referenceNo, String mayaNumber, String transactionID) {
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
        System.out.println("Processing Maya payment of " + getAmount()
                + " from " + mayaNumber + " (Txn: " + transactionID + ")");
        setStatus("Pending");
    }
}
