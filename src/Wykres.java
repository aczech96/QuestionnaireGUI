import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Created by Anna on 21.01.2018.
 */
public class Wykres extends Application {
    DataBase baza = new DataBase();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Wykres odpowiedzi");
        stage.setWidth(500);
        stage.setHeight(500);
        Scanner id = new Scanner(System.in);
        int nrWykresu = 0;
        System.out.println("Wykres ktorego pytania chcesz wyswietlic?(1-4)");
        switch (id.nextInt()) {
            case 1:
                nrWykresu = 1;
                break;
            case 2:
                nrWykresu = 2;
                break;
            case 3:
                nrWykresu = 3;
                break;
            case 4:
                nrWykresu = 4;
                break;
            default:
                System.out.println("Nie ma wykresu o takim nr");
                break;

        }
        ObservableList<PieChart.Data> pyt =
                FXCollections.observableArrayList(
                        new PieChart.Data(baza.plotName(nrWykresu, "odpA"), baza.plotValues(nrWykresu, 1)),
                        new PieChart.Data(baza.plotName(nrWykresu, "odpB"), baza.plotValues(nrWykresu, 2)),
                        new PieChart.Data(baza.plotName(nrWykresu, "odpC"), baza.plotValues(nrWykresu, 3)),
                        new PieChart.Data(baza.plotName(nrWykresu, "odpD"), baza.plotValues(nrWykresu, 4)));

        final PieChart chart = new PieChart(pyt);
        chart.setTitle(baza.plotTitle(nrWykresu));

        ((Group) scene.getRoot()).getChildren().add(chart);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
