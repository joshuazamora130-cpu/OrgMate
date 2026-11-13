
package src.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentAuditLog {

    private String actor;
    private String action;
    private String remarks;
    private LocalDateTime timestamp;

    public PaymentAuditLog(
            String actor,
            String action,
            String remarks) {

        this(actor, action, remarks, LocalDateTime.now());
    }

    // Used when loading an existing audit record from the database.
    public PaymentAuditLog(
            String actor,
            String action,
            String remarks,
            LocalDateTime timestamp) {

        this.actor = actor;
        this.action = action;
        this.remarks = remarks;
        this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
    }

    public String getActor() {
        return actor;
    }

    public String getAction() {
        return action;
    }

    public String getRemarks() {
        return remarks;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void displayLog() {

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm:ss a");

        System.out.println("-----------------------------------");
        System.out.println("Actor: " + actor);
        System.out.println("Action: " + action);
        System.out.println("Date: " + timestamp.format(formatter));
        System.out.println("Remarks: " + remarks);
    }
}
