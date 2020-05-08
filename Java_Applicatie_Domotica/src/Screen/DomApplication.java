package Screen;

import Communication.Database;
import User.Account;
import User.InlogController;
import User.RegistrationController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

public class DomApplication extends Application{
    private Stage primaryStage;
    private Scene loginScene;
    private Scene registrationScene;
    private InlogController inlogController;
    private RegistrationController registrationController;
    private FXMLLoader loader;

    private LeftScreen leftScreen;
    private CenterScreen centerScreen;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                leftScreen.getCommunicator().terminate();
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setTitle("Domotica Systeem");

        buildLogin();
        buildRegistration();

        boolean activeUser = checkActiveUsers();

        if (activeUser) openApplication();
        else showLogin();
    }

    public void openApplication() {
        primaryStage.hide();
        primaryStage.setTitle("Domotica Systeem: " + Account.getUsername());

        BorderPane layout = new BorderPane();

        leftScreen = new LeftScreen(this);
        layout.setLeft(leftScreen.getLeftPane());

        centerScreen = new CenterScreen(this);
        layout.setCenter(centerScreen.getCenterPane());

        leftScreen.setCenterScreen(centerScreen);

        Scene defaultScene = new Scene(layout, 1500, 800);
        primaryStage.setScene(defaultScene);

        primaryStage.show();
    }

    public void showRegistration() {
        try {
            primaryStage.hide();
            primaryStage.setScene(registrationScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLogin() {
        try {
            primaryStage.hide();
            primaryStage.setScene(loginScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildRegistration() {
        try {
            loader = new FXMLLoader(getClass().getResource("../User/Registration.fxml"));
            Parent registrationScreen = loader.load();
            registrationController = loader.getController();
            registrationController.setDomApplication(this);
            registrationScene = new Scene(registrationScreen, 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildLogin() {
        try {
            loader = new FXMLLoader(getClass().getResource("../User/Login.fxml"));
            Parent loginScreen = loader.load();
            inlogController = loader.getController();
            inlogController.setDomApplication(this);
            loginScene = new Scene(loginScreen, 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LeftScreen getLeftScreen() {
        return leftScreen;
    }

    public boolean checkActiveUsers() {
        ArrayList<ArrayList<String>> result = Database.executeQuery("SELECT id, gebruikersnaam FROM account WHERE active=1");

        try {
            Account.setAccountid(Integer.valueOf(result.get(0).get(0)));
            Account.setIdentity(result.get(0).get(1));
            return true;
        } catch (IndexOutOfBoundsException ioobe) {
            return false;
        }
    }
}
