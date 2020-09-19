package application;

import application.dao.Database;
import application.datamodel.Appointment;
import application.localization.Localization;
import application.ui.Application;
import application.ui.DialogController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import sun.jvm.hotspot.runtime.Threads;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;


public class Main extends javafx.application.Application {

    //    public static Connection dbConn;
    public static Stage login;
    public static Stage app;

    public static void exitApp() {
        Platform.exit();
    }

    // PRODUCTION LOCALE
    private static final Locale usingLocale = Locale.getDefault();

    // DEBUG LOCALES
//    private static final Locale usingLocale = Locale.US;
//    private static final Locale usingLocale = Locale.CANADA_FRENCH;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Start thread to open and ensure open db connection
        Runnable r = () -> {
            try {
                Database.connect();
            } catch (Exception ex) {
                ex.printStackTrace();
                DialogController.okModalDialog("Database connection failed. Exiting.");
                Platform.exit();
            }
        };

        new Thread(r).start();

        Locale.setDefault(usingLocale);

        app = FXMLLoader.load(getClass().getResource("ui/application.fxml"));

        login = FXMLLoader.load(getClass().getResource("ui/login.fxml"), Localization.RESOURCE_BUNDLE.LOGIN.Get());
        login.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
