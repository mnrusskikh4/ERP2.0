package base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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

    public String generate(String prompt, String model) throws Exception {
        ObjectNode params = mapper.createObjectNode();
        params.put("type", "Детальное фото");
        params.put("numImages", 1);
        params.put("width", 1024);
        params.put("height", 1024);
        params.set("generateParams", mapper.createObjectNode().put("query", prompt));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "key/api/v1/text2image/run"))
                .timeout(Duration.ofMinutes(1))
                .header("X-Key", "Key " + apiKey)
                .header("X-Secret", "Secret " + secretKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(params.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode responseData = mapper.readTree(response.body());
        return responseData.get("uuid").asText();
    }

    public JsonNode check_generation(String request_id) throws Exception {
        for (int attempts = 0; attempts < 10; attempts++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "key/api/v1/text2image/status/" + request_id))
                    .timeout(Duration.ofMinutes(1))
                    .header("X-Key", "Key " + apiKey)
                    .header("X-Secret", "Secret " + secretKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode data = mapper.readTree(response.body());

            if (data.get("status").asText().equals("DONE")) {
                return data.get("images");
            }

            Thread.sleep(10000); // 10 second delay
        }

        return null; // Or throw an exception
    }

    public static void main(String[] args) throws Exception {
        Kandinsky api = new Kandinsky("https://api-key.fusionbrain.ai/", "B0ACDF1FD75E32D32D471EA21260FAFC", "1A9B248B3EA51441B825F390840599D8");
        String model_id = api.get_model();
        String uuid = api.generate("Sun in sky", model_id);
        JsonNode images = api.check_generation(uuid);
        System.out.println(images);
    }

    public Path downloadImage(String imageUrl, String destinationPath) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest requestForImage = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .build();

        HttpResponse<InputStream> response = httpClient.send(requestForImage, HttpResponse.BodyHandlers.ofInputStream());

        Path imagePath = Path.of(destinationPath);
        Files.copy(response.body(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return imagePath; // Возвращает путь к сохраненному изображению
    }
}
