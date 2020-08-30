package application.ui;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.sql.*;

public class LoginController {

    private final boolean debugMode = true;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passText;

    @FXML
    private Label badCredsLabel;

    public void init() {
        if(debugMode) {
            userText.setText("test");
            passText.setText("test");
            attemptLogin();
        }
    }

    private void exitApp() {
        System.out.println("LoginController exitApp() called.");
        Main.exitApp(); }

    public void exitBtnPress() {
        exitApp();
    }

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
