package Authentication;

import General.Database;
import General.Methods;
import General.DomApplication;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class InlogController {
    private DomApplication domApplication;
    // UI elementen uit Login.fxml
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;
    @FXML
    private Text tError;

    // functie wordt aangeroepen bij het indrukken op de knop bInloggen
    public void login() {
            // controleren of gebruikersnaam + wachtwoord bekend zijn
            boolean userExists = controlLogin(tfUserName.getText(), tfPassword.getText());

            if (userExists) {
                domApplication.openApplication();
                Database.executeUpdate("UPDATE account SET active=1 WHERE id=" + Account.getAccountid() + ";");
            } else {
                tError.setText("Er is iets fout gegaan!");
            }
    }

    // laat het registratiescherm zien, wordt aangeroepen door de knop bNewAccount
    public void toRegistration() {
        domApplication.showRegistration();
    }

    // controleert of gebruikersnaam + wachtwoord bekend zijn in de Database
    public boolean controlLogin(String userName, String password) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            // wachtwoord hashen en gebruiker ophalen
            String passwordHashed = Methods.hasher(password.trim());
            String query = "SELECT id FROM account WHERE gebruikersnaam = '" + userName + "' AND wachtwoord = '" + passwordHashed + "';";
            result = Database.executeQuery(query);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        try {
            // Klasse van account instellen
            Account.setAccountid(Integer.parseInt(result.get(0).get(0)));
            Account.getPref();
            Account.setIdentity(userName);
            return true;
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println(ioobe.getMessage());
            return false;
        }
    }

    public void setDomApplication(DomApplication domApplication) {
        this.domApplication = domApplication;
    }
}
