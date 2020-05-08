package Screen;

import User.InlogController;
import User.RegistrationController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

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
        primaryStage.setTitle("Domotica Systeem");

        buildLogin();
        buildRegistration();

        this.primaryStage = primaryStage;

        showLogin(false);
    }

    public void openApplication() {
        primaryStage.hide();

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

    public void showLogin(boolean logout) {
        try {
            primaryStage.hide();
            if (logout == true) inlogController.setIdentity();
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
}
