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

// TODO Fix appointment search
// TODO Reports
// TODO Load user schedule on login
// TODO Show modal if any appointment within 15 minutes of login
// TODO Show user schedule in week and month format
// TODO Validate appointment creation/edit time selection with actually available times
// TODO Appointment "Go To" customer button
// TODO Fix localizing time zones / locale styles implementation
// TODO Prevent scheduling an appointment outside business hours
// TODO Prevent scheduling overlapping appointments
// TODO Prevent entering nonexistent or invalid customer data
// TODO Prevent entering an incorrect username and password
// TODO Find and comment lambdas

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
