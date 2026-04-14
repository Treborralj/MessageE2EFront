package is.hi.messagee2efront.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.messagee2efront.model.request.LoginRequest;
import is.hi.messagee2efront.model.request.SignupRequest;
import is.hi.messagee2efront.model.response.AuthenticationResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class AuthService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthenticationResponse login(LoginRequest loginRequest) throws IOException, InterruptedException{
        String requestBody = objectMapper.writeValueAsString(loginRequest);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/auth/login"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            return objectMapper.readValue(response.body(),AuthenticationResponse.class);
        } else{
            throw new RuntimeException("login failed. Status code: " + response.statusCode()
            + ", body: " + response.body());
        }
    }
    public void signup(SignupRequest signupRequest) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(signupRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ApiClient.getBaseUrl() + "/auth/signup"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = ApiClient.getHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Signup failed. Status code: " + response.statusCode()
                    + ", body: " + response.body());
        }
    }
}
