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
        if(checkUserName(tfUserName.getText())) {
            return;
        }

        String password = tfPassword.getText();
        String userName = tfUserName.getText();

        try {
            password = Methods.hasher(password.trim());
            int resultaat = Database.executeUpdate("INSERT INTO account (gebruikersnaam, wachtwoord) VALUES ('" + userName + "', '" + password + "');");

            if (resultaat == 1) {
                domApplication.showLogin();
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

    public void setDomApplication(DomApplication domApplication) {
        this.domApplication = domApplication;
    }
}
