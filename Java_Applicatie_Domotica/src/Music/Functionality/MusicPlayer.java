package Music.Functionality;

import General.TCPServer;
import Music.GUI.MusicPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.io.*;
import java.util.ArrayList;

public class MusicPlayer extends Thread {
    private TCPServer tcpServer;
    private MusicPane musicPane;

    private boolean running;

    private ArrayList<Song> playlist;
    private ArrayList<Song> playedSongs;

    private int songIndex;
    private int currentSong;

    private boolean newMessage;
    private String message;

    public MusicPlayer(MusicPane musicPane) {
        this.musicPane = musicPane;
        running = true;
        newMessage = false;
        message = "unknown";

        songIndex = 0;
        currentSong = 0;

        Song song1 = new Song("Dwayne Tryumf", "777 intro", 96, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\777intro.mp3");
        Song song2 = new Song("Dwayne Tryumf", "I don't pack a Matic", 249, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\Matic.mp3");
        Song song3 = new Song("Lecrae", "Don't Waste Your Life", 229, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\Waste.mp3");
        Song song4 = new Song("KB", "Champion", 269, "C:\\Users\\matti\\Downloads\\KBS_TestNummers\\Champion.mp3");

        playlist = new ArrayList<>();
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
                newMessage = false;

                if (message.equals("New song")) {
                    songIndex = 0;
                    tcpServer.write("mf");
                    sendMusicFile(playlist.get(songIndex));

                    playedSongs.add(playlist.get(songIndex));
                    currentSong = playedSongs.size() - 1;

                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    tcpServer.write("nf");

                    if (songIndex >= playlist.size() - 1) {
                        sendMusicFile(playlist.get(0));
                    } else {
                        sendMusicFile(playlist.get(songIndex+1));
                    }

                } else if (message.equals("Previous song")) {
                        if (playedSongs.size() > 0 && currentSong != 0) {
                            tcpServer.write("mf");
                            currentSong--;
                            sendMusicFile(playedSongs.get(currentSong));

                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            tcpServer.write("nf");

                            sendMusicFile(playedSongs.get(currentSong+1));
                        }
                } else if (message.equals("Next song")) {
                    if (currentSong >= playedSongs.size() - 1) {
                        if (songIndex + 1 >= playlist.size()) {
                            songIndex = 0;
                        } else {
                            songIndex++;
                        }

                        tcpServer.write("ns");
                        currentSong++;
                        playedSongs.add(playlist.get(songIndex));

                        try {
                            String answer = tcpServer.read();
                            if (answer.equals("sr")) {
                                tcpServer.write("mf");
                                sendMusicFile(playlist.get(songIndex));
                            }

                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            tcpServer.write("nf");

                            if (songIndex >= playlist.size() - 1) {
                                sendMusicFile(playlist.get(0));
                            } else {
                                sendMusicFile(playlist.get(songIndex+1));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        currentSong++;

                        tcpServer.write("ns");

                        try {
                            String answer = tcpServer.read();
                            if (answer.equals("sr")) {
                                tcpServer.write("mf");
                                sendMusicFile(playedSongs.get(currentSong));
                            }

                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            tcpServer.write("nf");

                            if (currentSong >= playlist.size() - 1) {
                                if (songIndex >= playlist.size()) {
                                    sendMusicFile(playlist.get(0));
                                } else {
                                    sendMusicFile(playlist.get(songIndex+1));
                                }
                            } else {
                                sendMusicFile(playedSongs.get(currentSong+1));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (message.equals("Pause song")) {
                    try {
                        System.out.println("Pausing song...");
                        tcpServer.write("ps");
                        tcpServer.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (message.equals("Resume song")) {
                    try {
                        System.out.println("Resuming song...");
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

    public void sendMusicFile(Song song) {
        byte[] musicFileArray = song.getSongBytes();
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

    public void setNewMessage(boolean newMessage) {
        this.newMessage = newMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

