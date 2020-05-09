package General;

import Domotica.GUI.PreferencePane;
import Domotica.GUI.GraphPane;
import Music.GUI.MusicPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class CenterScreen {
    private BorderPane centerPane;
    private GraphPane graphPane;
    private PreferencePane prefPane;
    private MusicPane musicPane;

    public CenterScreen(DomApplication domApplication) {
        centerPane = new BorderPane();
        centerPane.setMinSize(1200, 800);

        graphPane = new GraphPane();
        prefPane = new PreferencePane(this, domApplication);

        centerPane.setCenter(prefPane.getPane());

        musicPane = new MusicPane();
        centerPane.setBottom(musicPane.getPane());
    }

    public BorderPane getCenterPane() {
        return centerPane;
    }

    public void showPane(String paneName) {
        if (paneName.equals("Voorkeuren")) {
            centerPane.setCenter(prefPane.getPane());
        } else if (paneName.equals("Metingen")) {
            centerPane.setCenter(graphPane.getPane());
        }
    }

}
