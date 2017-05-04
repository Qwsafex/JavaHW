package client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGuiApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = SceneFactory.create();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}