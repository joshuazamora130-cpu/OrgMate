
package src.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;

import src.services.StudentRosterStorage;
import src.services.PaymentStorage;

public class Secretary extends Officer {

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public Secretary(String studentName, String studentID,
                     String block, String program, String officerID) {

        super(studentName, studentID, block, program,
              officerID, "Secretary");
    }

    // ==========================================
    // 1. REGISTER MEMBER INFORMATION
    // ==========================================

    public boolean registerMember(ImportedStudent student) {

        if (student == null) {
            return false;
        }

        return StudentRosterStorage.addStudent(student);
    }

    // ==========================================
    // UPDATE MEMBER INFORMATION
    // ==========================================

    public boolean updateMemberInfo(
            String studentID,
            String name,
            String program,
            String yearLevel,
            String block) {

        return StudentRosterStorage.updateStudent(
            studentID, name, program, yearLevel, block
        );
    }

    // ==========================================
    // 2. SEARCH MEMBER RECORDS
    // ==========================================

    public ImportedStudent searchMemberRecords(String studentID) {

        return StudentRosterStorage.findStudent(studentID);
    }

    // ==========================================
    // MANAGE MEMBER RECORDS
    // ==========================================

    public boolean deleteMemberRecord(String studentID) {

        return StudentRosterStorage.deleteStudent(studentID);
    }

    public ArrayList<ImportedStudent> getAllMemberRecords() {

        return StudentRosterStorage.getAllStudents();
    }

    // ==========================================
    // 3. CHECK MEMBERSHIP STATUS
    // ==========================================

    public String checkMembershipStatus(String studentID) {

        ImportedStudent student =
                StudentRosterStorage.findStudent(studentID);

        if (student == null) {
            return "Student record not found.";
        }

        ArrayList<PaymentTicket> tickets =
                PaymentStorage.getTicketsByStudent(studentID);

        for (PaymentTicket ticket : tickets) {

            if ("Reflected".equalsIgnoreCase(ticket.getStatus())) {
                return "Paid / Verified";
            }
        }

        return "No Verified Fee Participation";
    }

    // ==========================================
    // 4. GENERATE MEMBERSHIP REPORT
    // ==========================================

    public String generateMembershipReport() {

        ArrayList<ImportedStudent> students =
                StudentRosterStorage.getAllStudents();

        // Program -> [Population, Verified Participants]
        Map<String, int[]> programStats = new LinkedHashMap<>();

        int totalPopulation = students.size();
        int totalParticipants = 0;

        for (ImportedStudent student : students) {

            String program = student.getProgram();

            programStats.putIfAbsent(program, new int[2]);

            // Count population
            programStats.get(program)[0]++;

            // Check verified payment
            boolean verified = false;

            ArrayList<PaymentTicket> studentTickets =
                    PaymentStorage.getTicketsByStudent(
                        student.getStudentID()
                    );

            for (PaymentTicket ticket : studentTickets) {

                if ("Reflected".equalsIgnoreCase(ticket.getStatus())) {
                    verified = true;
                    break;
                }
            }

            if (verified) {
                programStats.get(program)[1]++;
                totalParticipants++;
            }
        }

        int nonParticipants = totalPopulation - totalParticipants;

        double participationRate = totalPopulation == 0
                ? 0
                : (totalParticipants * 100.0) / totalPopulation;

        StringBuilder report = new StringBuilder();

        report.append("\n======================================\n");
        report.append("       MEMBERSHIP PARTICIPATION REPORT\n");
        report.append("======================================\n");

        report.append("Total Student Population: ")
              .append(totalPopulation).append("\n");

        report.append("Verified Fee Participants: ")
              .append(totalParticipants).append("\n");

        report.append("No Verified Fee Participation: ")
              .append(nonParticipants).append("\n");

        report.append(String.format(
            "Overall Participation Rate: %.2f%%%n",
            participationRate
        ));

        report.append("\n===== PROGRAM BREAKDOWN =====\n");

        for (Map.Entry<String, int[]> entry : programStats.entrySet()) {

            String program = entry.getKey();
            int population = entry.getValue()[0];
            int participants = entry.getValue()[1];

            double rate = population == 0
                    ? 0
                    : (participants * 100.0) / population;

            report.append("\nProgram: ").append(program).append("\n");
            report.append("Population: ").append(population).append("\n");

            report.append("Verified Participants: ")
                  .append(participants).append("\n");

            report.append("No Verified Participation: ")
                  .append(population - participants).append("\n");

            report.append(String.format(
                "Participation Rate: %.2f%%%n", rate
            ));
        }

        report.append("\n======================================\n");

        return report.toString();
    }

    // ==========================================
    // OFFICER DUTIES
    // ==========================================

    @Override
    public void performDuty() {

        System.out.println(
            "Secretary is responsible for maintaining "
            + "official student and membership records."
        );
    }
}
