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
    private boolean running;
    private boolean newMusicFile;

    public MusicPlayer(ArrayList<Button> buttons) {
        this.buttons = buttons;
        running = true;
        newMusicFile = false;
    }

    public void run() {
        try {
            tcpServer = new TCPServer(6370);
            System.out.println("connected");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (running) {
            if (newMusicFile == true) {
                sendMusicFile();
                newMusicFile = false;
            }

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMusicFile() {
        tcpServer.write("mf");
        File musicFile = new File("C:\\Users\\matti\\Downloads\\Living Hope.wav");
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
            newMusicFile = true;
        }
    }
}
