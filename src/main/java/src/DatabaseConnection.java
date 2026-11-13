package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL =
            "jdbc:postgresql://" +
            dotenv.get("DB_HOST") + ":" +
            dotenv.get("DB_PORT") + "/" +
            dotenv.get("DB_NAME") +
            "?sslmode=require";

    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Temporary connection test
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Successfully connected to Supabase!");
            System.out.println("Database: " +
                    connection.getCatalog());
            System.out.println("Connection status: Connected");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}