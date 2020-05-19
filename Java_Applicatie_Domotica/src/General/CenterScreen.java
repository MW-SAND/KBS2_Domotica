package General;

import Domotica.GUI.LogPane;
import Domotica.GUI.PreferencePane;
import Domotica.GUI.GraphPane;
import Music.GUI.MusicPane;
import javafx.scene.layout.*;

public class CenterScreen {
    private BorderPane centerPane;
    private GraphPane graphPane;
    private PreferencePane prefPane;
    private LogPane logPane;
    private MusicPane musicPane;

    public CenterScreen(DomApplication domApplication) {
        centerPane = new BorderPane();
        centerPane.setMinSize(1200, 800);

        // maakt mogelijke schermen alvast aan
        graphPane = new GraphPane();
        prefPane = new PreferencePane(this, domApplication);
        logPane = new LogPane();

        centerPane.setCenter(prefPane.getPane());

        musicPane = new MusicPane();
        centerPane.setBottom(musicPane.getPane());
    }

    public BorderPane getCenterPane() {
        return centerPane;
    }

    // toont verschillende schermen afhankelijk van de knop
    public void showPane(String paneName) {
        if (paneName.equals("Voorkeuren")) {
            centerPane.setCenter(prefPane.getPane());
        } else if (paneName.equals("Metingen")) {
            centerPane.setCenter(graphPane.getPane());
        } else if (paneName.equals("Logging")) {
            centerPane.setCenter(logPane.getPane());
        }
    }

}
