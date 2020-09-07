package application;

import application.dao.Database;
import application.datamodel.Customer;
import application.localization.Localization;
import application.ui.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static application.localization.Localization.getLocalizedString;

public class Main extends Application {

    public static Connection dbConn;
    public static Stage login;
    public static Stage app;
    public static Scene homepage;
    public static Scene apptPage;
    public static Scene custPage;
    public static Scene searchResults;

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

//        app = primaryStage;
//        app.setScene(new Scene(FXMLLoader.load(getClass().getResource("ui/application.fxml"))));

        // Load Stages
        app = FXMLLoader.load(getClass().getResource("ui/application.fxml"));
        login = FXMLLoader.load(getClass().getResource("ui/login.fxml"));

        // Load Scenes
        homepage = FXMLLoader.load(getClass().getResource("ui/homepage.fxml"));
        apptPage = FXMLLoader.load(getClass().getResource("ui/appointmentPage.fxml"));
        custPage = FXMLLoader.load(getClass().getResource("ui/customerPage.fxml"));
        searchResults = FXMLLoader.load(getClass().getResource("ui/searchResults.fxml"));

        app.setScene(homepage);
        login.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static String getStr(String propertyLabel) {
        return getLocalizedString(propertyLabel, Localization.RESOURCE_BUNDLE.APP);
    }
}
