package GUI.Controller;

import BE.Case;
import BE.Technician;
import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerHomePageController implements Initializable {

    @FXML
    private TableColumn colCaseName, colCaseID, colCaseDescription, colCreatedDate, colTechAssigned;
    @FXML
    private TableView tblViewExistingCases, tblViewTechAssigned;
    @FXML
    private ImageView imgBack;
    @FXML
    private Button btnCreateNewCase, btnManageTech, btnUpdateCase;
    @FXML
    private TextField txtSearchBar;
    @FXML
    private Label lblCustomerName;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private ObservableList<Case> caseObservableList;
    private ObservableList<Technician> technicianObservableList;
    private final String back = "data/Images/Backward.png";
    private Util util = new Util();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        caseObservableList = FXCollections.observableArrayList();
        technicianObservableList = FXCollections.observableArrayList();
        lblCustomerName.setText(model.getCurrentCustomer().getCustomerName() + " Home Page");
        imgBack.setImage(util.loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        addListeners();
        util.addShadow(btnCreateNewCase, txtSearchBar);
        updateTableView();
        searchBarFilter();
        btnManageTech.setDisable(true);
        btnUpdateCase.setDisable(true);
        tblViewTechAssigned.setDisable(true);

    }

    /**
     * Navigates back to the CustomerView.
     * This method loads the CustomerView.fxml in the center of the application.
     */
    private void goBack() {
        try {
            controllerAssistant.loadCenter("CustomerView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not go back", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Updates the TableView with the cases for the current customer.
     * This method retrieves the cases from the model and populates the TableView.
     */
    private void updateTableView() {
        colCaseID.setCellValueFactory(new PropertyValueFactory<>("caseID"));
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colCaseDescription.setCellValueFactory(new PropertyValueFactory<>("caseDescription"));
        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        caseObservableList.clear();
        try {
            caseObservableList.addAll(model.getCasesForThisCustomer(model.getCurrentCustomer().getCustomerID()));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get cases from database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        tblViewExistingCases.setItems(caseObservableList);
    }

    /**
     * Adds listeners to the necessary components in the user interface.
     * This method handles the selection of existing cases in the TableView and the double click event.
     */
    private void addListeners() {
        tblViewExistingCases.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Case selectedItem = (Case) newSelection;
                ObservableList techniciansAssigned = FXCollections.observableArrayList();
                btnManageTech.setDisable(false);
                btnUpdateCase.setDisable(false);
                util.addShadow(btnManageTech, btnUpdateCase);
                try {
                    techniciansAssigned.addAll(model.getAssignedTechnicians(selectedItem.getCaseID()));
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Technicians from database", ButtonType.CANCEL);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                    dialogPane.getStyleClass().add("dialog");
                    alert.showAndWait();
                }
                tblViewTechAssigned.setDisable(false);
                colTechAssigned.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                tblViewTechAssigned.setItems(techniciansAssigned);
            } else {
                tblViewTechAssigned.setDisable(true);
                tblViewTechAssigned.getItems().clear();
                btnManageTech.setDisable(true);
                util.removeShadow(btnManageTech);
            }
        });
        tblViewExistingCases.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewExistingCases.getSelectionModel().getSelectedItem() != null) {
                Case selectedItem = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
                try {
                    model.setCurrentCase(selectedItem);
                    controllerAssistant.loadCenter("CaseHomePageView.fxml");
                    Thread thread = new Thread(() -> {
                        storeUserCaseLink(selectedItem);
                    });
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Case Home Page", ButtonType.CANCEL);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                    dialogPane.getStyleClass().add("dialog");
                    alert.showAndWait();
                }

            }
        });

    }

    /**
     * Store link between logged in user and selected case in database.
     */
    private void storeUserCaseLink(Case selectedCase) {
        User user = controllerAssistant.getLoggedInUser();
        try {
            model.storeUserCaseLink(user.getUserID(), selectedCase.getCaseID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not store link between User and Case in Database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Filters the items in the tblViewExistingCases TableView based on the user's input in the txtSearchBar TextField.
     * Creates a filtered list and sets a predicate to perform the filtering.
     * Displays all items if no input is entered, otherwise filters based on case name, assigned technician, case date, and case ID.
     */
    private void searchBarFilter() {
        // Create a list to hold the original unfiltered items in the tblViewCustomers TableView
        ObservableList<Case> originalList = FXCollections.observableArrayList(tblViewExistingCases.getItems());

        // Add a listener to the txtSearchBar TextField to filter the tblViewCustomers TableView based on the user's input
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a filtered list that contains all items from the tblViewCustomers TableView
            FilteredList<Case> filteredList = new FilteredList<>(originalList);

            // Set a predicate to filter the items based on the user's input
            if (newValue == null || newValue.isEmpty()) {
                // If the user has not entered any input, display all items
                tblViewExistingCases.setItems(originalList);
            } else {
                // Otherwise, filter the items based on the user's input
                String lowerCaseFilter = newValue.toLowerCase();
                filteredList.setPredicate(cases -> {
                    String caseID = String.valueOf(cases.getCaseID());
                    String caseDate = String.valueOf(cases.getCreatedDate());
                    String assignedTechnician = Objects.toString(cases.getAssignedTechnician(), "");
                    assignedTechnician = assignedTechnician.toLowerCase();
                    return cases.getCaseName().toLowerCase().contains(lowerCaseFilter)
                            || assignedTechnician.contains(lowerCaseFilter)
                            || caseDate.contains(lowerCaseFilter)
                            || caseID.contains(lowerCaseFilter);
                });
                tblViewExistingCases.setItems(filteredList);
            }
        });
    }

    /**
     * Handles the creation of a new case by opening the Create or Edit Case window.
     * Opens a new window using the FXMLLoader and sets the controller and location.
     * Sets only the current customer (without a selected case) in the CreateOrUpdateCaseController.
     * Updates the table view after creating the new case.
     */
    public void handleCreateNewCase() {
        CreateOrUpdateCaseController createOrUpdateCaseController = new CreateOrUpdateCaseController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(createOrUpdateCaseController);
        loader.setLocation(getClass().getResource("/GUI/View/CreateOrEditCaseView.fxml"));
        createOrUpdateCaseController.setOnlyCustomer(model.getCurrentCustomer());
        util.openNewWindow(stage, loader, "Could not open Case Window");
        updateTableView();
    }

    /**
     * Handles the update of a case by opening the Create or Edit Case window.
     * Retrieves the selected case from the table view.
     * Opens a new window using the FXMLLoader and sets the controller and location.
     * Sets the selected case and the current customer in the CreateOrUpdateCaseController.
     * Updates the table view after updating the case.
     */
    public void handleUpdateCase() {
        CreateOrUpdateCaseController createOrUpdateCaseController = new CreateOrUpdateCaseController();
        Case selectedCase = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(createOrUpdateCaseController);
        loader.setLocation(getClass().getResource("/GUI/View/CreateOrEditCaseView.fxml"));
        createOrUpdateCaseController.setCustomerAndCase(selectedCase, model.getCurrentCustomer());
        util.openNewWindow(stage, loader, "Could not open Case Window");
        updateTableView();
    }

    /**
     * Handles the management of technicians by opening the Manage Technicians window.
     * Retrieves the selected case from the table view and the already assigned technicians for the case.
     * Sets the selected case and already assigned technicians in the ManageTechniciansController.
     * Opens a new window using the FXMLLoader and sets the controller and location.
     * Updates the table view after managing the technicians.
     */
    public void handleManageTech() {
        ManageTechniciansController manageTechniciansController = new ManageTechniciansController();
        Case selectedCase = (Case) tblViewExistingCases.getSelectionModel().getSelectedItem();
        List<Technician> alreadyAssignedTechs = new ArrayList<>();
        try {
            alreadyAssignedTechs = model.getAssignedTechnicians(selectedCase.getCaseID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get assigned Technicians from the database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        manageTechniciansController.setSelectedCase(selectedCase, alreadyAssignedTechs);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(manageTechniciansController);
        loader.setLocation(getClass().getResource("/GUI/View/ManageTechniciansView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Manage Technicians Window", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        updateTableView();
    }
}

