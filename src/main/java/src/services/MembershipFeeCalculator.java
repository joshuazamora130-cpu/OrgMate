
package src.services;

import java.util.ArrayList;

public class MembershipFeeCalculator {

    public static final double CICS_FEE = 60.00;
    public static final double PROGRAM_ORG_FEE = 60.00;
    public static final double JPCS_FEE = 100.00;

    public static ArrayList<String> getOrganizations(String program) {

        ArrayList<String> organizations = new ArrayList<>();
        String normalized = program.trim().toLowerCase();

        organizations.add("CICS-SC");

        if (normalized.contains("computer science")
                || normalized.equals("cs")
                || normalized.equals("bscs")) {

            organizations.add("ACCESS");

        } else if (normalized.contains("information technology")
                || normalized.equals("it")
                || normalized.equals("bsit")) {

            organizations.add("IINTESS");

        } else {
            throw new IllegalArgumentException(
                "Unsupported CICS program: " + program
            );
        }

        return organizations;
    }

    public static double calculateTotal(
            String program, boolean includeJPCS) {

        double total = CICS_FEE + PROGRAM_ORG_FEE;

        if (includeJPCS) {
            total += JPCS_FEE;
        }

        return total;
    }

    public static void displayFeeBreakdown(
            String program, boolean includeJPCS) {

        ArrayList<String> organizations = getOrganizations(program);

        System.out.println("\n========== MEMBERSHIP FEE BREAKDOWN ==========");

        for (String organization : organizations) {
            System.out.printf("%-15s PHP 60.00%n", organization);
        }

        if (includeJPCS) {
            System.out.printf("%-15s PHP 100.00%n", "JPCS");
        }

        System.out.println("----------------------------------------------");
        System.out.printf("TOTAL:          PHP %.2f%n",
                calculateTotal(program, includeJPCS));
        System.out.println("==============================================");
    }
}
