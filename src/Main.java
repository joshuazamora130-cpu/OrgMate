package src;

import java.util.Scanner;
import src.model.Student;
import src.model.President;
import src.model.Secretary;
import src.model.Adviser;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // 1. Student Interactive Registration
        System.out.println("===== OrgMate Student Registration =====");

        System.out.print("Enter student name: ");
        String studentName = input.nextLine();

        System.out.print("Enter student ID: ");
        String studentID = input.nextLine();

        System.out.print("Enter block: ");
        String block = input.nextLine();

        System.out.print("Enter program: ");
        String program = input.nextLine();

        Student student = new Student(studentName, studentID, block, program);
        student.displayInfo();

        // 2. Instantiate Officers (President ID: 25-00037 and Secretary ID: 25-0061 used as both Student ID & Officer ID)
        System.out.println("\n==================================================");
        System.out.println("             ORGMATE OFFICER DASHBOARD            ");
        System.out.println("==================================================");

        President president = new President(
            "Maria Santos",
            "25-00037", // Student ID is used as Officer ID
            "CS-2101",
            "BS Computer Science"
        );

        Secretary secretary = new Secretary(
            "Juan Dela Cruz",
            "25-0061",  // Student ID is used as Officer ID
            "IT-2102",
            "BS Information Technology"
        );

        Adviser adviser = new Adviser(
            "Dr. Antonio Reyes",
            "Faculty",
            "CICS Department",
            "25-00001"
        );

        // 3. Execution & Display Logic
        president.displayInfo();
        president.performDuty();

        System.out.println("\n--------------------------------------------------");

        secretary.displayInfo();
        secretary.addMember(student.getStudentName());
        secretary.updateMember(student.getStudentName());
        secretary.performDuty();

        System.out.println("\n--------------------------------------------------");

        adviser.displayInfo();
        adviser.performDuty();

        System.out.println("\n==================================================");

        input.close();
    }
}