package User;

import Communication.Database;
import General.Methods;
import Screen.Hoofdscherm;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class InlogController {
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;
    @FXML
    private Text tError;
    @FXML

    public void login() {
            boolean userExists = controlLogin(tfUserName.getText(), tfPassword.getText());

            if (userExists) {
                Hoofdscherm.openApplication();
                Hoofdscherm.setInlogController(this);
            } else {
                tError.setText("Er is iets fout gegaan!");
            }
    }

    public void toRegistration() {
        Hoofdscherm.showRegistration();
    }

    public boolean controlLogin(String userName, String password) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            String passwordHashed = Methods.hasher(password.trim());
            result = Database.executeQuery("SELECT id FROM account WHERE gebruikersnaam = '" + userName + "' AND wachtwoord = '" + passwordHashed + "';");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        try {
            Account.setAccountid(Integer.parseInt(result.get(0).get(0)));
            Account.getPref();
            Account.setIdentity(userName, password);
            return true;
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println(ioobe.getMessage());
            return false;
        }
    }

    public void setIdentity() {
        tfUserName.setText(Account.getUsername());
        tfPassword.setText(Account.getPassword());
    }
}
