package src.model;

public class RegularStudent extends Student {
    private String membershipStatus;

    public RegularStudent(String studentName, String studentID, String block, String program) {
        super(studentName, studentID, block, program);
        this.membershipStatus = "Pending";
    }

    public String getMembershipStatus() { return membershipStatus; }
    public void setMembershipStatus(String membershipStatus) { this.membershipStatus = membershipStatus; }

    public void viewMembership() {
        System.out.println("Membership status of " + getStudentName() + ": " + membershipStatus);
    }

    public Payment submitPayment(Payment payment) {
        payment.setStatus("Pending");
        System.out.println(getStudentName() + " submitted payment (Ref: " + payment.getReferenceNo() + ").");
        return payment;
    }

    public void viewPaymentStatus(Payment payment) {
        System.out.println("Payment " + payment.getReferenceNo() + " status: " + payment.getStatus());
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Membership Status: " + membershipStatus);
    }
}
