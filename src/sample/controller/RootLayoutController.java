package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import sample.Main;
import sample.manager.LoginManager;

public class RootLayoutController {

    @FXML
    private Button logoutButton;
    @FXML
    private Label sessionLabel;

    @FXML
    private Menu menuId;

    private LoginManager loginManager;

    public void initialize() {
    }

    public void initSessionID(final LoginManager loginManager, String sessionID) {
        this.loginManager = loginManager;
        menuId.setText("User : "+sessionID);
    }

    public void logout(ActionEvent actionEvent) {
        loginManager.logout();
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void handleHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Program Information");
        alert.setHeaderText("This is a sample JAVAFX application!");
        alert.setContentText("You can search, delete, update, insert a new employee with this program.");
        alert.show();
    }

}
