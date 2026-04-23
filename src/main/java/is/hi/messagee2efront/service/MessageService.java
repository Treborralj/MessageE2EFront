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
 * e-mail: ral9@hi.is
 * Description: Handles message related communication between the JavaFX client
 * and the backend.
 *
 *****************************************************************************/
public class MessageService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Retrieves all conversation summaries for the logged-in user.
     * @return a list of conversation summaries
     * @throws IOException if the request fails during communication
     * @throws InterruptedException if the request is interrupted
     */
    public List<ConversationSummaryResponse> getConversationSummaries() throws IOException, InterruptedException {
        String token = TokenStorage.getToken();

        if (token == null || token.isBlank()) {
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

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<ConversationSummaryResponse>>() {
            });
        } else {
            throw new RuntimeException("Fetching conversation summaries failed. Status code: " + response.statusCode());
        }
    }

    /**
     * Retrieves the full conversation with the user with the given username.
     * @param username the username of the other conversation participant
     * @return a list of messages in the conversation
     * @throws IOException if the request fails during communication
     * @throws InterruptedException if the request is interrupted
     */
    public List<MessageResponse> getConversation(String username) throws IOException, InterruptedException {
        String token = TokenStorage.getToken();

        if (token == null || token.isBlank()) {
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

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<MessageResponse>>() {
            });
        } else {
            throw new RuntimeException("Fetching conversation failed. Status code: " + response.statusCode());
        }
    }

    /**
     * Sends an encrypted message to the backend.
     * @param requestBodyObject the encrypted message request
     * @throws IOException if the request fails during communication
     * @throws InterruptedException if the request is interrupted
     */
    public void sendMessage(SendMessageRequest requestBodyObject) throws IOException, InterruptedException {
        String token = TokenStorage.getToken();

        if (token == null || token.isBlank()) {
            throw new RuntimeException("No JWT token found. User is not logged in.");
        }


        String requestBody = objectMapper.writeValueAsString(requestBodyObject);

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


        if (response.statusCode() != 200) {
            throw new RuntimeException("Sending message failed. Status code: " + response.statusCode());
        }
    }

    /**
     * Retrieves the public key of the user with the given username.
     * @param username the username of the user whose public key is requested
     * @return a response containing the user's public key
     * @throws IOException if the request fails during communication
     * @throws InterruptedException if the request is interrupted
     */
    public PublicKeyResponse getPublicKey(String username) throws IOException, InterruptedException {
        String token = TokenStorage.getToken();
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/user/public-key/" + encodedUsername))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("User-Agent", "JavaFX-MessageE2E-Client")
                .GET()
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), PublicKeyResponse.class);
        } else {
            throw new RuntimeException("Fetching public key failed. Status code: " + response.statusCode());
        }

    }
}
