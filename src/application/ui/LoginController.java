package application.ui;

import application.Main;
import application.localization.Localization;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.util.Locale;

public class LoginController {

    private static final boolean debugMode = false;

//    private static final Locale usingLocale = Localization.getLocale();

    // DEBUG LOCALES
    //    private static final Locale usingLocale = Locale.US;
    private static final Locale usingLocale = Locale.CANADA_FRENCH;
    @FXML
    private VBox loginVBox;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passText;

    @FXML
    private Label loginLabel;

    @FXML
    private Label badCredsLabel;

    @FXML
    private Button loginGoBtn;

    public void loginGoBtnHandler() {
        attemptLogin();
    }

    @FXML
    private Button loginExitBtn;

    public void loginExitBtnHandler() {
        exitApp();
    }

    @FXML
    protected void initialize() {
        if(debugMode) {
            userText.setText("test");
            passText.setText("test");
            attemptLogin();
        }
        Locale.setDefault(usingLocale);
        loginLabel.setText(Localization.getLocalizedString("loginLabel", Localization.RESOURCE_BUNDLE.LOGIN));
        loginLabel.relocate(loginVBox.getLayoutX() + (loginVBox.getWidth()-loginLabel.getWidth())/2, loginLabel.getLayoutY());
        userText.setPromptText(Localization.getLocalizedString("userPrompt", Localization.RESOURCE_BUNDLE.LOGIN));
        passText.setPromptText(Localization.getLocalizedString("passPrompt", Localization.RESOURCE_BUNDLE.LOGIN));
        loginGoBtn.setText(Localization.getLocalizedString("loginGoBtn", Localization.RESOURCE_BUNDLE.LOGIN));
        loginExitBtn.setText(Localization.getLocalizedString("loginExitBtn", Localization.RESOURCE_BUNDLE.LOGIN));
    }

    private static void exitApp() {
        System.out.println("LoginController exitApp() called.");
        Main.exitApp(); }

    public void loginOnKeyPressedHandler(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            attemptLogin();
        }
    }

    public void attemptLogin() {
        if(!validateCreds()) {
            badCredsLabel.setVisible(true);
        }
        else {
            Main.login.close();

            ApplicationController.loggedInUser = userText.getText();
            Main.app.show();
        }
    }

    private boolean validateCreds() {

        try {
            Statement stmt = Main.dbConn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT password FROM user WHERE userName='" + userText.getText().replace("'", "") + "'");
            if(!rs.next()) {
                return false;
            } else return rs.getString("password").equals(passText.getText());
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
