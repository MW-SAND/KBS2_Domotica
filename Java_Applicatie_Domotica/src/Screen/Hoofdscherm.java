package Screen;

import Communication.Communication;
import User.InlogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Hoofdscherm extends Application{
    private static Stage primaryStage;
    private static Scene loginScene;
    private static Scene registrationScene;
    private static InlogController inlogController;
    private static Communication communicator;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Domotica Systeem");

        buildLogin();
        buildRegistration();

        Hoofdscherm.primaryStage = primaryStage;

        showLogin(false);
    }

    public static void openApplication() {
        primaryStage.hide();

        BorderPane layout = new BorderPane();

        LeftScreen leftScreen = new LeftScreen();
        layout.setLeft(leftScreen.getLeftPane());

        CenterScreen centerScreen = new CenterScreen(leftScreen);
        layout.setCenter(centerScreen.getCenterPane());

        leftScreen.setCenterScreen(centerScreen);

        Scene defaultScene = new Scene(layout, 1500, 800);
        primaryStage.setScene(defaultScene);

        primaryStage.show();

        communicator = new Communication(leftScreen);
        communicator.start();
    }

    public static void showRegistration() {
        try {
            primaryStage.hide();
            primaryStage.setScene(registrationScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showLogin(boolean logout) {
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
            Parent registrationScreen = FXMLLoader.load(getClass().getResource("../User/Registration.fxml"));
            registrationScene = new Scene(registrationScreen, 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildLogin() {
        try {
            Parent loginScreen = FXMLLoader.load(getClass().getResource("../User/Login.fxml"));
            loginScene = new Scene(loginScreen, 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInlogController(InlogController inlogController) {
        Hoofdscherm.inlogController = inlogController;
    }

    public static void closeConnections() {
        communicator.terminate();
    }
}
