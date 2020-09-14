package application.ui;

import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneChanger {

    public void ChangeScene(String sceneFxml) {
        try {
            Scene scene = FXMLLoader.load(Main.class.getResource(sceneFxml));
            Main.app.setScene(scene);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load fxml at " + sceneFxml);
        }
    }
}
