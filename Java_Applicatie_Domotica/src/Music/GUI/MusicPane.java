package Music.GUI;

import Music.Functionality.MusicPlayer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPane implements EventHandler<ActionEvent> {
    private Button bPlay;
    private Button bResume;
    private Button bPause;
    private Button bNext;
    private Button bPrevious;

    private BorderPane borderPane;
    private VBox leftBox;
    private VBox centerBox;
    private HBox controls;

    private Text tArtist;
    private Text tTitle;

    private Text tRemainingTime;
    private Text tTotalDuration;
    private ProgressBar songProgress;

    private MusicPlayer musicPlayer;

    public MusicPane() {
        borderPane = new BorderPane();

        borderPane.setPrefSize(1200, 200);

        // knoppen voor controle over de muziek
        bPlay = new Button("Play");
        bResume = new Button("Resume");
        bPause = new Button("Pause");
        bNext = new Button("Next");
        bPrevious = new Button("Previous");

        // muziekinformatie
        tTitle = new Text("Title");
        tArtist = new Text("Artist");

        tRemainingTime = new Text("-");
        tTotalDuration = new Text("-");

        leftBox = new VBox();
        leftBox.getChildren().addAll(tTitle, tArtist, tTotalDuration, tRemainingTime);

        borderPane.setLeft(leftBox);

        // toont muziekvoortgang met een progresbar
        songProgress = new ProgressBar();
        songProgress.setProgress(0);

        controls = new HBox();
        controls.getChildren().addAll(bPrevious, bPause, bResume, bNext, bPlay);

        songProgress.prefWidthProperty().bind(controls.widthProperty());

        centerBox = new VBox();
        centerBox.getChildren().addAll(controls, songProgress);

        borderPane.setCenter(centerBox);

        // maakt een muziekspeler aan
        musicPlayer = new MusicPlayer(this);

        bPlay.setOnAction(this);
        bPrevious.setOnAction(this);
        bPause.setOnAction(this);
        bResume.setOnAction(this);
        bNext.setOnAction(this);

        musicPlayer.start();
    }

    public BorderPane getPane() {
        return borderPane;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // verzend de knop opdracht naar de muziekspeler
        musicPlayer.setNewMessage(true);

        if (actionEvent.getSource() == bPlay) {
            musicPlayer.setMessage("New song");
        } else if (actionEvent.getSource() == bPrevious) {
            musicPlayer.setMessage("Previous song");
        } else if (actionEvent.getSource() == bPause) {
            musicPlayer.setMessage("Pause song");
        } else if (actionEvent.getSource() == bResume) {
            musicPlayer.setMessage("Resume song");
        } else if (actionEvent.getSource() == bNext) {
            musicPlayer.setMessage("Next song");
        }
    }

    public void setTime(int duration, int timePlaying) {
        int remainingTime = duration - timePlaying;
        int minutes;
        String format = ":";

        // rekent progressie uit en toont deze
        double progress = ((double) timePlaying) / ((double) duration);
        songProgress.setProgress(progress);

        if (remainingTime > 0) {
            // format de tijd naar minuut:secondes
            minutes = (int) TimeUnit.SECONDS.toMinutes(remainingTime);
            remainingTime -= TimeUnit.MINUTES.toSeconds(minutes);
            if (remainingTime < 10) format = ":0";
            tRemainingTime.setText(minutes + format + remainingTime);
        }

        // format de tijd naar minuut:secondes
        format = ":";
        minutes = (int) TimeUnit.SECONDS.toMinutes(duration);
        duration -= TimeUnit.MINUTES.toSeconds(minutes);
        if (duration < 10) format = ":0";
        tTotalDuration.setText(minutes + format + duration);
    }

    // past informatie over het nummer aan
    public void setSongInformation(String title, String artist)  {
        tTitle.setText(title);
        tArtist.setText(artist);
    }
}
