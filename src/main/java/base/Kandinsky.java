package base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class Kandinsky {
    private final String url;
    private final String apiKey;
    private final String secretKey;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public Kandinsky(String url, String apiKey, String secretKey) {
        this.url = url;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String get_model() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "key/api/v1/models"))
                .timeout(Duration.ofMinutes(1))
                .header("X-Key", "Key " + apiKey)
                .header("X-Secret", "Secret " + secretKey)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode models = mapper.readTree(response.body());
        return models.get(0).get("id").asText();
    }

    public String generate(String prompt, String modelId) throws Exception {
        JsonNode params = mapper.createObjectNode()
                .put("type", "GENERATE")
                .put("numImages", 1)
                .put("width", 1024)
                .put("height", 1024)
                .set("generateParams", mapper.createObjectNode().put("query", prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "key/api/v1/text2image/run"))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .header("X-Key", "Key " + apiKey)
                .header("X-Secret", "Secret " + secretKey)
                .POST(HttpRequest.BodyPublishers.ofString(params.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseData = mapper.readTree(response.body());
        return responseData.get("uuid").asText();
    }

    public JsonNode check_generation(String requestId) throws Exception {
        URI uri = URI.create(url + "key/api/v1/text2image/status/" + requestId);
        for (int attempts = 0; attempts < 10; attempts++) {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofMinutes(1))
                    .header("X-Key", "Key " + apiKey)
                    .header("X-Secret", "Secret " + secretKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode data = mapper.readTree(response.body());

            if ("DONE".equals(data.get("status").asText())) {
                return data.get("images");
            }
            Thread.sleep(10000); // 10 second delay
        }
        return null; // Or throw an exception
    }

    public Path downloadImage(String imageUrl, String destinationPath) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .GET()
                .build();

        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Path.of(destinationPath)));
        return response.body(); // Возвращает путь к сохраненному изображению
    }
}