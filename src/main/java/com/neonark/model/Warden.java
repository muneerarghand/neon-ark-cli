package com.neonark.model;

/**
 * Represents a single Warden record used by the CLI.
 * Mirrors the CSV seed data and database design.
 */
public class Warden {

    // Unique identifier (from CSV or generated in memory)
    private int id;

    // Basic identity fields
    private String firstName;
    private String lastName;

    // Controlled values (must match allowed sets)
    private String status;   // Active, Inactive, Retired, Deceased
    private String role;     // Xenobiologist, Caretaker, Security, Field

    // Required date field (YYYY-MM-DD)
    private String hireDate;

    /**
     * Creates a new Warden object.
     */
    public Warden(int id, String firstName, String lastName,
                  String status, String role, String hireDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.role = role;
        this.hireDate = hireDate;
    }

    // Getters (read-only access)
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }

    public String getHireDate() {
        return hireDate;
    }

    /**
     * Only status can change (for update/deactivate actions).
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
