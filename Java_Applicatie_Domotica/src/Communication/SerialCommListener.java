package Communication;

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

    private Communication communicator;

    public SerialCommListener(Communication communicator) {
        this.communicator = communicator;

        serialport = SerialPort.getCommPort("COM3");
        serialport.openPort();
        serialport.setBaudRate(9600);
        serialport.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

        System.out.println(serialport.isOpen());

        inputStream = serialport.getInputStream();
        outputStream = serialport.getOutputStream();

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

    /*    byte[] newData = new byte[serialport.bytesAvailable()];
        int numRead = serialport.readBytes(newData, newData.length);
        System.out.println("Read " + numRead + " bytes."); */
        byte[] newDataB = new byte[5];
        inputStream.read(newDataB, 0, 5);

        String newDataS = new String(newDataB).trim();
        communicator.setBrightness(newDataS);
    }

    public void writeData(String message) throws IOException {
        outputStream.write(message.getBytes());
        outputStream.flush();
    }
}
