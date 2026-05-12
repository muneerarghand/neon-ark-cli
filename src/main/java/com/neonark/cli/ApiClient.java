package com.neonark.cli;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private ApiResponse sendGet(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());

        } catch (Exception e) {
            return new ApiResponse(500, "Error: " + e.getMessage());
        }
    }

    private ApiResponse sendPost(String endpoint, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());

        } catch (Exception e) {
            return new ApiResponse(500, "Error: " + e.getMessage());
        }
    }

    private ApiResponse sendPut(String endpoint, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());

        } catch (Exception e) {
            return new ApiResponse(500, "Error: " + e.getMessage());
        }
    }

    private ApiResponse sendDelete(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + endpoint))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ApiResponse(response.statusCode(), response.body());

        } catch (Exception e) {
            return new ApiResponse(500, "Error: " + e.getMessage());
        }
    }

    // ============================
    // API ROUTES
    // ============================

    public ApiResponse listAllCreatures() {
        return sendGet("/api/creatures");
    }

    public ApiResponse getCreatureById(int id) {
        return sendGet("/api/creatures/" + id);
    }

    public ApiResponse registerCreature(String name, String species) {
        String json = String.format("{\"name\":\"%s\", \"species\":\"%s\"}", name, species);
        return sendPost("/api/creatures", json);
    }

    public ApiResponse renameCreature(int id, String newName) {
        String json = String.format("{\"newName\":\"%s\"}", newName);
        return sendPut("/api/creatures/" + id + "/name", json);
    }

    public ApiResponse removeCreature(int id) {
        return sendDelete("/api/creatures/" + id);
    }

    public ApiResponse getCreatureWithObservations(int id) {
        return sendGet("/api/creatures/" + id + "/observations");
    }

    public ApiResponse getAllUsers() {
        return sendGet("/api/admin/users");
    }

    // ⭐ NEW — Option 7
    public ApiResponse getCreaturesByFeedingTime(String time) {
        return sendGet("/api/feedings?time=" + time);
    }
}
