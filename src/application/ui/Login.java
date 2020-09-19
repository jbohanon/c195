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
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.Locale;

import static application.ui.Application.*;

public class Login {

    private static final boolean debugMode = true;

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

    public void loginGoBtnHandler() {
        attemptLogin();
    }

    public void loginExitBtnHandler() {
        exitApp();
    }

    @FXML
    protected void initialize() {
        if(debugMode) {
            userText.setText("test");
            passText.setText("test");
        }
        loginLabel.relocate(loginVBox.getLayoutX() + (loginVBox.getWidth()-loginLabel.getWidth())/2, loginLabel.getLayoutY());
    }

    private static void exitApp() {
        System.out.println("Login exitApp() called.");
        Main.exitApp(); }

    public void loginOnKeyPressedHandler(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            attemptLogin();
        }
    }

    public void attemptLogin() {
        if(!validateCreds()) {

            logUserLoginAttempt(userText.getText(), false);
            badCredsLabel.setVisible(true);
        }
        else {
            initializeLoggedInUser();
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

    private void logUserLoginAttempt(String userName, boolean successful) {
        String sep = System.getProperty("file.separator");
        File f = new File(String.format("src%sresources%slogins.txt", sep, sep));

        String s = Localization.getUtcNow().toString() +
                "\t" + userName + " login " + (successful ? "success." : "failure.") + "\n";

        try {
            System.out.println("File created: " + f.createNewFile());
            Files.write(f.toPath(), s.getBytes(), StandardOpenOption.APPEND);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeLoggedInUser() {
        logUserLoginAttempt(userText.getText(), true);
        Main.login.close();

        loggedInUser = userText.getText();
        try {
            usersAppointments = appointmentDAO.getAllForUser(userDAO.GetOptionalOrThrow(userDAO.lookup(loggedInUser)).getUserId());
        } catch(Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog("Unable to find any appointments for " + loggedInUser);
        }

        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);

        Main.app.show();
    }
}
