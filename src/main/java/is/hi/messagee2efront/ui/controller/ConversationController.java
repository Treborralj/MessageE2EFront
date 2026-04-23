package is.hi.messagee2efront.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.messagee2efront.model.DecryptedMessageViewModel;
import is.hi.messagee2efront.model.EncryptedMessagePayload;
import is.hi.messagee2efront.model.request.SendMessageRequest;
import is.hi.messagee2efront.model.response.MessageResponse;
import is.hi.messagee2efront.model.response.PublicKeyResponse;
import is.hi.messagee2efront.service.CryptoService;
import is.hi.messagee2efront.service.MessageService;
import is.hi.messagee2efront.service.SessionStorage;
import is.hi.messagee2efront.ui.MessageE2EApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description:
 *
 *****************************************************************************/
public class ConversationController {
    @FXML
    public Label conversationTitleLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public ListView<DecryptedMessageViewModel> messageListView;
    @FXML
    public TextField messageInputField;
    @FXML
    public Button sendButton;

    private final MessageService messageService = new MessageService();
    private final CryptoService cryptoService = new CryptoService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String otherUsersUsername;

    /**
     * Initializes the conversation view and configures the message list.
     */
    @FXML
    public void initialize() {
        messageListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(DecryptedMessageViewModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Label textLabel = new Label(item.getPlaintext());
                textLabel.setWrapText(true);

                Label timeLabel = new Label(item.getFormattedTime());

                VBox bubble = new VBox(textLabel, timeLabel);
                bubble.setSpacing(3);
                bubble.setMaxWidth(350);

                HBox wrapper = new HBox(bubble);

                if (item.isSentByCurrentUser()) {
                    wrapper.setStyle("-fx-alignment: CENTER-RIGHT;");
                    bubble.setStyle("-fx-background-color: lightblue; -fx-padding: 8; -fx-background-radius: 8;");
                } else {
                    wrapper.setStyle("-fx-alignment: CENTER-LEFT;");
                    bubble.setStyle("-fx-background-color: lightgray; -fx-padding: 8; -fx-background-radius: 8;");
                }

                setText(null);
                setGraphic(wrapper);
            }
        });
    }

    /**
     * Sets the username of the other conversation participant.
     * @param otherUsersUsername the username of the other participant
     */
    public void setOtherUsersUsername(String otherUsersUsername) {
        this.otherUsersUsername = otherUsersUsername;
        conversationTitleLabel.setText("Conversation with " + otherUsersUsername);
        loadConversation();
    }

    /**
     * Returns to the inbox view.
     */
    @FXML
    public void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/inbox-view.fxml"));

            Scene scene = new Scene(loader.load(), 600, 500);
            Stage stage = (Stage) messageListView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inbox");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Could not load inbox page.");
        }
    }

    /**
     * Reloads the current conversation.
     */
    @FXML
    public void onRefreshClick() {
        loadConversation();
    }

    /**
     * Encrypts and sends a message to the current conversation partner.
     */
    @FXML
    public void onSendClick() {
        String plaintext = messageInputField.getText();

        if (plaintext == null || plaintext.isBlank()) {
            statusLabel.setText("Message cannot be empty");
            return;
        }

        sendButton.setDisable(true);
        statusLabel.setText("Sending message...");

        new Thread(() -> {
            try {
                String currentUsername = SessionStorage.getUsername();

                PublicKeyResponse receiverKeyResponse = messageService.getPublicKey(otherUsersUsername);
                PublicKey receiverPublicKey = cryptoService.parsePublicKey(receiverKeyResponse.getPublicKey());

                PublicKeyResponse senderKeyResponse = messageService.getPublicKey(currentUsername);
                PublicKey senderPublicKey = cryptoService.parsePublicKey(senderKeyResponse.getPublicKey());

                SecretKey aesKey = cryptoService.generateAesKey();
                byte[] iv = cryptoService.generateIv();

                String ciphertext = cryptoService.encryptPlainText(plaintext, aesKey, iv);
                String encryptedAesKeyForReceiver = cryptoService.encryptAesKey(aesKey, receiverPublicKey);
                String encryptedAesKeyForSender = cryptoService.encryptAesKey(aesKey, senderPublicKey);
                String ivBase64 = cryptoService.encodeBase64(iv);

                EncryptedMessagePayload payload = new EncryptedMessagePayload(
                        encryptedAesKeyForSender,
                        encryptedAesKeyForReceiver,
                        ivBase64,
                        ciphertext
                );

                SendMessageRequest request = new SendMessageRequest(
                        otherUsersUsername,
                        payload.getEncryptedAesKeyForSender(),
                        payload.getEncryptedAesKeyForReceiver(),
                        payload.getIv(),
                        payload.getCiphertext()
                );

                messageService.sendMessage(request);

                Platform.runLater(() -> {
                    messageInputField.clear();
                    sendButton.setDisable(false);
                    statusLabel.setText("Message sent.");
                    loadConversation();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    sendButton.setDisable(false);
                    statusLabel.setText("Failed to send message.");
                });
            }
        }).start();
    }

    /**
     * Loads encrypted messages from the backend, decrypts them and updates the UI
     * showing the messages decrypted.
     */
    private void loadConversation() {
        statusLabel.setText("Loading conversation...");

        new Thread(() -> {
            try {
                List<MessageResponse> messages = messageService.getConversation(otherUsersUsername);
                List<DecryptedMessageViewModel> decryptedMessages = new ArrayList<>();

                String currentUsername = SessionStorage.getUsername();
                PrivateKey privateKey = SessionStorage.getPrivateKey();

                for (MessageResponse message : messages) {
                    EncryptedMessagePayload payload = objectMapper.readValue(
                            message.getEncryptedContent(),
                            EncryptedMessagePayload.class
                    );

                    boolean sentByCurrentUser = message.getSenderUsername().equals(currentUsername);

                    String encryptedAesKeyToUse;
                    if (sentByCurrentUser) {
                        encryptedAesKeyToUse = payload.getEncryptedAesKeyForSender();
                    } else {
                        encryptedAesKeyToUse = payload.getEncryptedAesKeyForReceiver();
                    }

                    SecretKey aesKey = cryptoService.decryptAesKey(encryptedAesKeyToUse, privateKey);
                    byte[] iv = cryptoService.decodeBase64(payload.getIv());
                    String plaintext = cryptoService.decryptCipherText(payload.getCiphertext(), aesKey, iv);

                    decryptedMessages.add(new DecryptedMessageViewModel(
                            message.getSenderUsername(),
                            plaintext,
                            formatSentAt(message.getSentAt()),
                            sentByCurrentUser
                    ));
                }

                Platform.runLater(() -> {
                    messageListView.setItems(FXCollections.observableArrayList(decryptedMessages));
                    statusLabel.setText("Loaded " + messages.size() + " messages.");
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> statusLabel.setText("Failed to load conversation."));
            }
        }).start();
    }

    /**
     * Formats a message timestamp for display in the conversation UI.
     * @param sentAt the raw timestamp string
     * @return the formatted timestamp string
     */
    private String formatSentAt(String sentAt) {
        LocalDateTime dateTime = LocalDateTime.parse(sentAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");
        return dateTime.format(formatter);
    }
}
