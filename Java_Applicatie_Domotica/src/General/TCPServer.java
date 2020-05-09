package General;

import Domotica.Functionality.Meting;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class TCPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int port;

    public TCPServer(int port) throws IOException {
        this.port = port;
        start(port);
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        try {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println(clientSocket.getLocalPort() + " " + clientSocket.getInetAddress());
        } catch (SocketException se) {
            System.out.println(se.getMessage());
        }
    }

    public String read() throws IOException {
        long startTime = System.nanoTime() / 1000000;
        long currentTime = System.nanoTime() / 1000000;

        while (currentTime - startTime < 500) {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = in.readLine();

            if (message != null) {

                if (message.equals("exit")) {
                    stop(true);
                    return null;
                }

                return message;
            }

            currentTime = System.nanoTime() / 1000000;
        }
        return null;
    }

    public void write(String message) {
        System.out.println(message);
        out.println(message);
        out.flush();
    }

    public void getMeasurements(ArrayList<Meting> measurements) throws IOException, NullPointerException {
        write("gt");
        measurements.get(0).setWaarde(Float.parseFloat(Objects.requireNonNull(read())));

        write("gh");
        measurements.get(1).setWaarde(Float.parseFloat(Objects.requireNonNull(read())));

        write("gp");
        measurements.get(2).setWaarde(Float.parseFloat(Objects.requireNonNull(read())));
    }

    public void stop(boolean start) throws IOException, NullPointerException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
        if (serverSocket != null) serverSocket.close();

        if (start == true) start(port);
    }

    public OutputStream getOutputStream() throws IOException {
        return clientSocket.getOutputStream();
    }
}