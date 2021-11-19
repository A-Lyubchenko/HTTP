package ua.lyubchenko.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import ua.lyubchenko.domains.ApiResponse;
import ua.lyubchenko.domains.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class UserController {
    private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SneakyThrows
    public User getByUserName(String url, String param) {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), User.class);
    }
    @SneakyThrows
    public ApiResponse logsUser(String url, String param) {
        String[] words = param.split(" ");
        String newUrl = url + "/login?username=" + words[0] + "&password=" + words[1];
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }
    @SneakyThrows
    public ApiResponse getLogOut(String url) {
        String newUrl = url + "/logout";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }
    @SneakyThrows
    public ApiResponse createUserWithList(String url, List<User> user) {
        String newUrl = url + "/createWithList";
        String requestBody = GSON.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }
    @SneakyThrows
    public ApiResponse createUser(String url, User user) {
        String requestBody = GSON.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);
    }
    @SneakyThrows
    public ApiResponse updateUser(String url, User user) {
        String newUrl = url + "/" + user.getUsername();
        String requestBody = GSON.toJson(user);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return GSON.fromJson(send.body(), ApiResponse.class);

    }
    @SneakyThrows
    public ApiResponse deleteUser(String url, String param) {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .DELETE()
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(),ApiResponse.class);

    }
}
