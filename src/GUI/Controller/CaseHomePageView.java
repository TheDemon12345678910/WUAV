package GUI.Controller;

import BE.Addendum;
import BE.Case;
import BE.Customer;
import BE.Report;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CaseHomePageView implements Initializable {

    @FXML
    private Label lblCaseName;
    @FXML
    private TextField txtReportName, txtSearchField;
    @FXML
    private TextArea txtReportDescription;
    @FXML
    private Button btnCreateNewReport, btnAddNewAddendum;
    @FXML
    private TableView tblViewExistingReports, tblViewAddendums;
    @FXML
    private TableColumn colReportName, colTechnician, colCreatedDate, colStatus, colAddendumName, colAddendumTechnician, colAddendumCreatedDate, colAddendumStatus;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;

    private Customer currentCustomer;
    private Case currentCase;

    private List<Report> reports;

    private ObservableList observableReports;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        updateTableView();
        disableAddendum();
        btnCreateNewReport.setDisable(true);
        lblCaseName.setText("Case Name: " + currentCase.getCaseName());
        addListeners();
        addShadow(txtReportDescription, txtReportName, txtSearchField);
        searchBarFilter();

    }

    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }

    private void removeShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(null);
        }
    }

    private void addListeners() {
        txtReportName.textProperty().addListener(createNewReportBtnListener);
        txtReportDescription.textProperty().addListener(createNewReportBtnListener);
        tblViewExistingReports.getSelectionModel().selectedItemProperty().addListener(selectedItemListener);


        tblViewExistingReports.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewExistingReports.getSelectionModel().getSelectedItem() != null) {
                Report selectedItem = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
                if (selectedItem.isActive()) {
                    try {
                        model.setCurrentReport(selectedItem);
                        model.setCurrentCase(currentCase);
                        model.setCurrentCustomer(currentCustomer);
                        controllerAssistant.loadCenter("ReportHomePageView.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Report Home Page", ButtonType.CANCEL);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report is inactive, please make a Addendum instead", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

        tblViewAddendums.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewAddendums.getSelectionModel().getSelectedItem() != null) {
                Report selectedItem = (Report) tblViewAddendums.getSelectionModel().getSelectedItem();
                if (selectedItem.isActive()) {
                    try {
                        model.setCurrentReport(selectedItem);
                        model.setCurrentCase(currentCase);
                        model.setCurrentCustomer(currentCustomer);
                        controllerAssistant.loadCenter("ReportHomePageView.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Addendum Home Page", ButtonType.CANCEL);
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Addendum is inactive, please make another Addendum instead", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });
    }


    ChangeListener<String> createNewReportBtnListener = (observable, oldValue, newValue) -> {
        if (txtReportName.getText().isEmpty() || txtReportDescription.getText().isEmpty()) {
            btnCreateNewReport.setDisable(true);
            removeShadow(btnCreateNewReport);
        } else {
            btnCreateNewReport.setDisable(false);
            addShadow(btnCreateNewReport);
        }
    };


    ChangeListener<Report> selectedItemListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            if (newValue.getIsActive().equals("Inactive")) {
                tblViewAddendums.setDisable(false);
                btnAddNewAddendum.setDisable(false);
                updateTableViewAddendums();
                addShadow(btnAddNewAddendum);
            } else {
                tblViewAddendums.setDisable(true);
                btnAddNewAddendum.setDisable(true);
                tblViewAddendums.getItems().clear();
                removeShadow(btnAddNewAddendum);
            }

        } else {
            tblViewAddendums.setDisable(true);
            tblViewAddendums.getItems().clear();
            btnAddNewAddendum.setDisable(true);
            addShadow(btnAddNewAddendum);
        }
    };

    private void updateTableViewAddendums() {
        ObservableList<Addendum> addendumObservableList = FXCollections.observableArrayList();
        Case currentCase1 = model.getCurrentCase();
        Report selectedReport = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
        int caseID = currentCase1.getCaseID();
        int reportID = selectedReport.getReportID();
        try {
            addendumObservableList.addAll(model.getAddendums(caseID, reportID));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get addendums from Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        colAddendumName.setCellValueFactory(new PropertyValueFactory<>("reportName"));
        colAddendumTechnician.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        colAddendumCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        colAddendumStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tblViewAddendums.setItems(addendumObservableList);

    }


    private void disableAddendum() {
        tblViewAddendums.setDisable(true);
        btnAddNewAddendum.setDisable(true);
    }

    private void updateTableView() {
        try {
            reports = model.getReports(currentCase.getCaseID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get reports from Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        observableReports = FXCollections.observableArrayList();
        observableReports.addAll(reports);
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        colReportName.setCellValueFactory(new PropertyValueFactory<>("reportName"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tblViewExistingReports.getColumns().addAll();
        tblViewExistingReports.setItems(observableReports);

    }

    public void handleCreateNewReport(ActionEvent actionEvent) {
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        int caseID = currentCase.getCaseID();
        try {
            model.createNewReport(reportName, reportDescription, caseID, controllerAssistant.getLoggedInUser().getUserID()); //TODO UserID might not be right here, we need to fix this.
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new report", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableView();
    }

    public void handleCreateNewAddendum(ActionEvent actionEvent) {
        AddAddendumView addAddendumView = new AddAddendumView();
        Report selectedReport = (Report) tblViewExistingReports.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(addAddendumView);
        loader.setLocation(getClass().getResource("/GUI/View/AddAddendumView.fxml"));
        addAddendumView.setCaseAndReport(currentCase, selectedReport);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Addendum Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateTableViewAddendums();
    }

    private void searchBarFilter() {
        // Create a list to hold the original unfiltered items in the tblViewCustomers TableView
        ObservableList<Report> originalList = FXCollections.observableArrayList(tblViewExistingReports.getItems());

        // Add a listener to the txtSearchBar TextField to filter the tblViewCustomers TableView based on the user's input
        txtSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a filtered list that contains all items from the tblViewCustomers TableView
            FilteredList<Report> filteredList = new FilteredList<>(originalList);

            // Set a predicate to filter the items based on the user's input
            if (newValue == null || newValue.isEmpty()) {
                // If the user has not entered any input, display all items
                tblViewExistingReports.setItems(originalList);
            } else {
                // Otherwise, filter the items based on the user's input
                String lowerCaseFilter = newValue.toLowerCase();
                filteredList.setPredicate(reports -> {
                    String reportDate = String.valueOf(reports.getCreatedDate());
                    String assignedTechnician = Objects.toString(reports.getAssignedTechnician(), "");
                    assignedTechnician = assignedTechnician.toLowerCase();
                    return reports.getReportName().toLowerCase().contains(lowerCaseFilter)
                            || assignedTechnician.contains(lowerCaseFilter)
                            || reportDate.contains(lowerCaseFilter);
                });
                tblViewExistingReports.setItems(filteredList);
            }
        });
    }
}

