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
    private ArrayList<Song> playlist;
    private int songIndex;

    public MusicPlayer(ArrayList<Button> buttons) {
        this.buttons = buttons;
        running = true;
        newMusicFile = false;

        songIndex = 0;

        Song song1 = new Song("Dwayne Tryumf", "777 intro", 96, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\777intro.mp3");
        Song song2 = new Song("Dwayne Tryumf", "I don't pack a Matic", 249, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\Matic.mp3");
        Song song3 = new Song("Lecrae", "Don't Waste Your Life", 229, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\Waste.mp3");
        Song song4 = new Song("KB", "Champion", 269, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\Champion.mp3");

        playlist = new ArrayList<Song>();
        playlist.add(song1);
        playlist.add(song2);
        playlist.add(song3);
        playlist.add(song4);
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
        byte[] musicFileArray = playlist.get(songIndex).getSongBytes();
        tcpServer.write(String.valueOf(musicFileArray.length));
        try {
            System.out.println(tcpServer.read());
            OutputStream out = tcpServer.getOutputStream();
            out.write(musicFileArray, 0, musicFileArray.length);
            out.flush();
            System.out.println(tcpServer.read());
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
