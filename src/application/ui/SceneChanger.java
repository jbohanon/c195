package application.ui;

import application.Main;
import application.localization.Localization;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneChanger {

//    public void ChangeScene(String sceneFxml, String className) {
//        try {
//            ResourceBundle languageResource =
//                ResourceBundle.getBundle(className, Locale.getDefault());
//            Scene scene = FXMLLoader.load(Main.class.getResource(sceneFxml), languageResource);
//            Main.app.setScene(scene);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            throw new RuntimeException("Failed to load fxml at " + sceneFxml);
//        }
//    }
    public void ChangeScene(Localization.RESOURCE_BUNDLE res) {
        try {
            Scene scene = FXMLLoader.load(Main.class.getResource(res.GetFxml()), res.Get());
            Main.app.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to load fxml at " + res.GetFxml());
        }
    }
}
