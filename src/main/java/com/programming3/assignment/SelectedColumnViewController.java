package com.programming3.assignment;

import com.programming3.assignment.algorithms.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SelectedColumnViewController {

    @FXML
    private Button mergeSortButton;
    @FXML
    private Button quickSortButton;
    @FXML
    private Button insertionSortButton;
    @FXML
    private Button shellSortButton;
    @FXML
    private Button heapSortButton;

    @FXML
    private Label mergeSortTimeLabel;
    @FXML
    private Label quickSortTimeLabel;
    @FXML
    private Label insertionSortTimeLabel;
    @FXML
    private Label shellSortTimeLabel;
    @FXML
    private Label heapSortTimeLabel;
    private ObservableList<Double> originalData;
    private final SortingAlgorithm mergeSort = new MergeSort();
    private final SortingAlgorithm quickSort = new QuickSort();
    private final SortingAlgorithm insertionSort = new InsertionSort();
    private final SortingAlgorithm shellSort = new ShellSort();
    private final SortingAlgorithm heapSort = new HeapSort();

    private ObservableList<Double> columnData;
    private String columnHeader;

    public void setColumnData(String columnHeader, ObservableList<Double> columnData) {
        this.originalData = FXCollections.observableArrayList(columnData);
        this.columnHeader = columnHeader;
        this.columnData = columnData;
    }

    @FXML
    public void initialize() {
        mergeSortButton.setOnAction(event ->
                performSort(mergeSort, mergeSortTimeLabel)
        );
        quickSortButton.setOnAction(event ->
                performSort(quickSort, quickSortTimeLabel)
        );
        insertionSortButton.setOnAction(event ->
                performSort(insertionSort, insertionSortTimeLabel)
        );
        shellSortButton.setOnAction(event ->
                performSort(shellSort, shellSortTimeLabel)
        );
        heapSortButton.setOnAction(event ->
                performSort(heapSort, heapSortTimeLabel)
        );
    }

    private double performSort(SortingAlgorithm algorithm, Label timeLabel) {
        double[] dataArray = originalData.stream().mapToDouble(Double::doubleValue).toArray();


        long duration = measureSortingTime(() -> algorithm.sort(dataArray));
        double timeInMs = duration / 1_000_000.0;



        timeLabel.setText(String.format("%.2f ms", timeInMs));
        return timeInMs;
}


    private long measureSortingTime(Runnable sortingAlgorithm) {
        long startTime = System.nanoTime();
        sortingAlgorithm.run();
        return System.nanoTime() - startTime;
    }
}
