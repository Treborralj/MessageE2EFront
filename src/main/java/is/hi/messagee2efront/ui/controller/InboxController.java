package is.hi.messagee2efront.ui.controller;

import is.hi.messagee2efront.model.response.ConversationSummaryResponse;
import is.hi.messagee2efront.model.response.MessageResponse;
import is.hi.messagee2efront.service.MessageService;
import is.hi.messagee2efront.service.TokenStorage;
import is.hi.messagee2efront.ui.MessageE2EApplication;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class InboxController {
    @FXML
    public Label titleLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public ListView<ConversationSummaryResponse> inboxListView;
    @FXML
    public Button refreshButton;
    private final MessageService messageService = new MessageService();

    @FXML
    public void initialize(){
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
}
