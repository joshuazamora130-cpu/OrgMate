package OrgMate.src;
import java.util.Scanner;
import OrgMate.src.model.Student;


public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Registration - Student.java
        System.out.println("===== OrgMate Student Registration =====");

        System.out.print("Enter student name: ");
        String studentName = input.nextLine();

        System.out.print("Enter student ID: ");
        String studentID = input.nextLine();

        System.out.print("Enter block: ");
        String block = input.nextLine();

        System.out.print("Enter program: ");
        String program = input.nextLine();

        Student student = new Student(
            studentName,
            studentID,
            block,
            program
        );
        student.displayInfo();

        // Next Model class comes here
        
        
        // Do not erase this
        input.close();
    }
}
