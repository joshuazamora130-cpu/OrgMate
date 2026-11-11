
package src.authentication;

import src.model.Account;
import src.services.AccountStorage;

public class Auth {

    // Officer authorization code
    private static final String OFFICER_REGISTRATION_CODE =
            "ORGMATE-ADMIN-2026";

    // ==========================================
    // LOGIN AUTHENTICATION
    // ==========================================

    public static Account authenticate(String srCode, String password) {

        Account account = AccountStorage.findAccount(srCode);

        if (account == null) {
            return null;
        }

        if (!account.getPassword().equals(password)) {
            return null;
        }

        return account;
    }

    // ==========================================
    // PASSWORD VALIDATION
    // ==========================================

    public static boolean isValidPassword(String password) {

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

    // ==========================================
    // SR CODE VALIDATION
    // ==========================================

    public static boolean isValidSRCode(String srCode) {
        return srCode != null && srCode.matches("\\d{2}-\\d{5}");
    }

    // ==========================================
    // OFFICER AUTHORIZATION
    // ==========================================

    public static boolean authorizeOfficer(String code) {

        return code != null
                && code.equals(OFFICER_REGISTRATION_CODE);
    }
}
