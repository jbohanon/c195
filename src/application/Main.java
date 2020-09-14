package application;

import application.dao.Database;
import application.datamodel.Customer;
import application.localization.Localization;
import application.ui.ApplicationController;
import application.ui.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

import static application.localization.Localization.getLocalizedString;

public class Main extends Application {

    public static final String HomepageFxml = "ui/homepage.fxml";
    public static final String AppointmentPageFxml = "ui/appointmentPage.fxml";
    public static final String CustomerPageFxml = "ui/customerPage.fxml";
    public static final String SearchResultsFxml = "ui/searchResults.fxml";

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

        ApplicationController.sceneChanger.ChangeScene(HomepageFxml);

        login = FXMLLoader.load(getClass().getResource("ui/login.fxml"));
        login.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static String getStr(String propertyLabel) {
        return getLocalizedString(propertyLabel, Localization.RESOURCE_BUNDLE.APP);
    }
}
