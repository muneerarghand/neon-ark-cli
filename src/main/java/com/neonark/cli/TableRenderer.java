package com.neonark.cli;

import org.json.JSONArray;
import org.json.JSONObject;

public class TableRenderer {

    public void renderCreatures(String jsonBody) {
        JSONArray arr = new JSONArray(jsonBody);

        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("ID    Name                 Species              Habitat                   Status");
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject c = arr.getJSONObject(i);

            System.out.printf("%-5d %-20s %-20s %-25s %-10s\n",
                    c.getInt("id"),
                    c.getString("name"),
                    c.optString("species", "(no species)"),
                    c.getString("habitatName"),
                    c.getString("status")
            );
        }

        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    public void renderSingleCreature(String jsonBody) {
        JSONObject c = new JSONObject(jsonBody);

        System.out.println("ID          : " + c.getInt("id"));
        System.out.println("Name        : " + c.getString("name"));
        System.out.println("Species     : " + c.optString("species", "(no species)"));
        System.out.println("Habitat     : " + c.getString("habitatName"));
        System.out.println("Status      : " + c.getString("status"));
    }

    public void renderObservations(String jsonBody) {
        JSONObject json = new JSONObject(jsonBody);

        System.out.println("Creature : " + json.getString("name"));
        System.out.println("Habitat  : " + json.getString("habitatName"));
        System.out.println("\n--- Observations ---");

        JSONArray arr = json.getJSONArray("observations");

        if (arr.isEmpty()) {
            System.out.println("(No observations yet)");
            return;
        }

        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            System.out.println("- " + o.getString("note") + " (" + o.getString("author") + ")");
        }
    }

    public void renderUsers(String jsonBody) {
        JSONArray arr = new JSONArray(jsonBody);

        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("ID    Full Name                 Email                          Role");
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject u = arr.getJSONObject(i);

            System.out.printf("%-5d %-25s %-30s %-10s\n",
                    u.getInt("id"),
                    u.getString("fullName"),
                    u.getString("email"),
                    u.getString("role")
            );
        }

        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    // ⭐ NEW — Option 7 table
    public void renderFeedingResults(JSONArray arr) {
        System.out.println("-----------------------------------------------------------------------------------------------");
        System.out.println("ID    Name                 Habitat");
        System.out.println("-----------------------------------------------------------------------------------------------");

        for (int i = 0; i < arr.length(); i++) {
            JSONObject c = arr.getJSONObject(i);

            System.out.printf("%-5d %-20s %-25s\n",
                    c.getInt("id"),
                    c.getString("name"),
                    c.getString("habitatName")
            );
        }

        System.out.println("-----------------------------------------------------------------------------------------------");
    }
}
