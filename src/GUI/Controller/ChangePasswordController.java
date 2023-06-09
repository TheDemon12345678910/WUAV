package GUI.Controller;

import BE.User;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private ImageView imgWUAVLogo;
    @FXML
    private PasswordField pswOldPassword, pswNewPassword, pswNewPasswordCheck;
    @FXML
    private Button btnChangePassword;
    private Util util = new Util();
    private Model model;
    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        setImg();
        util.addShadow(pswOldPassword, pswNewPassword, pswNewPasswordCheck, imgWUAVLogo);
        btnChangePassword.setDisable(true);
        addListner();
    }

    /**
     * Sets the logo at the top.
     */
    private void setImg() {
        String logo = "data/Images/logoWhite.png";
        imgWUAVLogo.setImage(util.loadImages(logo));
    }

    /**
     * Checks to see if the old password is correct, checks if the new password is a valid length, and then saves the changes to the database
     * @param actionEvent
     * @throws Exception
     */
    public void handleChangePassword(ActionEvent actionEvent) throws Exception {
        if (model.checkPassword(user.getPassword(), pswOldPassword.getText())) {
            if (pswNewPassword.getText().equals(pswNewPasswordCheck.getText())) {
                if (pswNewPassword.getText().length() >= 8) {
                    model.setPassword(user.getUserName(), pswNewPassword.getText());
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password must be 8 or more characters", ButtonType.CLOSE);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                    dialogPane.getStyleClass().add("dialog");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Listeners for the textfields
     */
    private void addListner(){
        pswOldPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!pswOldPassword.getText().isEmpty()&&!pswNewPassword.getText().isEmpty()&&!pswNewPasswordCheck.getText().isEmpty()){
                    btnChangePassword.setDisable(false);
                    util.addShadow(btnChangePassword);
                }
                else {
                    btnChangePassword.setDisable(true);
                }
            }
        });
        pswNewPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!pswOldPassword.getText().isEmpty()&&!pswNewPassword.getText().isEmpty()&&!pswNewPasswordCheck.getText().isEmpty()){
                    btnChangePassword.setDisable(false);
                    util.addShadow(btnChangePassword);
                }
                else {
                    btnChangePassword.setDisable(true);
                }
            }
        });
        pswNewPasswordCheck.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!pswOldPassword.getText().isEmpty()&&!pswNewPassword.getText().isEmpty()&&!pswNewPasswordCheck.getText().isEmpty()){
                    btnChangePassword.setDisable(false);
                    util.addShadow(btnChangePassword);
                }
                else {
                    btnChangePassword.setDisable(true);
                }
            }
        });
    }

    /**
     * Sets the user to the one passed through.
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

}
