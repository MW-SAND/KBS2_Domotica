package Domotica.Functionality;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogLine {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private String action;
    private String description;
    private Date date;
    private String songInfo;

    public LogLine(String action, String description, String date, String songInfo) {
        this.action = action;
        this.description = description;
        this.songInfo = songInfo;

        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
            this.date = null;
        }
    }

    // getters worden automatisch gebruikt door de tabel in LogPane
    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getSongInfo() {
        return songInfo;
    }
}
