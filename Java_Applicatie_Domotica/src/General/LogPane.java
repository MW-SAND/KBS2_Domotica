package General;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Date;

public class LogPane implements EventHandler<ActionEvent> {
    private BorderPane borderPane;
    private Text tTitle;
    private Button bRefresh;
    private HBox topBar;
    private TableView table;
    private ObservableList<LogLine> data;

    public LogPane() {
        borderPane = new BorderPane();
        topBar = new HBox();
        tTitle = new Text("Logging");
        bRefresh = new Button("Refresh");
        bRefresh.setOnAction(this);
        topBar.getChildren().addAll(tTitle, bRefresh);

        borderPane.setTop(topBar);

        table = new TableView();
        table.setEditable(true);

        TableColumn tcAction = new TableColumn("Actie");
        tcAction.setCellValueFactory(new PropertyValueFactory<LogLine, String>("action"));

        TableColumn tcDescription = new TableColumn("Beschrijving");
        tcDescription.setCellValueFactory(new PropertyValueFactory<LogLine, String>("description"));

        TableColumn tcDate = new TableColumn("Datum");
        tcDate.setCellValueFactory(new PropertyValueFactory<LogLine, Date>("date"));

        TableColumn tcSong = new TableColumn("Nummer");
        tcSong.setCellValueFactory(new PropertyValueFactory<LogLine, String>("songInfo"));

        fillTableRows();
        table.getColumns().addAll(tcAction, tcDescription, tcDate, tcSong);
        borderPane.setCenter(table);
    }

    private void fillTableRows() {
        ArrayList<LogLine> loggingData = new ArrayList<>();
        String query = "SELECT actie, beschrijving, datum, naam, artist FROM log LEFT JOIN muzieknummer mn ON mn.id = muzieknummer_id;";
        ArrayList<ArrayList<String>> result = Database.executeQuery(query);

        for(ArrayList<String> resultLine : result) {
            String muziekInfo = "-";
            try {
                muziekInfo = resultLine.get(3) + " - " + resultLine.get(4);
            } catch (IndexOutOfBoundsException ioobe) {
                System.out.println(ioobe.getMessage());
            }
          loggingData.add(new LogLine(resultLine.get(0), resultLine.get(1), resultLine.get(2), muziekInfo));
        }

        data = FXCollections.observableList(loggingData);
        table.setItems(data);
    }

    public Pane getPane() {
        return borderPane;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == bRefresh) {
            fillTableRows();
        }
    }
}
