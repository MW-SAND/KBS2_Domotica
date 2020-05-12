package Music.GUI;

import Music.Functionality.MusicPlayer;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MusicPane {
    private ArrayList<Button> buttons;
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

        buttons = new ArrayList<>();
        buttons.add(bPlay);
        buttons.add(bPrevious);
        buttons.add(bPause);
        buttons.add(bResume);
        buttons.add(bNext);

        musicPlayer = new MusicPlayer(buttons);

        bPlay.setOnAction(musicPlayer);
        bPrevious.setOnAction(musicPlayer);
        bPause.setOnAction(musicPlayer);
        bResume.setOnAction(musicPlayer);
        bNext.setOnAction(musicPlayer);

        musicPlayer.start();
    }

    public BorderPane getPane() {
        return borderPane;
    }
}
