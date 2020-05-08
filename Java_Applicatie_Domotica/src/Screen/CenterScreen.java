package Screen;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

public class CenterScreen {
    private BorderPane centerPane;
    private GraphPane graphPane;
    private PreferencePane prefPane;

    public CenterScreen(DomApplication domApplication) {
        centerPane = new BorderPane();
        centerPane.setMinSize(1200, 800);

        graphPane = new GraphPane();
        prefPane = new PreferencePane(this, domApplication);

        centerPane.setCenter(prefPane.getPane());

        Label bottomLabel = createLabel("BOTTOM", Paint.valueOf("ff0000"));
        bottomLabel.setPrefSize(1200, 300);
        centerPane.setBottom(bottomLabel);
    }

    public Label createLabel(String text, javafx.scene.paint.Paint styleColor) {
        Label label = new Label(text);
        label.setBackground(new Background(new BackgroundFill(styleColor, CornerRadii.EMPTY, Insets.EMPTY)));
        return label;
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
