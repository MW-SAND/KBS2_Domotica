package Screen;

import Communication.Communication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class Hoofdscherm extends Application{
    private static Scene defaultScene;
    private static BorderPane layout;
    private static LeftScreen leftScreen;
    private static CenterScreen centerScreen;
    private static Stage primaryStage;
    private static Scene loginScene;
    private static Scene registrationScene;
    private static Parent registrationScreen;
    private static Parent loginScreen;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Domotica Systeem");

        buildLogin();
        buildRegistration();

        Hoofdscherm.primaryStage = primaryStage;

        showLogin();
    }

    public static void openApplication() {
        primaryStage.hide();

        layout = new BorderPane();

        leftScreen = new LeftScreen();
        layout.setLeft(leftScreen.getLeftPane());

        centerScreen = new CenterScreen(leftScreen);
        layout.setCenter(centerScreen.getCenterPane());

        leftScreen.setCenterScreen(centerScreen);

        defaultScene = new Scene(layout, 1500, 800);
        primaryStage.setScene(defaultScene);

        primaryStage.show();

        Communication communicator = new Communication(leftScreen);
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

    public static void showLogin() {
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
            registrationScreen = FXMLLoader.load(getClass().getResource("../User/Registration.fxml"));
            registrationScene = new Scene(registrationScreen, 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildLogin() {
        try {
            loginScreen = FXMLLoader.load(getClass().getResource("../User/Login.fxml"));
            loginScene = new Scene(loginScreen, 400, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
