package Communication;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerHost {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println(clientSocket.getLocalPort() + " " + clientSocket.getInetAddress());
    }

    public static String read() throws IOException {
        long startTime = System.nanoTime() / 1000000;
        long currentTime = System.nanoTime() / 1000000;

        while (currentTime - startTime < 500) {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();

            if (message != null) {

                if (message.equals("exit")) {
                    stop();
                    return null;
                }

                return message;
            }

            currentTime = System.nanoTime() / 1000000;
        }
        return null;
    }

    public static void write(String message) {
        out.println(message);
        out.flush();
    }

    public static ArrayList<Meting> getMeasurements(ArrayList<Meting> measurements) throws IOException, NullPointerException {
        write("gt");
        measurements.get(0).setWaarde(Float.valueOf(read()));

        write("gh");
        measurements.get(1).setWaarde(Float.valueOf(read()));

        write("gp");
        measurements.get(2).setWaarde(Float.valueOf(read()));

        return measurements;
    }

    public static void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();

        start(6369);
    }
}