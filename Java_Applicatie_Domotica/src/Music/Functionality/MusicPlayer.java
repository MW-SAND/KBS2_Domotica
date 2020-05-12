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

    private ArrayList<Song> playlist;
    private ArrayList<Song> playedSongs;

    private int songIndex;

    private boolean newMessage;
    private String message;

    public MusicPlayer(ArrayList<Button> buttons) {
        this.buttons = buttons;
        running = true;
        newMessage = false;

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

        playedSongs = new ArrayList<>();
    }

    public void run() {
        try {
            tcpServer = new TCPServer(6370);
            System.out.println("connected");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (running) {
            if (newMessage == true) {
                if (message.equals("New song")) {

                } else if (message.equals("Previous song")) {

                } else if (message.equals("Next song")) {

                } else if (message.equals("Pause song")) {
                    try {
                        tcpServer.write("ps");
                        tcpServer.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (message.equals("Resume song")) {
                    try {
                        tcpServer.write("rs");
                        tcpServer.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
        newMessage = true;

        if (actionEvent.getSource() == buttons.get(0)) {
            message = "New song";

        } else if (actionEvent.getSource() == buttons.get(1)) {
            message = "Previous song";
        } else if (actionEvent.getSource() == buttons.get(2)) {
            message = "Pause song";
        } else if (actionEvent.getSource() == buttons.get(3)) {
            message = "Resume song";
        } else if (actionEvent.getSource() == buttons.get(4)) {
            message = "Next song";
        } else {
            message = "unknown";
        }
    }
}
