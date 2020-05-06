package User;

import Communication.Database;
import General.Methods;
import Screen.Hoofdscherm;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class RegistrationController {
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfError;

    public void toLogin() {
        Hoofdscherm.showLogin();
    }

    public void register() {
        if(checkUserName(tfUserName.getText())) {
            return;
        }

        String password = tfPassword.getText();
        String userName = tfUserName.getText();

        try {
            password = Methods.hasher(password.trim());
            int resultaat = Database.executeUpdate("INSERT INTO account (gebruikersnaam, wachtwoord) VALUES ('" + userName + "', '" + password + "');");

            if (resultaat == 1) {
                Hoofdscherm.showLogin();
            } else {
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public boolean checkUserName(String userName) {
        ArrayList<ArrayList<String>> result = Database.executeQuery("SELECT gebruikersnaam FROM account WHERE gebruikersnaam = '" + userName + "';");

        if(result.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
