
package src.services;

import java.util.ArrayList;

import src.model.FacultyAccount;

public class FacultyAccountStorage {

    private static final ArrayList<FacultyAccount> accounts =
            new ArrayList<>();

    // Register a faculty account
    public static boolean addAccount(FacultyAccount account) {

        if (account == null) {
            return false;
        }

        if (findByEmail(account.getInstitutionalEmail()) != null) {
            return false;
        }

        accounts.add(account);
        return true;
    }

    // Find faculty account using institutional email
    public static FacultyAccount findByEmail(String email) {

        if (email == null) {
            return null;
        }

        for (FacultyAccount account : accounts) {

            if (account.getInstitutionalEmail()
                    .equalsIgnoreCase(email.trim())) {

                return account;
            }
        }

        return null;
    }

    // Authenticate faculty account
    public static FacultyAccount authenticate(
            String email, String password) {

        FacultyAccount account = findByEmail(email);

        if (account != null &&
                account.getPassword().equals(password)) {

            return account;
        }

        return null;
    }

    // Retrieve all faculty accounts
    public static ArrayList<FacultyAccount> getAllAccounts() {

        return new ArrayList<>(accounts);
    }

    // Check total faculty accounts
    public static int getTotalAccounts() {

        return accounts.size();
    }
}
