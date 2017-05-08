package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Gui application for communication with FTP server.
 */
public class ClientGuiApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new SceneFactory().create();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch GUI app.
     * @param args console arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}