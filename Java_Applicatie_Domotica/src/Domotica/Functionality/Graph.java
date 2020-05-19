package Domotica.Functionality;

import General.Database;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Graph {
    private LineChart<Number, Number> lineChart;
    private XYChart.Series serie;

    public Graph(ArrayList<ArrayList<String>> data, String grootheid) {
        // initaliseren onderdelen grafiek
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);

        xAxis.setLabel("Aantal dagen");
        yAxis.setLabel("Waarde");

        lineChart.setTitle(grootheid);
        lineChart.setLegendVisible(false);

        serie = new XYChart.Series();

        // geeft minimale en maximale waarde van de datum
        long lowestDate = calcDate(grootheid, "MIN");
        long highestDate = calcDate(grootheid, "MAX");

        // een stap is 10% van de x-as
        double step = (double) (highestDate - lowestDate) / (10*1000*60*60*24);
        int numberOfSteps = 1;
        double averageTotal = 0;
        double numberOfMeasurements = 0;
        double dayTotal = 0;

        for (ArrayList<String> datum : data) {
            // controleren of meting van het gewenste type is
            if (datum.get(1).equals(grootheid)) {
                try {
                    double waarde = Double.parseDouble(datum.get(0));
                    double day = getDateValue(datum.get(2), lowestDate);

                    // controleren of dag niet binnen de stap valt
                    if (day > (step * numberOfSteps)) {
                        // toevoegen gemiddelde aan grafiek en resetten variabelen
                        double dayAverage = dayTotal / numberOfMeasurements;
                        double waardeAverage = averageTotal / numberOfMeasurements;
                        serie.getData().add(new XYChart.Data(dayAverage, waardeAverage));
                        dayTotal = 0;
                        averageTotal = 0;
                        numberOfMeasurements = 0;
                        numberOfSteps++;
                    }

                    // dagwaarden toevoegen aan totaal
                    dayTotal += day;
                    averageTotal += waarde;
                    numberOfMeasurements++;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    public void addSerie() {
       lineChart.getData().add(serie);
    }

    public long calcDate(String grootheid, String operator) {
        long date = 0;

        // datum uit de database halen
        String query = "SELECT " + operator + "(datum) FROM metingen WHERE grootheid = '" + grootheid + "';";
        ArrayList<ArrayList<String>> result = Database.executeQuery(query);
        String DateS = result.get(0).get(0);
        try {
            // datumstring naar long
            Date DateD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(DateS);
            date =DateD.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public double getDateValue(String stringDate, long lowestDate) throws ParseException {
        // datumstring naar long
        Date dateDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(stringDate);
        long longDate = dateDate.getTime();

        return (double) (longDate - lowestDate) / (1000*60*60*24);
    }


}