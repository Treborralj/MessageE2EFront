package is.hi.messagee2efront.ui.controller;

import is.hi.messagee2efront.model.response.ConversationSummaryResponse;
import is.hi.messagee2efront.service.MessageService;
import is.hi.messagee2efront.service.SessionStorage;
import is.hi.messagee2efront.service.TokenStorage;
import is.hi.messagee2efront.ui.MessageE2EApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class InboxController {
    @FXML
    public Label titleLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public ListView<ConversationSummaryResponse> inboxListView;
    @FXML
    public Button refreshButton;
    @FXML
    public TextField newConversationUsernameField;
    private final MessageService messageService = new MessageService();

    @FXML
    public void initialize(){
        inboxListView.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(ConversationSummaryResponse item, boolean empty){
                super.updateItem(item, empty);

                if (empty || item == null){
                    setText(null);
                    setGraphic(null);
                    return;
                }

                String firstLine;
                if(item.getUnreadCount() > 0){
                    firstLine = item.getUnreadCount() + " new messages from " + item.getOtherUsersUsername();
                } else{
                  firstLine = "0 new messages from " + item.getOtherUsersUsername();
                }

                Label title = new Label(firstLine);
                if(item.getUnreadCount() > 0){
                   title.setStyle("-fx-font-weight: bold;");
                }

                String timeText = "";
                if(item.getLastMessageSentAt() != null && !item.getLastMessageSentAt().isBlank()){
                    timeText = "Last activity: " + formatSentAt(item.getLastMessageSentAt());
                }

                Label timeLabel = new Label(timeText);
                timeLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 11px");

                VBox box = new VBox(title, timeLabel);
                box.setSpacing(3);

                setText(null);
                setGraphic(box);
            }
        });

        inboxListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                ConversationSummaryResponse selected = inboxListView.getSelectionModel().getSelectedItem();
                if(selected != null){
                    openConversationPage(selected.getOtherUsersUsername());
                }
            }
        });
        loadConversationSummaries();
    }

    @FXML
    public void onRefreshClick() {
        loadConversationSummaries();
    }

    @FXML
    public void onOpenConversationClick(){
        ConversationSummaryResponse selected = inboxListView.getSelectionModel().getSelectedItem();
        if(selected != null){
            openConversationPage(selected.getOtherUsersUsername());
        } else{
            statusLabel.setText("Please select a conversation.");
        }
    }

    @FXML
    public void onLogoutClick() {
        TokenStorage.setToken(null);
        SessionStorage.clear();

        try{
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 320, 280);
            Stage stage = (Stage) inboxListView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (Exception e){
            e.printStackTrace();
            statusLabel.setText("Could not load login page.");
        }
    }

    private void loadConversationSummaries(){
        refreshButton.setDisable(true);
        statusLabel.setText("Loading inbox...");

        new Thread(() -> {
            try{
               List<ConversationSummaryResponse> conversations = messageService.getConversationSummaries();

                Platform.runLater(() -> {
                    inboxListView.setItems(FXCollections.observableArrayList(conversations));
                    statusLabel.setText("Loaded " + conversations.size() + " conversations");
                    refreshButton.setDisable(false);
                });
            } catch (Exception e){
                Platform.runLater(() -> {
                    statusLabel.setText("Failed to load inbox");
                    refreshButton.setDisable(false);
                });
                e.printStackTrace();
            }
        }).start();
    }

    private void openConversationPage(String username){
        try{
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/conversation-view.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);

            ConversationController controller = loader.getController();
            controller.setOtherUsersUsername(username);

            Stage stage = (Stage) inboxListView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Conversation with " +  username);
        } catch(Exception e){
            e.printStackTrace();
            statusLabel.setText("Could not open conversation.");
        }
    }

    public void onNewConversationClick() {
        String username = newConversationUsernameField.getText();

        if(username == null || username.isBlank()){
            statusLabel.setText("You must enter a username");
            return;
        }

        username = username.trim();

        if(username.equals(SessionStorage.getUsername())){
            statusLabel.setText("You can not start send yourself a message.");
            return;
        }

        newConversationUsernameField.clear();
        openConversationPage(username);
    }

    private String formatSentAt(String sentAt) {
        LocalDateTime dateTime = LocalDateTime.parse(sentAt);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM HH:mm");
        return dateTime.format(formatter);
    }
}
