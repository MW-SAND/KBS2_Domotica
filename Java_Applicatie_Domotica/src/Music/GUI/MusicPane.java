package Music.GUI;

import Music.Functionality.MusicPlayer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.swing.*;
import java.util.ArrayList;

public class MusicPane implements EventHandler<ActionEvent> {
    private Button bPlay;
    private Button bResume;
    private Button bPause;
    private Button bNext;
    private Button bPrevious;

    private BorderPane borderPane;
    private VBox leftBox;
    private HBox centerBox;

    private Text tArtist;
    private Text tTitle;

    private MusicPlayer musicPlayer;

    public MusicPane() {
        borderPane = new BorderPane();

        borderPane.setPrefSize(1200, 200);

        bPlay = new Button("Play");
        bResume = new Button("Resume");
        bPause = new Button("Pause");
        bNext = new Button("Next");
        bPrevious = new Button("Previous");

        tTitle = new Text("Title");
        tArtist = new Text("Artist");

        leftBox = new VBox();
        leftBox.getChildren().addAll(tTitle, tArtist);

        borderPane.setLeft(leftBox);

        centerBox = new HBox();
        centerBox.getChildren().addAll(bPrevious, bPause, bResume, bNext, bPlay);

        borderPane.setCenter(centerBox);

        musicPlayer = new MusicPlayer();

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
}
