package com.neonark.cli;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Scanner;

public class MenuHandler {
    private final ApiClient apiClient = new ApiClient("http://localhost:8080");
    private final Scanner scanner = new Scanner(System.in);
    private final TableRenderer tableRenderer = new TableRenderer();

    public void start() {
        System.out.println("=== Neon Ark CLI System ===");
        while (true) {
            printMenu();
            int choice = getIntInput("Select an option: ");

            switch (choice) {
                case 1 -> listAllCreatures();
                case 2 -> viewCreatureById();
                case 3 -> registerNewCreature();
                case 4 -> renameCreature();
                case 5 -> removeCreature();
                case 6 -> viewCreatureObservations();
                case 7 -> findCreaturesByFeedingTime();   // ⭐ NEW OPTION
                case 8 -> viewAllUsers();
                case 0 -> {
                    if (confirmAction("exit the application")) {
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    }
                }
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=====================================");
        System.out.println("       NEON ARK CLI SYSTEM");
        System.out.println("=====================================");
        System.out.println("1. List all creatures");
        System.out.println("2. View creature by ID");
        System.out.println("3. Register new creature");
        System.out.println("4. Rename creature");
        System.out.println("5. Remove creature");
        System.out.println("6. View creature observations/notes");
        System.out.println("7. Find creatures by feeding time");   //
        System.out.println("--- Admin Only ---");
        System.out.println("8. View all system users");
        System.out.println("0. Exit");
        System.out.println("-------------------------------------");
    }

    private void listAllCreatures() {
        ApiResponse response = apiClient.listAllCreatures();
        printResult("Creatures", response, () -> tableRenderer.renderCreatures(response.getBody()));
    }

    private void viewCreatureById() {
        int id = getIntInput("Enter Creature ID: ");
        ApiResponse response = apiClient.getCreatureById(id);
        printResult("Creature Details", response, () -> tableRenderer.renderSingleCreature(response.getBody()));
    }

    private void registerNewCreature() {
        String name = getStringInput("Name: ");
        String species = getStringInput("Species: ");

        if (name.isBlank() || species.isBlank()) {
            System.out.println("❌ Error: Name and Species cannot be empty.");
            return;
        }

        ApiResponse response = apiClient.registerCreature(name, species);
        printResult("Registration Result", response, () -> {
            try {
                JSONObject json = new JSONObject(response.getBody());
                System.out.println("✅ Creature created successfully! ID: " + json.optInt("id"));
            } catch (Exception e) {
                System.out.println(response.getBody());
            }
        });
    }

    private void renameCreature() {
        int id = getIntInput("Enter Creature ID: ");
        String newName = getStringInput("New Name: ");

        if (!confirmAction("rename creature " + id + " to '" + newName + "'")) return;

        ApiResponse response = apiClient.renameCreature(id, newName);
        printResult("Rename Result", response, () -> System.out.println(response.getBody()));
    }

    private void removeCreature() {
        int id = getIntInput("Enter Creature ID: ");
        if (!confirmAction("remove creature " + id)) return;

        ApiResponse response = apiClient.removeCreature(id);
        printResult("Remove Result", response, () -> System.out.println(response.getBody()));
    }

    private void viewCreatureObservations() {
        int id = getIntInput("Enter Creature ID: ");
        ApiResponse response = apiClient.getCreatureWithObservations(id);
        printResult("Creature Observations", response, () -> tableRenderer.renderObservations(response.getBody()));
    }

    private void viewAllUsers() {
        ApiResponse response = apiClient.getAllUsers();
        printResult("System Users", response, () -> tableRenderer.renderUsers(response.getBody()));
    }

    // ⭐ NEW OPTION 7 HANDLER
    private void findCreaturesByFeedingTime() {
        String time = getStringInput("Enter feeding time (HH:MM): ");

        if (!time.matches("^\\d{2}:\\d{2}$")) {
            System.out.println("❌ Invalid time format. Use HH:MM.");
            return;
        }

        ApiResponse response = apiClient.getCreaturesByFeedingTime(time);

        printResult("Creatures to Feed at " + time, response, () -> {
            try {
                JSONObject json = new JSONObject(response.getBody());

                if (json.has("creatures")) {
                    JSONArray arr = json.getJSONArray("creatures");

                    if (arr.isEmpty()) {
                        System.out.println("(No creatures require feeding at this time)");
                        return;
                    }

                    tableRenderer.renderFeedingResults(arr);
                }

            } catch (Exception e) {
                System.out.println("❌ Error parsing response");
            }
        });
    }

    private void printResult(String title, ApiResponse response, Runnable successAction) {
        System.out.println("\nStatus: " + response.getStatusCode());

        if (response.isSuccess()) {
            System.out.println("=== " + title + " ===");
            successAction.run();

            try {
                JSONObject json = new JSONObject(response.getBody());
                String message = json.optString("message", "");
                if (!message.isBlank()) {
                    System.out.println("\n✅ " + message);
                }
            } catch (Exception ignored) {}

        } else {
            System.out.println("❌ Operation Failed");
            try {
                JSONObject error = new JSONObject(response.getBody());
                System.out.println("→ " + error.optString("error", response.getBody()));
            } catch (Exception e) {
                System.out.println(response.getBody());
            }
        }
    }

    private boolean confirmAction(String action) {
        System.out.print("Are you sure you want to " + action + "? (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
}
