package GUI.Controller;

import BE.Case;
import BE.Technician;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageTechniciansController implements Initializable {

    @FXML
    private VBox vboxAllTechnicians, vboxComboBoxes;
    @FXML
    private Button btnConfirmChoices;
    private List<Technician> alreadyAssignedTechs, chosenTechnicians;
    private Model model;
    private Case selectedCase;
    private Util util = new Util();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chosenTechnicians = new ArrayList<>();
        model = Model.getInstance();
        btnConfirmChoices.getStyleClass().add("orangeButtons");
        util.addShadow(btnConfirmChoices);
        updateTechnicians();
    }

    /**
     * Updates the list of technicians by retrieving them from the database and populating the UI.
     * It adds technician names as labels, checkboxes for selection, and assigns appropriate styles.
     * It also handles the selection and deselection of technicians using checkboxes.
     */
    private void updateTechnicians() {
        List<Technician> technicians = new ArrayList<>();
        try {
            technicians.addAll(model.getAllTechnicians());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Technicians from Database", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        for (Technician t : technicians) {
            Label technicianName = new Label(t.getFullName());
            technicianName.setStyle("-fx-font-size: 24");
            CheckBox cbChosenTech = new CheckBox();
            for (Technician t1 : alreadyAssignedTechs) {
                if(t1.getFullName().equals(t.getFullName())) {
                    chosenTechnicians.add(t);
                    cbChosenTech.setSelected(true);
                }
            }
            cbChosenTech.setStyle("-fx-font-size: 24");
            vboxAllTechnicians.getChildren().add(technicianName);
            vboxComboBoxes.getChildren().add(cbChosenTech);
            util.addShadow(cbChosenTech);
            // Add a listener to the checkbox
            cbChosenTech.setOnAction(event -> {
                if (cbChosenTech.isSelected()) {
                    chosenTechnicians.add(t);
                } else {
                    chosenTechnicians.remove(t);
                }
            });
        }
    }

    /**
     * Setter for selectedCase and already assigned tech.
     */
    public void setSelectedCase(Case selectedCase, List<Technician> alreadyAssignedTechs) {
        this.selectedCase = selectedCase;
        this.alreadyAssignedTechs = alreadyAssignedTechs;
    }

    /**
     * Handles the confirmation of selected technicians for a case.
     * It retrieves the case ID and adds the chosen technicians to the case in the database.
     * Finally, it closes the current window.
     */
    public void handleConfirmChoices() {
        int caseID = selectedCase.getCaseID();
        try {
            model.addTechnicianToCase(caseID, chosenTechnicians);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not assign Technicians to Case", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        Stage stage = (Stage) btnConfirmChoices.getScene().getWindow();
        stage.close();
    }

}
