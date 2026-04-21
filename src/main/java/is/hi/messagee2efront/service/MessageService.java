package is.hi.messagee2efront.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.messagee2efront.model.request.SendMessageRequest;
import is.hi.messagee2efront.model.response.ConversationSummaryResponse;
import is.hi.messagee2efront.model.response.MessageResponse;
import is.hi.messagee2efront.model.response.PublicKeyResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class MessageService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ConversationSummaryResponse> getConversationSummaries() throws IOException, InterruptedException{
        String token = TokenStorage.getToken();

        if(token == null || token.isBlank()){
            throw new RuntimeException("No JWT found. User not logged in.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/message/conversations"))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("User-Agent", "JavaFX-MessageE2E-Client")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if(response.statusCode() == 200){
            return objectMapper.readValue(response.body(), new TypeReference<List<ConversationSummaryResponse>>(){});
        } else{
            throw new RuntimeException("Fetching conversation summaries failed. Status code: " + response.statusCode());
        }
    }

    public List<MessageResponse> getConversation(String username) throws IOException, InterruptedException{
        String token = TokenStorage.getToken();

        if(token == null || token.isBlank()) {
            throw new RuntimeException("No JWT token found. User is not logged in.");
        }
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/message/conversation/" + encodedUsername))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("User-Agent", "JavaFX-MessageE2E-Client")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if(response.statusCode() == 200){
            return objectMapper.readValue(response.body(), new TypeReference<List<MessageResponse>>(){});
        } else{
            throw new RuntimeException("Fetching conversation failed. Status code: " + response.statusCode());
        }
    }

    public void sendMessage(SendMessageRequest requestBodyObject) throws IOException, InterruptedException{
        String token = TokenStorage.getToken();

        if (token == null || token.isBlank()) {
            throw new RuntimeException("No JWT token found. User is not logged in.");
        }


        String requestBody = objectMapper.writeValueAsString(requestBodyObject);
        System.out.println("JWT token: " + token);
        System.out.println("Send message URL: " + ApiClient.getBaseUrl() + "/message/send");
        System.out.println("Request body: " + requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/message/send"))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("User-Agent", "JavaFX-MessageE2E-Client")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        System.out.println("Send message response status: " + response.statusCode());
        System.out.println("Send message response body: " + response.body());

        if(response.statusCode() != 200){
            throw new RuntimeException("Sending message failed. Status code: " + response.statusCode());
        }
    }

    public PublicKeyResponse getPublicKey(String username) throws IOException, InterruptedException{
        String token = TokenStorage.getToken();
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);

        HttpRequest request =HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/user/public-key/" + encodedUsername))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("User-Agent", "JavaFX-MessageE2E-Client")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if(response.statusCode() == 200){
            return objectMapper.readValue(response.body(), PublicKeyResponse.class);
        } else {
            throw new RuntimeException("Fetching public key failed. Status code: " + response.statusCode());
        }

    }
}
