package Music.GUI;

import Music.Functionality.MusicPlayer;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MusicPane {
    private Button bPlay;
    private BorderPane borderPane;
    private Text tArtist;
    private Text tTitle;
    private VBox leftBox;
    private MusicPlayer musicPlayer;
    private ArrayList<Button> buttons;

    public MusicPane() {
        borderPane = new BorderPane();

        borderPane.setPrefSize(1200, 200);

        bPlay = new Button("Play");

        tTitle = new Text("Title");
        tArtist = new Text("Artist");

        leftBox = new VBox();
        leftBox.getChildren().addAll(tTitle, tArtist);

        borderPane.setLeft(leftBox);
        borderPane.setCenter(bPlay);

        buttons = new ArrayList<>();
        buttons.add(bPlay);

        musicPlayer = new MusicPlayer(buttons);
        bPlay.setOnAction(musicPlayer);
    }

    public BorderPane getPane() {
        return borderPane;
    }
}
