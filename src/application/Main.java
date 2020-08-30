package application;

import application.dao.Database;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

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

        app = primaryStage;
        app.setScene(new Scene(FXMLLoader.load(getClass().getResource("ui/application.fxml"))));

        login = FXMLLoader.load(getClass().getResource("ui/login.fxml"));
        login.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
