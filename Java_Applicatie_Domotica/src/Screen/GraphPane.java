package Screen;

import Communication.Database;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GraphPane implements EventHandler<ActionEvent> {
    private BorderPane borderPane;
    private HBox menu;
    private ToggleButton temperatuur;
    private ToggleButton luchtvochtigheid;
    private ToggleButton luchtdruk;
    private ToggleButton helderheid;

    public GraphPane() {
        borderPane = new BorderPane();
        createMenu();
        borderPane.setTop(menu);

        newGraph("temperature");
    }

    public void createMenu() {
        menu = new HBox();

        Button returnButton = new Button("<");
        Text titel = new Text("Metingen");

        ToggleGroup grootheden = new ToggleGroup();

        temperatuur = new ToggleButton("Temperatuur");
        luchtvochtigheid = new ToggleButton("Luchtvochtigheid");
        luchtdruk = new ToggleButton("Luchtdruk");
        helderheid = new ToggleButton("Helderheid");

        temperatuur.setToggleGroup(grootheden);
        luchtvochtigheid.setToggleGroup(grootheden);
        luchtdruk.setToggleGroup(grootheden);
        helderheid.setToggleGroup(grootheden);

        temperatuur.setSelected(true);

        temperatuur.setOnAction(this);
        luchtvochtigheid.setOnAction(this);
        luchtdruk.setOnAction(this);
        helderheid.setOnAction(this);

        ObservableList lijst = menu.getChildren();
        lijst.addAll(returnButton, titel, temperatuur, luchtvochtigheid, luchtdruk, helderheid);

    }

    public void newGraph(String grootheid) {
        ArrayList<ArrayList<String>> result = Database.executeQuery("SELECT waarde, grootheid, datum FROM metingen");
        Graph graph = new Graph(result, grootheid);
        borderPane.setCenter(graph.getLineChart());

        graph.addSerie();
    }

    public BorderPane getPane() {
        return borderPane;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource() == temperatuur) {
            newGraph("temperature");
        } else if (actionEvent.getSource() == luchtvochtigheid) {
            newGraph("humidity");
        } else if (actionEvent.getSource() == luchtdruk) {
            newGraph("pressure");
        } else if (actionEvent.getSource() == helderheid) {
            newGraph("brightness");
        }
    }
}
