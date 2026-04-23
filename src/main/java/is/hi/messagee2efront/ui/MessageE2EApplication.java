package is.hi.messagee2efront.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description : This class contains the main method that starts the
 *               application
 *
 *****************************************************************************/
public class MessageE2EApplication extends Application {
    /**
     * Loads the initial JavaFX view and shows the main application window.
     * @param stage the primary stage of the application
     * @throws IOException if the initial FXML view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MessageE2EApplication.class.getResource("/is/hi/messagee2efront/ui/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX application.
     */
    public static void main(String[] args) {
        launch();
    }
}