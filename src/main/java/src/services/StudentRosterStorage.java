
package src.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import src.DatabaseConnection;
import src.model.ImportedStudent;

public class StudentRosterStorage {

    // Add a student to the official roster
    public static boolean addStudent(ImportedStudent student) {

        if (student == null || student.getStudentID() == null) {
            return false;
        }

        String sql = """
                INSERT INTO student_roster
                (sr_code, student_name, program, year_level, block)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getStudentID().trim());
            statement.setString(2, student.getStudentName());
            statement.setString(3, student.getProgram());
            statement.setString(4, student.getYearLevel());
            statement.setString(5, student.getBlock());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to add student: " + e.getMessage());
            return false;
        }
    }

    // Find a student using their SR Code
    public static ImportedStudent findStudent(String studentID) {

        if (studentID == null || studentID.isBlank()) {
            return null;
        }

        String sql = """
                SELECT sr_code, student_name, program, year_level, block
                FROM student_roster
                WHERE LOWER(sr_code) = LOWER(?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentID.trim());

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new ImportedStudent(
                            result.getString("sr_code"),
                            result.getString("student_name"),
                            result.getString("program"),
                            result.getString("year_level"),
                            result.getString("block")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to find student: " + e.getMessage());
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

        String sql = """
                UPDATE student_roster
                SET student_name = ?,
                    program = ?,
                    year_level = ?,
                    block = ?,
                    updated_at = NOW()
                WHERE LOWER(sr_code) = LOWER(?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentName);
            statement.setString(2, program);
            statement.setString(3, yearLevel);
            statement.setString(4, block);
            statement.setString(5, studentID.trim());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to update student: " + e.getMessage());
            return false;
        }
    }

    // Delete a student record
    public static boolean deleteStudent(String studentID) {

        if (studentID == null || studentID.isBlank()) {
            return false;
        }

        String sql = """
                DELETE FROM student_roster
                WHERE LOWER(sr_code) = LOWER(?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, studentID.trim());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to delete student: " + e.getMessage());
            return false;
        }
    }

    // Retrieve all official student records
    public static ArrayList<ImportedStudent> getAllStudents() {

        ArrayList<ImportedStudent> students = new ArrayList<>();

        String sql = """
                SELECT sr_code, student_name, program, year_level, block
                FROM student_roster
                ORDER BY sr_code ASC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                ImportedStudent student = new ImportedStudent(
                        result.getString("sr_code"),
                        result.getString("student_name"),
                        result.getString("program"),
                        result.getString("year_level"),
                        result.getString("block")
                );

                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve students: " + e.getMessage());
        }

        return students;
    }

    // Get total official student population
    public static int getTotalStudents() {

        String sql = "SELECT COUNT(*) FROM student_roster";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Failed to count students: " + e.getMessage());
        }

        return 0;
    }

    // Display all official student records
    public static void displayAllStudents() {

        ArrayList<ImportedStudent> students = getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No student records available.");
            return;
        }

        for (ImportedStudent student : students) {
            System.out.println("----------------------------");
            student.displayInfo();
        }

        System.out.println("----------------------------");
        System.out.println("Total Students: " + students.size());
    }
}
