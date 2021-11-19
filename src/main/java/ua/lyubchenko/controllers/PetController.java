package ua.lyubchenko.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import ua.lyubchenko.domains.ApiResponse;
import ua.lyubchenko.domains.Pet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetController {
    private final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @SneakyThrows
    public Pet getPetById(String url, String param) {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Pet.class);
    }

    @SneakyThrows
    public List<Pet> getPetsByStatus(String url, String param) {
        if (!param.equals("available") && !param.equals("pending") && !param.equals("sold")) {
            System.out.printf("Питомец под статусом %s не найден. Введите правильный статус.",param);
        }
            String newUrl = url + "/findByStatus?status=" + param;
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(newUrl))
                    .build();
            HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return GSON.fromJson(send.body(), new TypeToken<List<Pet>>() {
            }.getType());
        }

    @SneakyThrows
    public ApiResponse deletePetById(String url, String param) {
        String newUrl = url + "/" + param;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .DELETE()
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(),ApiResponse.class);

    }

    @SneakyThrows
    public Pet updatePet(String url, Pet pet) {
        String requestBody = GSON.toJson(pet);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        return GSON.fromJson(send.body(), Pet.class);

    }

    @SneakyThrows
    public Pet createPet(String url, Pet pet) {
        String requestBody = GSON.toJson(pet);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .headers("Content-Type", "application/json")
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), Pet.class);

    }

    @SneakyThrows
    public ApiResponse updatePetsStatus(String url, Pet pet) {
        String newUrl = url + "/" + pet.getId();
        String requestBody = GSON.toJson(pet);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);

    }

    @SneakyThrows
    public ApiResponse postUploadImage(String url,String param) {
        String[] words = param.split(" ");
        String filePath = param.replaceFirst(words[0], "").trim().replace(words[1], "").trim();
        String boundary = "-------------oiawn4tp89n4e9p5";
        Map<Object, Object> data = new HashMap<>();

        data.put("additionalMetadata",words[1]);

        data.put("file", filePath);
        String newUrl = url + "/" + words[0] + "/uploadImage";
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(newUrl))
                .headers("accept", "application/json","Content-Type",
        "multipart/form-data;boundary=" + boundary)
                .POST(oMultipartData(data, boundary))
                .build();
        HttpResponse<String> send = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(send.body(), ApiResponse.class);

    }
    private  HttpRequest.BodyPublisher oMultipartData(Map<Object, Object> data,
                                                           String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary
                + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path) {
                var path = (Path) entry.getValue();
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\""
                        + path.getFileName() + "\"\r\nContent-Type: " + mimeType
                        + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
                byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
            } else {
                byteArrays.add(
                        ("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue()
                                + "\r\n").getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays
                .add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}
