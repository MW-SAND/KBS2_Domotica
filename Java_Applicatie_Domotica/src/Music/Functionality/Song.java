package Music.Functionality;

import java.io.*;

public class Song {
    private String artist;
    private String title;
    private int playDuration;
    private byte[] songBytes;
    private Integer databaseID;

    public Song(String artist, String title, int playDuration, String path, Integer databaseID) {
        this.artist = artist;
        this.title = title;
        this.playDuration = playDuration;
        this.databaseID = databaseID;

        try {
            File musicFile = new File(path);
            songBytes = new byte[(int) musicFile.length()];
            FileInputStream fis = new FileInputStream(musicFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(songBytes, 0, songBytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public byte[] getSongBytes() {
        return songBytes;
    }

    public Integer getDatabaseID() {
        return databaseID;
    }
}
