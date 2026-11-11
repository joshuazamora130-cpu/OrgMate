
package src.services;

import java.util.ArrayList;
import src.model.ImportedStudent;

public class StudentRosterStorage {

    private static ArrayList<ImportedStudent> studentRoster =
            new ArrayList<>();

    // Add a student to the official roster
    public static boolean addStudent(ImportedStudent student) {

        if (student == null || findStudent(student.getStudentID()) != null) {
            return false;
        }

        studentRoster.add(student);
        return true;
    }

    // Find a student using their SR Code
    public static ImportedStudent findStudent(String studentID) {

        if (studentID == null) {
            return null;
        }

        for (ImportedStudent student : studentRoster) {
            if (student.getStudentID().equalsIgnoreCase(studentID.trim())) {
                return student;
            }
        }

        return null;
    }

    // Update an existing student record
    public static boolean updateStudent(
            String studentID,
            String studentName,
            String program,
            String yearLevel,
            String block) {

        ImportedStudent student = findStudent(studentID);

        if (student == null) {
            return false;
        }

        student.setStudentName(studentName);
        student.setProgram(program);
        student.setYearLevel(yearLevel);
        student.setBlock(block);

        return true;
    }

    // Delete a student record
    public static boolean deleteStudent(String studentID) {

        ImportedStudent student = findStudent(studentID);

        if (student == null) {
            return false;
        }

        studentRoster.remove(student);
        return true;
    }

    // Retrieve all official student records
    public static ArrayList<ImportedStudent> getAllStudents() {
        return new ArrayList<>(studentRoster);
    }

    // Get total official student population
    public static int getTotalStudents() {
        return studentRoster.size();
    }

    // Display all official student records
    public static void displayAllStudents() {

        if (studentRoster.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        for (ImportedStudent student : studentRoster) {
            System.out.println("----------------------------");
            student.displayInfo();
        }

        System.out.println("----------------------------");
        System.out.println("Total Students: " + getTotalStudents());
    }
}
