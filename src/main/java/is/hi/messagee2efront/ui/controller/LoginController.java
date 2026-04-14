package is.hi.messagee2efront.ui.controller;

import is.hi.messagee2efront.model.request.LoginRequest;
import is.hi.messagee2efront.model.response.AuthenticationResponse;
import is.hi.messagee2efront.service.AuthService;
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

    @FXML
    protected void onLoginClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username == null || username.isBlank() || password == null || password.isBlank()){
            statusLabel.setText("Username and password must not be empty.");
            return;
        }
        loginButton.setDisable(true);
        statusLabel.setText("Logging in...");

        new Thread(() -> {
            try{
                LoginRequest request = new LoginRequest(username, password);
                AuthenticationResponse response = authService.login(request);

                Platform.runLater(() -> {
                    TokenStorage.setToken(response.getToken());
                    loginButton.setDisable(false);
                    goToInboxPage();
                });

            } catch (Exception e){
                Platform.runLater(() -> {
                    statusLabel.setText("Login failed.");
                    loginButton.setDisable(false);
                });
                e.printStackTrace();
            }
        }).start();

    }

    @FXML
    protected void onGoToSignupClick() {
        try{
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/signup-view.fxml"));
            Scene scene = new Scene(loader.load(), 320, 280);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign up");
        } catch (Exception e){
            e.printStackTrace();
            statusLabel.setText("Could not load signup page");
        }
    }

    @FXML
    private void goToInboxPage(){
        try{
            FXMLLoader  loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/inbox-view.fxml"));
            Scene scene = new Scene(loader.load(), 500, 500);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inbox");
        } catch (Exception e){
            statusLabel.setText("Could not load inbox page.");
        }
    }
}