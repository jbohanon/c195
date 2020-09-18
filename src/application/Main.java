package application;

import application.dao.Database;
import application.datamodel.Appointment;
import application.localization.Localization;
import application.ui.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

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


//    private static final Locale usingLocale = Localization.getLocale();

    // DEBUG LOCALES
        private static final Locale usingLocale = Locale.US;
//    private static final Locale usingLocale = Locale.CANADA_FRENCH;

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println(Appointment.APPT_TYPE.INTRODUCTION.getString());
        System.out.println(Appointment.APPT_TYPE.CONSULT_TAX.getString());
        System.out.println(Appointment.APPT_TYPE.CONSULT_INVEST.getString());


        Locale.setDefault(usingLocale);

        dbConn = Database.connectToDatabase();
        if(!Database.testDbConn(dbConn)) {
            System.out.println("Failed to produce meaningful connection");
            exitApp();
        }

        app = FXMLLoader.load(getClass().getResource("ui/application.fxml"));

        Application.sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);

        login = FXMLLoader.load(getClass().getResource("ui/login.fxml"), Localization.RESOURCE_BUNDLE.LOGIN.Get());
        login.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static String getStr(String propertyLabel) {
        return getLocalizedString(propertyLabel, Localization.RESOURCE_BUNDLE.APPLICATION);
    }
}
