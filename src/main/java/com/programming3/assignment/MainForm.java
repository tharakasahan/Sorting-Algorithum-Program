package com.programming3.assignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainForm {
    @FXML
    private ComboBox<String> numericColumnComboBox;

    private List<String> headers = new ArrayList<>();
    private List<List<String>> rawData = new ArrayList<>();
    private ObservableList<ObservableList<Double>> numericData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        numericColumnComboBox.setOnAction(this::handleNumericColumnSelection);
    }

    public void addCSVFileOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            loadCSVFile(file);
        }
    }

    private void loadCSVFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true;
            headers.clear();
            rawData.clear();
            numericColumnComboBox.getItems().clear();
            numericData.clear();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (isFirstRow) {
                    isFirstRow = false;
                    headers.addAll(List.of(fields));
                } else {
                    rawData.add(List.of(fields));

                    // Convert numeric fields to Double
                    ObservableList<Double> numericRow = FXCollections.observableArrayList();
                    for (String field : fields) {
                        try {
                            numericRow.add(Double.parseDouble(field));
                        } catch (NumberFormatException e) {
                            numericRow.add(Double.NaN);
                        }
                    }
                    numericData.add(numericRow);
                }
            }

            identifyNumericColumns();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void identifyNumericColumns() {
        for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
            boolean isNumeric = true;

            for (List<String> row : rawData) {
                String cellValue = row.get(colIndex);
                if (cellValue == null || !cellValue.matches("-?\\d+(\\.\\d+)?")) {
                    isNumeric = false;
                    break;
                }
            }

            if (isNumeric) {
                String columnName = headers.get(colIndex);
                numericColumnComboBox.getItems().add(colIndex + " - " + columnName);
            }
        }
    }

    @FXML
    private void handleNumericColumnSelection(ActionEvent event) {
        String selectedColumn = numericColumnComboBox.getSelectionModel().getSelectedItem();
        if (selectedColumn != null) {
            int columnIndex = Integer.parseInt(selectedColumn.split(" - ")[0]);
            showSelectedColumnData(columnIndex);
        }
    }

    private void showSelectedColumnData(int columnIndex) {
        ObservableList<Double> columnData = FXCollections.observableArrayList();
        String columnHeader = headers.get(columnIndex);

        for (List<String> row : rawData) {
            try {
                columnData.add(Double.parseDouble(row.get(columnIndex)));
            } catch (NumberFormatException e) {
                columnData.add(Double.NaN);
            }
        }
        openColumnWindow(columnHeader, columnData);
    }

    private void openColumnWindow(String columnHeader, ObservableList<Double> columnData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/programming3/assignment/SelectedColumnViews.fxml"));
            Parent root = loader.load();

            SelectedColumnViewController controller = loader.getController();
            controller.setColumnData(columnHeader, columnData);

            Stage stage = new Stage();
            stage.setTitle("Analysis Screen - " + columnHeader);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}