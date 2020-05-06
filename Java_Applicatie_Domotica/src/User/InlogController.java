package User;

import Communication.Database;
import General.Methods;
import Screen.Hoofdscherm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class InlogController {
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;
    @FXML
    private Text tError;


    public void login() {
        boolean userExists = controlLogin(tfUserName.getText(), tfPassword.getText());
        System.out.println(userExists);
        if (userExists) {
            Hoofdscherm.openApplication();
        } else {
            //tError.setText("Foutieve inloggegevens");
            System.out.println("Foutieve inloggegevens");
        }
    }

    public void toRegistration() {
        Hoofdscherm.showRegistration();
    }

    public boolean controlLogin(String userName, String password) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        try {
            password = Methods.hasher(password.trim());
            result = Database.executeQuery("SELECT id FROM account WHERE gebruikersnaam = '" + userName + "' AND wachtwoord = '" + password + "';");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        try {
            Account.setAccountid(Integer.valueOf(result.get(0).get(0)));
            Account.getPref();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
