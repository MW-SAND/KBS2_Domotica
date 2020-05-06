package Communication;

import Screen.LeftScreen;
import User.Account;

import java.io.IOException;
import java.util.ArrayList;

public class Communication extends Thread {
    private LeftScreen screen;
    private ArrayList<Meting> measurements;
    private SerialCommListener serialComm;
    private boolean verlichting;
    private boolean verwarming;

    public Communication(LeftScreen screen) {
        this.screen = screen;
        measurements = new ArrayList<Meting>();
        measurements.add(new Meting("temperature"));
        measurements.add(new Meting("humidity"));
        measurements.add(new Meting("pressure"));
        measurements.add(new Meting("brightness"));

        verwarming = false;
        verlichting = false;
    }

    public void run() {
        createConnection();

        serialComm = new SerialCommListener(this);

        while (true) {
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
            measurements = ServerHost.getMeasurements(measurements);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            try {
                ServerHost.stop();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void createConnection() {
        ServerHost server = new ServerHost();

        try {
            server.start(6369);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBrightness(String brightness) {
        measurements.get(3).setWaarde(Float.valueOf(brightness));
    }

    public String buildQuery() {
        String query = "INSERT INTO metingen (waarde, grootheid) VALUES ";

        for (int i = 0; i < 4; i++) {
            String naam = measurements.get(i).getNaam();
            float waarde = measurements.get(i).getWaarde();

            if (waarde != 0) {
                if (i > 0) query += ", ";

                query += "(" + waarde + ", '" + naam + "')";
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
                    ServerHost.write("uv");
                    System.out.println(ServerHost.read());
                    verlichting = false;
                }
            } else if (measurements.get(0).getWaarde() < temperaturePref) {
                ServerHost.write("sv");
                verwarming = true;
                System.out.println(ServerHost.read());
            }
        }

        if (lightPref != 0.0) {
            if (verlichting) {
                if (measurements.get(3).getWaarde() > lightPref * 1.10) {
                    ServerHost.write("ul");
                    System.out.println(ServerHost.read());
                    verlichting = false;
                }
            } else if (measurements.get(3).getWaarde() < lightPref && measurements.get(3).getWaarde() != 0.0) {
                ServerHost.write("sl");
                verlichting = true;
                System.out.println(ServerHost.read());
            }
        }

        screen.showSystemStatus(verwarming, verlichting);
    }
}
