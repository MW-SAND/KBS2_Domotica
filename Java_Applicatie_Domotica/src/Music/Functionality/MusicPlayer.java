package Music.Functionality;

import General.TCPServer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.io.*;
import java.util.ArrayList;

public class MusicPlayer extends Thread implements EventHandler<ActionEvent> {
    private TCPServer tcpServer;
    private ArrayList<Button> buttons;
    private boolean running = true;

    public MusicPlayer(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }

    public void run() {
        try {
            tcpServer = new TCPServer(6370);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (running) {

        }
    }

    public void sendMusicFile() {
        tcpServer.write("mf");
        File musicFile = new File("C:\\Users\\matti\\Downloads\\Jezus Overwinnaar.wav");
        byte[] musicFileArray = new byte[(int) musicFile.length()];
        tcpServer.write(String.valueOf(musicFileArray.length));
        try {
            FileInputStream fis = new FileInputStream(musicFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(musicFileArray, 0, musicFileArray.length);
            OutputStream out = tcpServer.getOutputStream();
            out.write(musicFileArray, 0, musicFileArray.length);
            out.flush();
            System.out.println(tcpServer.read());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttons.get(0)) {
            if (tcpServer != null) sendMusicFile();
        }
    }
}
