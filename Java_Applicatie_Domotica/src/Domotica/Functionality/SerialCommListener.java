package Domotica.Functionality;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.Thread.sleep;

public class SerialCommListener {
    SerialPort serialport;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Communicator communicator;

    public SerialCommListener(Communicator communicator) {
        this.communicator = communicator;

        // maakt de connectie met de Arduino
        serialport = SerialPort.getCommPort("COM3");
        serialport.openPort();
        serialport.setBaudRate(9600);
        serialport.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        inputStream = serialport.getInputStream();
        outputStream = serialport.getOutputStream();

        // er wordt geluisterd naar binnenkomende berichten
        serialport.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;

                try {
                    readData();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void readData() throws IOException {
        try {
            sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // er worden 5 bytes gelezen en de informatie wordt verwerkt
        byte[] newDataB = new byte[5];
        inputStream.read(newDataB, 0, 5);

        String newDataS = new String(newDataB).trim();
        communicator.setBrightness(newDataS);
    }

    // zendt data naar Arduino
    public void writeData(String message) throws IOException {
        outputStream.write(message.getBytes());
        outputStream.flush();
    }

    // sluit de poort
    public void closePort() throws IOException {
        serialport.closePort();
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
