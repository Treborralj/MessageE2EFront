package is.hi.messagee2efront.ui.controller;

import is.hi.messagee2efront.model.request.LoginRequest;
import is.hi.messagee2efront.model.response.AuthenticationResponse;
import is.hi.messagee2efront.service.AuthService;
import is.hi.messagee2efront.service.KeyStorageService;
import is.hi.messagee2efront.service.SessionStorage;
import is.hi.messagee2efront.service.TokenStorage;
import is.hi.messagee2efront.ui.MessageE2EApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Controls the login view and hanles user authentication input.
 *
 *****************************************************************************/
public class LoginController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label statusLabel;
    private final AuthService authService = new AuthService();
    @FXML
    public Button loginButton;
    private final KeyStorageService keyStorageService = new KeyStorageService();

    /**
     * Attempts to authenticate the user and open the inbox view.
     */
    @FXML
    protected void onLoginClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            statusLabel.setText("Username and password must not be empty.");
            return;
        }
        loginButton.setDisable(true);
        statusLabel.setText("Logging in...");

        new Thread(() -> {
            try {
                LoginRequest request = new LoginRequest(username, password);
                AuthenticationResponse response = authService.login(request);
                System.out.println("Token: " + response.getToken());

                Platform.runLater(() -> {
                    TokenStorage.setToken(response.getToken());
                    SessionStorage.setUsername(username);
                    SessionStorage.setPrivateKey(keyStorageService.loadPrivateKey(username, password));
                    loginButton.setDisable(false);
                    goToInboxPage();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    statusLabel.setText("Login failed.");
                    loginButton.setDisable(false);
                });
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Opens the signup page.
     */
    @FXML
    protected void onGoToSignupClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/signup-view.fxml"));
            Scene scene = new Scene(loader.load(), 500, 280);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign up");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Could not load signup page");
        }
    }

    /**
     * Opens the inbox page.
     */
    @FXML
    private void goToInboxPage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/inbox-view.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inbox");
        } catch (Exception e) {
            statusLabel.setText("Could not load inbox page.");
        }
    }
}