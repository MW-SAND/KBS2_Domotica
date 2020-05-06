package Screen;

import User.Account;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PreferencePane implements EventHandler<ActionEvent> {
    private LeftScreen leftScreen;

    private BorderPane borderPane;
    private HBox menu;
    private Button returnButton;
    private Text title;
    private Button saveButton;

    private GridPane body;
    private Text tempLabel;
    private Text brightLabel;
    private Text currentTemp;
    private Text currentBright;
    private TextField newTemp;
    private TextField newBright;
    private Text explanation;
    private Text notification;

    public PreferencePane(LeftScreen leftScreen) {
        this.leftScreen = leftScreen;
        borderPane = new BorderPane();

        createMenu();
        borderPane.setTop(menu);

        createBody();
        borderPane.setCenter(body);
    }

    public void createMenu() {
        menu = new HBox();

        returnButton = new Button("<");
        title = new Text("Voorkeuren");
        saveButton = new Button("Opslaan");

        saveButton.setOnAction(this);

        ObservableList lijst = menu.getChildren();
        lijst.addAll(returnButton, title, saveButton);
    }

    public void createBody() {
        body = new GridPane();
        body.setPadding(new Insets(10, 10, 10, 10));

        notification = new Text("");
        body.add(notification, 0, 0);

        tempLabel = new Text("Temperatuur:");
        brightLabel = new Text("Helderheid:");

        currentTemp = new Text(String.valueOf(Account.getTemperatureVK()));
        currentBright = new Text(String.valueOf(Account.getLichtVK()));

        newTemp = new TextField();
        newBright = new TextField();

        body.addRow(1, tempLabel, currentTemp, newTemp);
        body.addRow(2, brightLabel, currentBright, newBright);

        explanation = new Text("Voer de temperatuur of helderheid in waarbij u wilt dat de verwarming of verlichting wordt ingeschakeld");
        body.add(explanation, 0, 3);
    }

    public BorderPane getPane() {
        return borderPane;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == saveButton) {
            boolean geupdate = false;
            try {
                String newTempText = newTemp.getText();
                String newBrightText = newBright.getText();

                if (!(newTempText.equals(""))) {
                    float newTempDouble = Float.valueOf(newTempText);
                    geupdate = Account.updatePref("temperatuurVK", newTempDouble);
                }

                if (!(newBrightText.equals(""))) {
                    float newBrightDouble = Float.valueOf(newBrightText);
                    geupdate = Account.updatePref("lichtVK", newBrightDouble);
                }

                if (geupdate) {
                    notification.setText("Wijziging is geslaagd!");
                    leftScreen.showPreference();
                } else {
                    notification.setText("Wijziging is mislukt");
                }
            } catch (NumberFormatException nfe) {
                notification.setText("Voer een geldige waarde in!");
            }
        }
    }
}
