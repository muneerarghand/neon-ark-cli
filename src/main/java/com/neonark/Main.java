package com.neonark;

import com.neonark.menu.AdminMenu;

/**
 * Entry point for the Neon Ark CLI application.
 * Starts the AdminMenu and launches the console loop.
 */
public class Main {
    public static void main(String[] args) {
        // Launch the main admin console
        new AdminMenu().start();
    }
}
