package is.hi.messagee2efront.ui.controller;

import is.hi.messagee2efront.model.response.MessageResponse;
import is.hi.messagee2efront.service.MessageService;
import is.hi.messagee2efront.ui.MessageE2EApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class ConversationController {
    @FXML
    public Label conversationTitleLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public ListView<MessageResponse> messageListView;

    private final MessageService messageService = new MessageService();
    private String otherUsersUsername;

    public void setOtherUsersUsername(String otherUsersUsername){
        this.otherUsersUsername = otherUsersUsername;
        conversationTitleLabel.setText("Conversation with " + otherUsersUsername);
        loadConversation();
    }

    @FXML
    public void onBackClick() {
        try{
            FXMLLoader loader = new FXMLLoader(
                    MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/inbox-view.fxml"));

            Scene scene = new Scene(loader.load(), 500, 500);
            Stage stage = (Stage) messageListView.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Inbox");
        } catch (Exception e){
            e.printStackTrace();
            statusLabel.setText("Could not load inbox page.");
        }
    }

    @FXML
    public void onRefreshClick() {
        loadConversation();
    }

    private void loadConversation(){
        statusLabel.setText("Loading conversation...");

        new Thread(() -> {
            try{
                List<MessageResponse> messages = messageService.getConversation(otherUsersUsername);

                Platform.runLater(() -> {
                    messageListView.setItems(FXCollections.observableArrayList(messages));
                    statusLabel.setText("Loaded " + messages.size() + " messages.");
                });
            }catch (Exception e){
                Platform.runLater(() -> statusLabel.setText("Failed to load conversation."));
            }
        }).start();
    }
}
