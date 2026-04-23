package com.neonark.menu;

import com.neonark.model.Warden;
import com.neonark.service.WardenService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * AdminMenu
 * ----------
 * This class represents the main console interface for the Neon Ark
 * Boundary Console Verification Initiative.
 *
 * It simulates how a real front-end client would behave when interacting
 * with backend services. No real database or backend exists — all actions
 * are simulated as per the project instructions.
 */
public class AdminMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final WardenService wardenService = new WardenService();

    /**
     * Starts the main menu loop.
     * Continues running until the user selects option 6 (Exit).
     */
    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addNewWarden();
                case "2" -> viewWardens();
                case "3" -> updateWarden();
                case "4" -> manageCertifications();
                case "5" -> deactivateWarden();
                case "6" -> {
                    System.out.println("\nExiting Neon Ark Admin Console. Goodbye.");
                    running = false;
                }
                default -> System.out.println("\nInvalid option. Please choose 1-6.\n");
            }
        }
        scanner.close();
    }

    /**
     * Prints the main menu exactly as specified in the Neon Ark project.
     */
    private void printMainMenu() {
        System.out.println("=========================================================");
        System.out.println("        NEON ARK — ADMIN WARDEN ONBOARDING CONSOLE");
        System.out.println("=========================================================\n");
        System.out.println("[ MAIN MENU ]");
        System.out.println("---------------------------------------------------------");
        System.out.println("1. Add New Warden");
        System.out.println("2. View Wardens");
        System.out.println("3. Update Warden");
        System.out.println("4. Manage Certifications");
        System.out.println("5. Deactivate / Terminate Warden");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * FULLY IMPLEMENTED: Add New Warden
     * Prompts for all required fields with proper validation,
     * then prints a realistic simulated POST request with JSON payload.
     */
    private void addNewWarden() {
        System.out.println("\n[ Add New Warden ]");

        String firstName = promptRequired("First Name");
        String lastName = promptRequired("Last Name");

        String role = promptValidRole();
        String status = promptValidStatus();
        String hireDate = promptValidDate();

        // Create and add to in-memory list (for this session only)
        int newId = wardenService.generateNextId();
        Warden newWarden = new Warden(newId, firstName, lastName, status, role, hireDate);
        wardenService.addWarden(newWarden);

        // Simulated outbound request (as required by the project)
        System.out.println("\n--------------------------------");
        System.out.println("BRIEF DESCRIPTION: Create a new Warden record in the system.");
        System.out.println("WOULD SEND: POST /api/wardens\n");

        System.out.println("PAYLOAD:");
        System.out.println("{");
        System.out.println("  \"firstName\": \"" + firstName + "\",");
        System.out.println("  \"lastName\": \"" + lastName + "\",");
        System.out.println("  \"status\": \"" + status + "\",");
        System.out.println("  \"role\": \"" + role + "\",");
        System.out.println("  \"hireDate\": \"" + hireDate + "\"");
        System.out.println("}\n");

        System.out.println("RESULT: SUCCESS (simulated)");
        System.out.println("Assigned ID: " + newId);
        System.out.println("--------------------------------\n");
    }

    private String promptRequired(String label) {
        while (true) {
            System.out.print(label + ": ");
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println(label + " is required and cannot be blank.");
        }
    }

    private String promptValidRole() {
        while (true) {
            System.out.print("Role (Xenobiologist, Caretaker, Security, Field): ");
            String role = scanner.nextLine().trim();
            if (wardenService.isValidRole(role)) return role;
            System.out.println("Invalid role. Please choose one of the listed roles.");
        }
    }

    private String promptValidStatus() {
        while (true) {
            System.out.print("Status (Active, Inactive, Retired, Deceased): ");
            String status = scanner.nextLine().trim();
            if (wardenService.isValidStatus(status)) return status;
            System.out.println("Invalid status. Please choose one of the listed statuses.");
        }
    }

    private String promptValidDate() {
        while (true) {
            System.out.print("Hire Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            if (wardenService.isValidDate(date)) return date;
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }

    /**
     * FULLY IMPLEMENTED: View Wardens
     * Displays all wardens (from CSV + in-memory additions) in a clean table.
     */
    private void viewWardens() {
        System.out.println("\n[ View Wardens ]\n");

        List<Warden> wardens = wardenService.getAllWardens();

        if (wardens.isEmpty()) {
            System.out.println("No wardens found.\n");
            return;
        }

        System.out.println("ID | First Name | Last Name | Status | Role | Hire Date");
        System.out.println("-------------------------------------------------------------");

        for (Warden w : wardens) {
            System.out.printf("%-2d | %-11s | %-10s | %-8s | %-9s | %s%n",
                    w.getId(), w.getFirstName(), w.getLastName(),
                    w.getStatus(), w.getRole(), w.getHireDate());
        }
        System.out.println();
    }

    /**
     * Simulated: Update Warden status
     */
    private void updateWarden() {
        System.out.println("\n[ Update Warden ]");

        int id = promptWardenId();
        if (id == -1) return;

        Optional<Warden> wardenOpt = wardenService.findById(id);
        if (wardenOpt.isEmpty()) {
            System.out.println("No warden found with ID " + id + ".\n");
            return;
        }

        String newStatus = promptValidStatus();

        wardenService.updateStatus(id, newStatus);

        System.out.println("\n--------------------------------");
        System.out.println("BRIEF DESCRIPTION: Update employment status for an existing Warden.");
        System.out.println("WOULD SEND: PUT /api/wardens/" + id);
        System.out.println("RESULT: SUCCESS (simulated)");
        System.out.println("--------------------------------\n");
    }

    /**
     * Simulated: Manage Certifications
     */
    private void manageCertifications() {
        System.out.println("\n[ Manage Certifications ]");

        int id = promptWardenId();
        if (id == -1) return;

        if (wardenService.findById(id).isEmpty()) {
            System.out.println("No warden found with ID " + id + ".\n");
            return;
        }

        System.out.println("BRIEF DESCRIPTION: Create and attach a certification record to a Warden.");
        System.out.println("WOULD SEND: POST /api/wardens/" + id + "/certifications\n");

        System.out.println("PAYLOAD:");
        System.out.println("{");
        System.out.println("  \"name\": \"Rift Safety Level 1\",");
        System.out.println("  \"earnedDate\": \"2026-02-03\"");
        System.out.println("}\n");

        System.out.println("RESULT: SUCCESS (simulated)\n");
    }

    /**
     * Simulated: Deactivate / Terminate Warden
     */
    private void deactivateWarden() {
        System.out.println("\n[ Deactivate / Terminate Warden ]");

        int id = promptWardenId();
        if (id == -1) return;

        if (wardenService.findById(id).isEmpty()) {
            System.out.println("No warden found with ID " + id + ".\n");
            return;
        }

        wardenService.deactivate(id);

        System.out.println("\n--------------------------------");
        System.out.println("BRIEF DESCRIPTION: Soft delete a Warden by marking status as Deceased.");
        System.out.println("WOULD SEND: DELETE /api/wardens/" + id);
        System.out.println("RESULT: SUCCESS (simulated)");
        System.out.println("--------------------------------\n");
    }

    /**
     * Helper: Prompts for Warden ID and validates it is a number.
     */
    private int promptWardenId() {
        System.out.print("Enter Warden ID: ");
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("ID must be a valid number.\n");
            return -1;
        }
    }
}