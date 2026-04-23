package com.neonark.service;

import com.neonark.model.Warden;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Service layer for managing Warden records.
 * Handles seed loading, validation, and in‑memory updates.
 */
public class WardenService {

    // In‑memory list of all wardens (CSV + new additions)
    private final List<Warden> wardens = new ArrayList<>();

    // Tracks used IDs to prevent duplicates
    private final Set<Integer> usedIds = new HashSet<>();

    // Allowed controlled values
    private static final List<String> VALID_ROLES = List.of(
            "Xenobiologist", "Caretaker", "Security", "Field"
    );

    private static final List<String> VALID_STATUS = List.of(
            "Active", "Inactive", "Retired", "Deceased"
    );

    /**
     * Loads seed data from wardens.csv on startup.
     */
    public WardenService() {
        loadSeedData();
    }

    /**
     * Reads the CSV file and loads initial Warden records.
     * CSV is treated as read‑only.
     */
    private void loadSeedData() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("wardens.csv");
            if (is == null) {
                System.out.println("Seed data file not found: wardens.csv");
                return;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                boolean header = true;

                while ((line = br.readLine()) != null) {
                    if (header) { // Skip header row
                        header = false;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length < 6) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    String status = parts[3].trim();
                    String role = parts[4].trim();
                    String hireDate = parts[5].trim();

                    Warden w = new Warden(id, firstName, lastName, status, role, hireDate);
                    wardens.add(w);
                    usedIds.add(id);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading seed data: " + e.getMessage());
        }
    }

    /**
     * Returns an unmodifiable list of all wardens.
     */
    public List<Warden> getAllWardens() {
        return Collections.unmodifiableList(wardens);
    }

    /**
     * Checks if an ID is already used.
     */
    public boolean idExists(int id) {
        return usedIds.contains(id);
    }

    /**
     * Generates the next available ID.
     */
    public int generateNextId() {
        int max = usedIds.stream().mapToInt(i -> i).max().orElse(0);
        int next = max + 1;
        usedIds.add(next);
        return next;
    }

    /**
     * Validates role against allowed values.
     */
    public boolean isValidRole(String role) {
        return VALID_ROLES.contains(role);
    }

    /**
     * Validates status against allowed values.
     */
    public boolean isValidStatus(String status) {
        return VALID_STATUS.contains(status);
    }

    /**
     * Validates date format (YYYY‑MM‑DD).
     */
    public boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    /**
     * Adds a new Warden to memory (CSV is not modified).
     */
    public void addWarden(Warden warden) {
        wardens.add(warden);
    }

    /**
     * Finds a Warden by ID.
     */
    public Optional<Warden> findById(int id) {
        return wardens.stream().filter(w -> w.getId() == id).findFirst();
    }

    /**
     * Updates a Warden's status.
     */
    public void updateStatus(int id, String newStatus) {
        findById(id).ifPresent(w -> w.setStatus(newStatus));
    }

    /**
     * Marks a Warden as Deceased (soft delete).
     */
    public void deactivate(int id) {
        findById(id).ifPresent(w -> w.setStatus("Deceased"));
    }
}
