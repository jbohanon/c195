package application.ui;

import application.Main;
import application.localization.Localization;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static application.ui.Application.PageHistoryStack;

public class SceneChanger {
    public void ChangeScene(Localization.RESOURCE_BUNDLE newSceneResource) {
        try {
            Scene scene = FXMLLoader.load(Main.class.getResource(newSceneResource.GetFxml()), newSceneResource.Get());
            Main.app.setScene(scene);
            PageHistoryStack.push(newSceneResource);
            System.out.println(String.format("%s on top of %d tall stack", PageHistoryStack.peek().toString(), PageHistoryStack.size()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load fxml at " + newSceneResource.GetFxml());
        }
    }
}
