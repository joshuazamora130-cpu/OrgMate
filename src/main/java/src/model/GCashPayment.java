
package src.model;

public class GCashPayment extends Payment {

    private String gcashNo;

    public GCashPayment(double amount, String paymentDate,
                        String status, String referenceNo,
                        String gcashNo) {
        super(amount, paymentDate, status, referenceNo);
        this.gcashNo = gcashNo;
    }

    public String getGcashNo() {
        return gcashNo;
    }

    public void setGcashNo(String gcashNo) {
        this.gcashNo = gcashNo;
    }

    @Override
    public void processPayment() {
        System.out.println("\n--- Processing GCash Payment ---");
        System.out.println("GCash Number: " + gcashNo);
        System.out.println("Reference No: " + getReferenceNo());
        System.out.println("Amount: P" + getAmount());
        System.out.println("Date: " + getPaymentDate());
        System.out.println("Status: " + getStatus());
        System.out.println("GCash payment details submitted for verification.");
    }
}
