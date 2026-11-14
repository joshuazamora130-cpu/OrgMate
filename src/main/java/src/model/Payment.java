
package src.model;

public abstract class Payment {

    private double amount;
    private String paymentDate;
    private String status;
    private String referenceNo;

    public Payment(double amount, String paymentDate,
                    String status, String referenceNo) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.referenceNo = referenceNo;
    }

    // Getters
    public double getAmount() {
        return amount;
    }

    public double getPaymentAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getDate() {
        return paymentDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
}

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    

    // Abstract method
    public abstract void processPayment();
}
