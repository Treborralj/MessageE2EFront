package is.hi.messagee2efront.ui.controller;

import is.hi.messagee2efront.model.request.SignupRequest;
import is.hi.messagee2efront.service.AuthService;
import is.hi.messagee2efront.service.KeyService;
import is.hi.messagee2efront.service.KeyStorageService;
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

import java.security.KeyPair;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class SignupController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public Button signupButton;
    @FXML
    public Label statusLabel;

    private final AuthService authService = new AuthService();
    private final KeyService keyService = new KeyService();
    private final KeyStorageService keyStorageService = new KeyStorageService();

    @FXML
    protected void onSignupClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(username == null || username.isBlank()
                || password == null || password.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()){
            statusLabel.setText("All fields must be filled");
            return;
        }

        if(!password.equals(confirmPassword)){
            statusLabel.setText("Passwords do not match");
            return;
        }
        signupButton.setDisable(true);
        statusLabel.setText("Creating account...");

        new Thread(() -> {
            try{
                KeyPair keyPair = keyService.generateRsaKeyPair();
                keyStorageService.savePrivateKey(username, password, keyPair.getPrivate());
                String publicKeyBase64 = keyService.publicKeyToBase64(keyPair);

                SignupRequest request = new SignupRequest(username, password, publicKeyBase64);
                authService.signup(request);

                Platform.runLater(() -> {
                    statusLabel.setText("Account created. Redirecting to login...");
                    signupButton.setDisable(false);
                    goToLoginPage();
                });
            } catch(Exception e){
                Platform.runLater(() -> {
                    statusLabel.setText("Signup failed.");
                    signupButton.setDisable(false);
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void onBackToLoginClick() {
        goToLoginPage();
    }

    private void goToLoginPage(){
        try{
            FXMLLoader loader = new FXMLLoader(MessageE2EApplication.class.getResource(
                    "/is/hi/messagee2efront/ui/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 320, 280);

            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch(Exception e){
            e.printStackTrace();
            statusLabel.setText("Could not load login page.");
        }
    }
}
