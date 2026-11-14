
package src.authentication;

import org.mindrot.jbcrypt.BCrypt;

import src.model.Account;
import src.model.FacultyAccount;
import src.services.AccountStorage;
import src.services.FacultyAccountStorage;

public class Auth {

    private static final String OFFICER_REGISTRATION_CODE =
            "ORGMATE-ADMIN-2026";

    // STUDENT AND OFFICER LOGIN

    public static Account authenticate(
            String srCode,
            String password) {

        if (srCode == null || password == null) {
            return null;
        }

        Account account = AccountStorage.findAccount(srCode);

        if (account == null) {
            return null;
        }

        try {
            if (!BCrypt.checkpw(
                    password,
                    account.getPassword())) {
                return null;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid stored password hash.");
            return null;
        }

        return account;
    }

    // FACULTY LOGIN

    public static FacultyAccount authenticateFaculty(
            String email,
            String password) {

        if (email == null || password == null) {
            return null;
        }

        return FacultyAccountStorage.authenticate(
                email.trim(),
                password
        );
    }

    // PASSWORD VALIDATION

    public static boolean isValidPassword(String password) {

        if (password == null) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasUppercase = true;
            }

            if (Character.isLowerCase(character)) {
                hasLowercase = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }
        }

        return password.length() >= 8
                && hasUppercase
                && hasLowercase
                && hasNumber;
    }

    // SR CODE VALIDATION

    public static boolean isValidSRCode(String srCode) {

        return srCode != null
                && srCode.matches("\\d{2}-\\d{5}");
    }

    // OFFICER AUTHORIZATION

    public static boolean authorizeOfficer(String code) {

        return code != null
                && code.equals(OFFICER_REGISTRATION_CODE);
    }
}
