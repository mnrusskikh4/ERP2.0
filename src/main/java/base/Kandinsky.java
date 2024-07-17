//package base;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.file.Path;
//import java.time.Duration;
//
//public class Kandinsky {
//    private final String url;
//    private final String apiKey;
//    private final String secretKey;
//    private final HttpClient client;
//    private final ObjectMapper mapper;
//
//    public Kandinsky(String url, String apiKey, String secretKey) {
//        this.url = url;
//        this.apiKey = apiKey;
//        this.secretKey = secretKey;
//        this.client = HttpClient.newHttpClient();
//        this.mapper = new ObjectMapper();
//        }
//
//    public String get_model() throws Exception {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url + "key/api/v1/models"))
//                .timeout(Duration.ofMinutes(1))
//                .header("X-Key", "Key " + apiKey)
//                .header("X-Secret", "Secret " + secretKey)
//                .GET()
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        JsonNode models = mapper.readTree(response.body());
//
//        if (models.isArray() && models.size() > 0) {
//            return models.get(0).get("id").asText();
//        } else {
//            throw new Exception("No models received from API, response was: " + response.body());
//        }
//    }
//
//    public String generate(String prompt, String modelId) throws Exception {
//        JsonNode params = mapper.createObjectNode()
//                .put("type", "GENERATE")
//                // .put("numImages", 1) // Removed: "numImages" is not present in documentation JSON examples.
//                .put("width", 1024)
//                .put("height", 1024)
//                .set("generateParams", mapper.createObjectNode().put("query", prompt));
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url + "key/api/v1/text2image/run"))
//                .timeout(Duration.ofMinutes(1))
//                .header("Content-Type", "application/json")
//                .header("X-Key", apiKey)
//                .header("X-Secret", secretKey)
//                .POST(HttpRequest.BodyPublishers.ofString(params.toString()))
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        // Проверка на корректность HTTP статуса
//        if (response.statusCode() == 200) {
//            JsonNode responseData = mapper.readTree(response.body());
//            if (responseData.hasNonNull("uuid")) {
//                return responseData.get("uuid").asText();
//            } else {
//                throw new Exception("Response JSON does not contain the 'uuid' field.");
//            }
//        } else {
//            // Обработка HTTP ошибок согласно API
//            throw new Exception("HTTP error occurred: " + response.statusCode());
//        }
//    }
//
//    public JsonNode check_generation(String requestId) throws Exception {
//        URI uri = URI.create(url + "key/api/v1/text2image/status/" + requestId);
//        for (int attempts = 0; attempts < 10; attempts++) {
//            HttpRequest request = HttpRequest.newBuilder(uri)
//                    .timeout(Duration.ofMinutes(1))
//                    .header("X-Key", apiKey)
//                    .header("X-Secret", secretKey)
//                    .GET()
//                    .build();
//
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            JsonNode data = mapper.readTree(response.body());
//
//            if ("DONE".equals(data.get("status").asText())) {
//                return data.get("images");
//            }
//            Thread.sleep(10000); // 10 second delay
//        }
//        return null; // Or throw an exception
//    }
//
//    public Path downloadImage(String imageUrl, String destinationPath) throws Exception {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(imageUrl))
//                .GET()
//                .build();
//
//        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Path.of(destinationPath)));
//        return response.body(); // Возвращает путь к сохраненному изображению
//    }
//}