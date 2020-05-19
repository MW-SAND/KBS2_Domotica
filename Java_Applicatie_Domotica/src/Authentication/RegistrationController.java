package Authentication;

import General.Database;
import General.Methods;
import General.DomApplication;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class RegistrationController {
    private DomApplication domApplication;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfError;


    public void toLogin() {
        domApplication.showLogin();
    }

    public void register() {
        // controleert of de gebruikersnaam al bestaat
        if(checkUserName(tfUserName.getText())) {
            return;
        }

        String password = tfPassword.getText();
        String userName = tfUserName.getText();

        try {
            // wachtwoord hashen
            password = Methods.hasher(password.trim());

            // gebruiker toevoegen
            String query = "INSERT INTO account (gebruikersnaam, wachtwoord) VALUES ('" + userName + "', '" + password + "');";
            int resultaat = Database.executeUpdate(query);

            // als het toevoegen geslaagd is verschijnt het inlogscherm
            if (resultaat == 1) {
                domApplication.showLogin();
            } else {
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    // controleert of de gebruikersnaam al bekend is
    public boolean checkUserName(String userName) {
        String query = "SELECT gebruikersnaam FROM account WHERE gebruikersnaam = '" + userName + "';";
        ArrayList<ArrayList<String>> result = Database.executeQuery(query);

        if(result.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setDomApplication(DomApplication domApplication) {
        this.domApplication = domApplication;
    }
}
