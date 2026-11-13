
package src.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import src.DatabaseConnection;
import src.model.*;

public class AccountStorage {

    // ==========================================
    // ADD ACCOUNT AND STUDENT INFORMATION
    // ==========================================

    public static boolean addAccount(Account account) {

        if (account == null || account.getStudent() == null
                || account.getPassword() == null) {
            return false;
        }

        String studentSQL = """
                INSERT INTO student_roster
                (sr_code, student_name, program, year_level, block)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (sr_code)
                DO UPDATE SET
                    student_name = EXCLUDED.student_name,
                    program = EXCLUDED.program,
                    year_level = EXCLUDED.year_level,
                    block = EXCLUDED.block,
                    updated_at = NOW()
                """;

        String accountSQL = """
                INSERT INTO accounts
                (sr_code, password_hash, classification)
                VALUES (?, ?, ?)
                RETURNING account_id
                """;

        String officerSQL = """
                INSERT INTO officer_assignments
                (account_id, officer_id, position)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                var student = account.getStudent();

                // 1. Save student information
                try (PreparedStatement statement =
                        connection.prepareStatement(studentSQL)) {

                    statement.setString(1, student.getStudentID());
                    statement.setString(2, student.getStudentName());
                    statement.setString(3, student.getProgram());
                    statement.setString(4, account.getYearLevel());
                    statement.setString(5, student.getBlock());

                    statement.executeUpdate();
                }

                // 2. Hash password
                String passwordHash = BCrypt.hashpw(
                        account.getPassword(),
                        BCrypt.gensalt(12)
                );

                // 3. Create account and retrieve UUID
                UUID accountID;

                try (PreparedStatement statement =
                        connection.prepareStatement(accountSQL)) {

                    statement.setString(1, student.getStudentID());
                    statement.setString(2, passwordHash);
                    statement.setString(
                            3, account.getClassification()
                    );

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            connection.rollback();
                            return false;
                        }

                        accountID = result.getObject(
                                "account_id", UUID.class
                        );
                    }
                }

                // 4. Save officer assignment if applicable
                if (student instanceof Officer officer) {

                    try (PreparedStatement statement =
                            connection.prepareStatement(officerSQL)) {

                        statement.setObject(1, accountID);
                        statement.setString(
                                2, officer.getOfficerID()
                        );
                        statement.setString(
                                3, officer.getPosition()
                        );

                        statement.executeUpdate();
                    }
                }

                // 5. Commit all changes
                connection.commit();
                return true;

            } catch (SQLException | RuntimeException e) {

                connection.rollback();

                System.out.println(
                        "Account registration failed: " + e.getMessage()
                );

                return false;

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage()
            );

            return false;
        }
    }

    // ==========================================
    // ACCOUNT RETRIEVAL QUERY
    // ==========================================

    private static final String ACCOUNT_QUERY = """
            SELECT
                a.account_id,
                a.sr_code,
                a.password_hash,
                a.classification,
                s.student_name,
                s.program,
                s.year_level,
                s.block,
                oa.officer_id,
                oa.position
            FROM accounts a
            INNER JOIN student_roster s
                ON a.sr_code = s.sr_code
            LEFT JOIN officer_assignments oa
                ON a.account_id = oa.account_id
                AND oa.is_active = TRUE
            """;

    // ==========================================
    // RECONSTRUCT ACCOUNT OBJECT
    // ==========================================

    private static Account createAccount(ResultSet result)
            throws SQLException {

        String name = result.getString("student_name");
        String srCode = result.getString("sr_code");
        String program = result.getString("program");
        String block = result.getString("block");
        String yearLevel = result.getString("year_level");

        String passwordHash = result.getString("password_hash");
        String classification = result.getString("classification");

        String officerID = result.getString("officer_id");
        String position = result.getString("position");

        Student student;

        if (position != null) {

            switch (position.toLowerCase()) {

                case "president":
                    student = new President(
                            name, srCode, block, program, officerID);
                    break;

                case "secretary":
                    student = new Secretary(
                            name, srCode, block, program, officerID);
                    break;

                case "treasurer":
                    student = new Treasurer(
                            name, srCode, block, program, officerID);
                    break;

                case "auditor":
                    student = new Auditor(
                            name, srCode, block, program, officerID);
                    break;

                default:
                    student = new RegularStudent(
                            name, srCode, block, program);
            }

        } else {

            student = new RegularStudent(
                    name, srCode, block, program);
        }

        return new Account(
                student,
                passwordHash,
                yearLevel,
                classification
        );
    }

    // ==========================================
    // FIND ACCOUNT
    // ==========================================

    public static Account findAccount(String srCode) {

        if (srCode == null || srCode.isBlank()) {
            return null;
        }

        String sql = ACCOUNT_QUERY + """
                WHERE LOWER(a.sr_code) = LOWER(?)
                LIMIT 1
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, srCode.trim());

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return createAccount(result);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to find account: " + e.getMessage());
        }

        return null;
    }

    // ==========================================
    // CHECK EXISTING ACCOUNT
    // ==========================================

    public static boolean accountExists(String srCode) {

        if (srCode == null || srCode.isBlank()) {
            return false;
        }

        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM accounts
                    WHERE LOWER(sr_code) = LOWER(?)
                )
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, srCode.trim());

            try (ResultSet result = statement.executeQuery()) {
                return result.next() && result.getBoolean(1);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to check account: " + e.getMessage());
        }

        return false;
    }

    // ==========================================
    // GET ALL ACCOUNTS
    // ==========================================

    public static ArrayList<Account> getAllAccounts() {

        ArrayList<Account> accounts = new ArrayList<>();

        String sql = ACCOUNT_QUERY + " ORDER BY a.sr_code ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                accounts.add(createAccount(result));
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to retrieve accounts: " + e.getMessage());
        }

        return accounts;
    }

    // ==========================================
    // GET ACCOUNT COUNT
    // ==========================================

    public static int getAccountCount() {

        String sql = "SELECT COUNT(*) FROM accounts";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                return result.getInt(1);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Failed to count accounts: " + e.getMessage());
        }

        return 0;
    }
}
