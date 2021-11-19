package ua.lyubchenko.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import ua.lyubchenko.domains.ApiResponse;
import ua.lyubchenko.domains.Order;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OrderController {
    private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @SneakyThrows
    public String getStoreInventory(String url,String param) {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return send.body();
    }
    @SneakyThrows
    public Order getOrderByPetsId(String url, String param) {
        String newUrl = url + "/order/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Order.class);
    }
    @SneakyThrows
    public Order placeAnOrderForAPet(String url, Order order) {
        String newUrl = url + "/order";
        String requestBody = GSON.toJson(order);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Order.class);
    }
    @SneakyThrows
    public ApiResponse deleteOrderById(String url, String param) {
        String newUrl = url + "/order/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .DELETE()
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(),ApiResponse.class);

    }
}
