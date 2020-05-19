package Domotica.Functionality;

import General.TCPServer;
import General.Database;
import Domotica.GUI.LeftScreen;
import Authentication.Account;

import java.io.*;
import java.util.ArrayList;

public class Communicator extends Thread {
    private LeftScreen screen;

    private TCPServer tcpServer;
    private SerialCommListener serialComm;

    private ArrayList<Meting> measurements;

    // variabele houdt bij of het systeem aanstaat
    private boolean verlichting;
    private boolean verwarming;

    private boolean running;

    public Communicator(LeftScreen screen) {
        this.screen = screen;
        measurements = new ArrayList<>();
        measurements.add(new Meting("temperature"));
        measurements.add(new Meting("humidity"));
        measurements.add(new Meting("pressure"));
        measurements.add(new Meting("brightness"));

        verwarming = false;
        verlichting = false;
        running = true;
    }

    public void run() {
        // aanmaken verbinding met RPi en Arduino
        try {
            tcpServer = new TCPServer(6369);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serialComm = new SerialCommListener(this);

        while (running) {
            // ophalen en tonen nieuwe metingen
            updateMeasurements();
            screen.showMeasurements(measurements);

            // opslaan metingen
            String query = buildQuery();
            System.out.println(Database.executeUpdate(query));

            try {
                // metingen vergelijken met voorkeur
                controlThreshold();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateMeasurements() {
        // versturen vraag voor metingen. Bij de RPi wordt er op antwoord gewacht.
        try {
            serialComm.writeData("gb");
            tcpServer.getMeasurements(measurements);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            try {
                tcpServer.stop(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String buildQuery() {
        StringBuilder query = new StringBuilder("INSERT INTO metingen (waarde, grootheid) VALUES ");

        for (int i = 0; i < 4; i++) {
            String naam = measurements.get(i).getNaam();
            float waarde = measurements.get(i).getWaarde();

            if (waarde != 0) {
                if (i > 0) query.append(", ");

                query.append("(").append(waarde).append(", '").append(naam).append("')");
            }
        }

        return query + ";";
    }

    public void controlThreshold() throws IOException {
        float temperaturePref = Account.getTemperatureVK();
        float lightPref = Account.getLichtVK();

        if (temperaturePref != 0.0) {
            if (verwarming) {
                // zet verwarming uit als de temperatuur 110% van de voorkeur komt.
                if (measurements.get(0).getWaarde() > temperaturePref * 1.10) {
                    tcpServer.write("uv");
                    System.out.println(tcpServer.read());
                    verwarming = false;

                    // update het log
                    Database.updateLog("Update extern systeem", "Verwarming uitgeschakeld", null);
                }
                // zet de verwarming aan als de temperatuur onder de voorkeur komt.
            } else if (measurements.get(0).getWaarde() < temperaturePref) {
                tcpServer.write("sv");
                System.out.println(tcpServer.read());
                verwarming = true;

                // update het log
                Database.updateLog("Update extern systeem", "Verwarming ingeschakeld", null);
            }
        }

        if (lightPref != 0.0) {
            if (verlichting) {
                // zet verlichting uit als de helderheid 110% van de voorkeur komt.
                if (measurements.get(3).getWaarde() > lightPref * 1.10) {
                    tcpServer.write("ul");
                    System.out.println(tcpServer.read());
                    verlichting = false;

                    // update het log
                    Database.updateLog("Update extern systeem", "Verlichting uitgeschakeld", null);
                }
                // zet verwarming aan als de helderheid onder de voorkeur komt.
            } else if (measurements.get(3).getWaarde() < lightPref && measurements.get(3).getWaarde() != 0.0) {
                tcpServer.write("sl");
                System.out.println(tcpServer.read());
                verlichting = true;

                // update het log
                Database.updateLog("Update extern systeem", "Verlichting ingeschakeld", null);

            }
        }

        screen.showSystemStatus(verwarming, verlichting);
    }

    public void closeConnections() {
        // sluit de verbindingen
        try {
            if (tcpServer != null) tcpServer.stop(false);
            if (serialComm != null) serialComm.closePort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        // stopt het proces
        running = false;

        try {
            sleep(500);
            closeConnections();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setBrightness(String brightness) {
        measurements.get(3).setWaarde(Float.parseFloat(brightness));
    }
}
