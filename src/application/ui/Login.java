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
//            attemptLogin();
        }
//        loginLabel.setText(Localization.getLocalizedString("loginLabel", Localization.RESOURCE_BUNDLE.LOGIN));
        loginLabel.relocate(loginVBox.getLayoutX() + (loginVBox.getWidth()-loginLabel.getWidth())/2, loginLabel.getLayoutY());
//        userText.setPromptText(Localization.getLocalizedString("userPrompt", Localization.RESOURCE_BUNDLE.LOGIN));
//        passText.setPromptText(Localization.getLocalizedString("passPrompt", Localization.RESOURCE_BUNDLE.LOGIN));
//        loginGoBtn.setText(Localization.getLocalizedString("loginGoBtn", Localization.RESOURCE_BUNDLE.LOGIN));
//        loginExitBtn.setText(Localization.getLocalizedString("loginExitBtn", Localization.RESOURCE_BUNDLE.LOGIN));
//        badCredsLabel.setText(Localization.getLocalizedString("badCredsLabel", Localization.RESOURCE_BUNDLE.LOGIN));
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
            logUserLoginAttempt(userText.getText(), true);
            Main.login.close();

            Application.loggedInUser = userText.getText();
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

    private void logUserLoginAttempt(String userName, boolean successful) {
        String sep = System.getProperty("file.separator");
        File f = new File(String.format("src%sresources%slogins.txt", sep, sep));

        String s = Localization.getUtcNow().toString() +
                "\t" + userName + " login " + (successful ? "success." : "failure.") + "\n";

        try {
            System.out.println("File created: " + f.createNewFile());
////            FileWriter fw = new FileWriter(f);
////            fw.append(s);
//            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//            bw.append(s);
//            bw.close();
            Files.write(f.toPath(), s.getBytes(), StandardOpenOption.APPEND);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
