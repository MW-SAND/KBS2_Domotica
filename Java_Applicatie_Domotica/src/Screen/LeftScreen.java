package Screen;

import Communication.Communicator;
import Communication.Database;
import Communication.Meting;
import User.Account;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class LeftScreen implements EventHandler<ActionEvent> {
    private GridPane LeftPane;
    private DomApplication domApplication;
    private CenterScreen centerScreen;
    private Communicator communicator;

    private Text tfSystemTitle;
    private Text tfHeaterTitle;
    private Text tfHeaterStatus;
    private Text tfLightingTitle;
    private Text tfLightingStatus;

    private Text tfPreferenceTitle;
    private Text tfTemperaturePreferenceTitle;
    private Text tfTemperaturePreferenceSize;
    private Text tfLightPreferenceTitle;
    private Text tfLightPreferenceSize;

    private Text tfMeasurementTitle;
    private Text tfTemperatureTitle;
    private Text tfTemperatureMeasurement;
    private Text tfLightTitle;
    private Text tfLightMeasurement;
    private Text tfHumidityTitle;
    private Text tfHumidityMeasurement;
    private Text tfPressureTitle;
    private Text tfPressureMeasurement;

    private Button showMetingen;
    private Button showPref;
    private Button logout;


    public LeftScreen(DomApplication domApplication) {
        this.domApplication = domApplication;

        communicator = new Communicator(this);
        communicator.start();

        LeftPane = new GridPane();
        LeftPane.setMinSize(300, 800);
        LeftPane.setPadding(new Insets(10, 10, 10, 10));

        LeftPane.setVgap(5);
        LeftPane.setHgap(5);

        addTiles();
    }

    public void showMeasurements(ArrayList<Meting> measurements) {
        String[] measurementString = new String[4];
        for (int i = 0; i < 4; i++) {
            measurementString[i] = String.valueOf(measurements.get(i).getWaarde());
        }
        try {
            tfTemperatureMeasurement.setText(measurementString[0].substring(0, 4) + "   °C");
            tfHumidityMeasurement.setText(measurementString[1].substring(0, 4) + "   g/m3");
            tfPressureMeasurement.setText(measurementString[2].substring(0, 6) + "   Pa");
            tfLightMeasurement.setText(measurementString[3] + "   lm");
        } catch (StringIndexOutOfBoundsException sioobe) {
            System.out.println(sioobe.getMessage());
        }
    }

    private void addTiles() {
        tfSystemTitle = new Text("Systeem");
        tfHeaterTitle = new Text("Verwarming");
        tfLightingTitle = new Text("Verlichting");

        tfHeaterStatus = new Text();
        tfLightingStatus = new Text();

        LeftPane.add(tfSystemTitle, 0, 0);
        LeftPane.add(tfHeaterTitle, 0, 1);
        LeftPane.add(tfHeaterStatus, 1, 1);
        LeftPane.add(tfLightingTitle, 0, 2);
        LeftPane.add(tfLightingStatus, 1, 2);

        tfPreferenceTitle = new Text("Voorkeuren");
        tfTemperaturePreferenceTitle = new Text("Temperatuur");
        tfLightPreferenceTitle = new Text("Licht");

        Account.getPref();

        tfTemperaturePreferenceSize = new Text(String.valueOf(Account.getTemperatureVK()));
        tfLightPreferenceSize = new Text(String.valueOf(Account.getLichtVK()));

        LeftPane.add(tfPreferenceTitle, 0, 4);
        LeftPane.add(tfTemperaturePreferenceTitle, 0, 5);
        LeftPane.add(tfTemperaturePreferenceSize, 1, 5);
        LeftPane.add(tfLightPreferenceTitle, 0, 6);
        LeftPane.add(tfLightPreferenceSize, 1, 6);

        tfMeasurementTitle = new Text("Meetgegevens");
        tfTemperatureTitle = new Text("Temperatuur");
        tfHumidityTitle = new Text("Luchtvochtigheid");
        tfPressureTitle = new Text("Luchtdruk");
        tfLightTitle = new Text("Helderheid");

        tfTemperatureMeasurement = new Text();
        tfHumidityMeasurement = new Text();
        tfPressureMeasurement = new Text();
        tfLightMeasurement = new Text();

        LeftPane.add(tfMeasurementTitle, 0, 8);
        LeftPane.add(tfTemperatureTitle, 0, 9);
        LeftPane.add(tfTemperatureMeasurement, 1, 9);
        LeftPane.add(tfHumidityTitle, 0, 10);
        LeftPane.add(tfHumidityMeasurement, 1, 10);
        LeftPane.add(tfPressureTitle, 0, 11);
        LeftPane.add(tfPressureMeasurement, 1, 11);
        LeftPane.add(tfLightTitle, 0, 12);
        LeftPane.add(tfLightMeasurement, 1, 12);

        showMetingen = new Button("Metingen");
        showPref = new Button("Voorkeur");

        showMetingen.setOnAction(this);
        showPref.setOnAction(this);

        LeftPane.add(showMetingen, 1, 8);
        LeftPane.add(showPref, 1, 4);

        logout = new Button("uitloggen");
        logout.setOnAction(this);
        LeftPane.add(logout, 0, 13);
    }

    public GridPane getLeftPane() {
        return LeftPane;
    }

    public void setCenterScreen(CenterScreen centerScreen) {
        this.centerScreen = centerScreen;
    }

    public void showSystemStatus(boolean heater, boolean light) {
        if (heater) {
            tfHeaterStatus.setText("Aan");
        } else {
            tfHeaterStatus.setText("Uit");
        }

        if (light) {
            tfLightingStatus.setText("Aan");
        } else {
            tfLightingStatus.setText("Uit");
        }
    }

    public void showPreference() {
        tfTemperaturePreferenceSize.setText(String.valueOf(Account.getTemperatureVK()));
        tfLightPreferenceSize.setText(String.valueOf(Account.getLichtVK()));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource() == showPref) {
            centerScreen.showPane("Voorkeuren");
        } else if (actionEvent.getSource() == showMetingen) {
            centerScreen.showPane("Metingen");
        } else if (actionEvent.getSource() == logout) {
            communicator.terminate();
            Database.executeUpdate("UPDATE account SET active=0 WHERE id=" + Account.getAccountid() + ";");
            domApplication.showLogin();
        }
    }

    public Communicator getCommunicator() {
        return communicator;
    }
}
