package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {



    private Stage stage;
    private ArrayList<City> cityList;

    public Controller(){

    }
    @FXML private Button button_Open;
    @FXML private Button button_Start;
    @FXML private Button pause_Button;
    @FXML private Button stop_Button;
    @FXML private TextField dataset_TextField;
    @FXML private TextField generations_TextField;
    @FXML private TextField mutationChance_TextField;
    @FXML private ChoiceBox parentChildRatio_Box;
    @FXML private CheckBox elitism_CheckBox;
    @FXML private ChoiceBox parentCoice_ChoiceBox;
    @FXML private ChoiceBox reproductionMethod;
    @FXML private ChoiceBox mutationMethod;
    @FXML private TextArea result_TextArea;
    @FXML private AnchorPane coverage_AnchorPane;
    @FXML private LineChart<String,Number> coverage_Chart;
    @FXML private ScatterChart<Number,Number> cities_Chart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private TextField population_TextField;
    private Algorithm geneticAlgorithm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> list = new ArrayList<String>();
        list.add("Roulette Wheel");
        list.add("Rank Roulette Wheel");
        list.add("Tournament");
        ObservableList obList = FXCollections.observableList(list);
        parentCoice_ChoiceBox.getItems().clear();
        parentCoice_ChoiceBox.setItems(obList);
        parentCoice_ChoiceBox.setValue("Roulette Wheel");
        list = new ArrayList<String>();
        list.add("25:75");
        list.add("50:50");
        list.add("75:25");
        obList = FXCollections.observableList(list);
        parentChildRatio_Box.getItems().clear();
        parentChildRatio_Box.setItems(obList);
        parentChildRatio_Box.setValue("25:75");
        list = new ArrayList<String>();
        list.add("Uniform");
        obList = FXCollections.observableList(list);
        mutationMethod.getItems().clear();
        mutationMethod.setItems(obList);
        mutationMethod.setValue("Uniform");
        list = new ArrayList<String>();
        list.add("Linear Crossover");
        list.add("Arithmetic Crossover");
        obList = FXCollections.observableList(list);
        reproductionMethod.getItems().clear();
        reproductionMethod.setItems(obList);
        reproductionMethod.setValue("Linear Crossover");
        elitism_CheckBox.setAllowIndeterminate(false);
        elitism_CheckBox.setSelected(false);

        coverage_Chart.setTitle("Coverage Chart");
        coverage_Chart.setAnimated(false);
        coverage_Chart.setLegendVisible(false);
        pause_Button.setVisible(false);
        stop_Button.setVisible(false);

        cities_Chart.setLegendVisible(false);
        cities_Chart.setAnimated(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(100);
        yAxis.setUpperBound(100);
        yAxis.setLowerBound(0);
        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        XYChart.Series<String,Number> series = new XYChart.Series<>();
        coverage_Chart.getData().add(series);
        coverage_AnchorPane.setMinWidth(500);
        coverage_Chart.setMinWidth(500);

    }

    public void updateScatterGraphWithBestSolution(Solution solution){

        cities_Chart.getData().clear();
        XYChart.Series<Number,Number> cities = new XYChart.Series<>();
        for (City i : cityList){
            cities.getData().add(new XYChart.Data<>(i.getX(),i.getY()));
        }
        cities_Chart.getData().add(cities);
        XYChart.Series<Number,Number> antennas = new XYChart.Series<>();
        antennas.getData().add(new XYChart.Data<>(solution.getX1(),solution.getY1()));
        antennas.getData().add(new XYChart.Data<>(solution.getX2(),solution.getY2()));
        antennas.getData().add(new XYChart.Data<>(solution.getX3(),solution.getY3()));
        cities_Chart.getData().add(antennas);

    }


    public void updateLineChartWithBestSolution(Solution solution){

        if(solution.getGeneration() == 1){
            coverage_Chart.getData().clear();
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            coverage_Chart.getData().add(series);
            coverage_AnchorPane.setMinWidth(500);
            coverage_Chart.setMinWidth(500);
        }
        String gen = "Gen " + solution.getGeneration();
        Number n = (int)solution.getEvaluation();
        coverage_Chart.getData().get(0).getData().add(new XYChart.Data<>(gen,n));
        coverage_AnchorPane.setMinWidth(coverage_AnchorPane.getMinWidth() + 20);
        coverage_Chart.setMinWidth(coverage_AnchorPane.getMinWidth() + 20);

    }

    public void updateResultsArea(String text){

        result_TextArea.clear();
        result_TextArea.setText(text);
    }



    @FXML protected void handleStartButtonAction(ActionEvent event){

        Parameters parameters = new Parameters();
        cityList = DatasetLoader.decodeDataset(new File(dataset_TextField.getText()));
        parameters.setCityList(cityList);
        parameters.setElitism(elitism_CheckBox.isSelected());
        parameters.setGenerations(Integer.parseInt(generations_TextField.getText()));
        parameters.setMutationChance(mutationChance_TextField.getText());
        parameters.setParentChoice(parentCoice_ChoiceBox.getValue().toString());
        parameters.setParentChildRatio(parentChildRatio_Box.getValue().toString());
        parameters.setMutationMethod(mutationMethod.getValue().toString());
        parameters.setReproductionMethod(reproductionMethod.getValue().toString());
        parameters.setPopulation(Integer.parseInt(population_TextField.getText()));
        coverage_Chart.getData().get(0).getData().clear();
        geneticAlgorithm = new Algorithm(this,parameters);
        geneticAlgorithm.run();

    }

    @FXML protected void handlePauseButtonAction(ActionEvent event){

    }

    @FXML protected void handleStopButtonAction(ActionEvent event){

    }

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Dataset File");
        File datasetFile = fileChooser.showOpenDialog(null);
        dataset_TextField.setText(datasetFile.getPath());
    }

    public void setTextAreaResult(String result){
        result_TextArea.setText(result);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ArrayList<City> getCityList(){

        return (cityList != null ? cityList : new ArrayList<City>());
    }


}
