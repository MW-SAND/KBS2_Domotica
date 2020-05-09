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
    private ArrayList<Meting> measurements;
    private SerialCommListener serialComm;
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
        try {
            tcpServer = new TCPServer(6369);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serialComm = new SerialCommListener(this);

        while (running) {
            updateMeasurements();

            screen.showMeasurements(measurements);

            String query = buildQuery();
            System.out.println(Database.executeUpdate(query));

            try {
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

    public void setBrightness(String brightness) {
        measurements.get(3).setWaarde(Float.parseFloat(brightness));
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
                if (measurements.get(0).getWaarde() > temperaturePref * 1.10) {
                    tcpServer.write("uv");
                    System.out.println(tcpServer.read());
                    verlichting = false;
                }
            } else if (measurements.get(0).getWaarde() < temperaturePref) {
                tcpServer.write("sv");
                verwarming = true;
                System.out.println(tcpServer.read());
            }
        }

        if (lightPref != 0.0) {
            if (verlichting) {
                if (measurements.get(3).getWaarde() > lightPref * 1.10) {
                    tcpServer.write("ul");
                    System.out.println(tcpServer.read());
                    verlichting = false;
                }
            } else if (measurements.get(3).getWaarde() < lightPref && measurements.get(3).getWaarde() != 0.0) {
                tcpServer.write("sl");
                verlichting = true;
                System.out.println(tcpServer.read());
            }
        }

        screen.showSystemStatus(verwarming, verlichting);
    }

    public void closeConnections() {
        try {
            if (tcpServer != null) tcpServer.stop(false);
            if (serialComm != null) serialComm.closePort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        running = false;

        try {
            sleep(500);
            closeConnections();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
