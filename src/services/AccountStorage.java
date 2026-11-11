
package src.services;

import java.util.ArrayList;
import src.model.Account;

public class AccountStorage {

    // Stores all registered accounts
    private static final ArrayList<Account> accounts = new ArrayList<>();

    // Add a new account
    public static void addAccount(Account account) {
        accounts.add(account);
    }

    // Find an account using SR Code
    public static Account findAccount(String srCode) {

        for (Account account : accounts) {

            if (account.getStudent().getStudentID().equals(srCode)) {
                return account;
            }
        }

        return null;
    }

    // Check if an account already exists
    public static boolean accountExists(String srCode) {

        return findAccount(srCode) != null;
    }

    // Get all registered accounts
    public static ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    // Get total number of registered accounts
    public static int getAccountCount() {
        return accounts.size();
    }
}
