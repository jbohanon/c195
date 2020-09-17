package application;

import application.dao.Database;
import application.localization.Localization;
import application.ui.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

import static application.localization.Localization.getLocalizedString;

public class Main extends javafx.application.Application {

    public static Connection dbConn;
    public static Stage login;
    public static Stage app;

    public static void exitApp() {
        try {
            if (!dbConn.isClosed()) {
                dbConn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        dbConn = Database.connectToDatabase();
        if(!Database.testDbConn(dbConn)) {
            System.out.println("Failed to produce meaningful connection");
            exitApp();
        }

        app = FXMLLoader.load(getClass().getResource("ui/application.fxml"));

        Application.sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);

        login = FXMLLoader.load(getClass().getResource("ui/login.fxml"));
        login.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static String getStr(String propertyLabel) {
        return getLocalizedString(propertyLabel, Localization.RESOURCE_BUNDLE.APPLICATION);
    }
}
