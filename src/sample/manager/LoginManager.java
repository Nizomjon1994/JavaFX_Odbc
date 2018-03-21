package sample.manager;

/**
 * Created by Nizomjon on 12/03/2017.
 */

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.Main;
import sample.controller.LoginController;
import sample.controller.RootLayoutController;
import sample.util.DBUtil;


public class LoginManager {
    private Scene scene;
    private BorderPane rootLayout;

    private StackPane stackPane;
    private Stage stage;

    public LoginManager(Scene scene, Stage stage) {
        this.scene = scene;
        this.stage = stage;
    }


    public void authenticated(String sessionID) {
        showMainView(sessionID);
    }

    public void logout() {
        try {
            DBUtil.dbDisconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/login.fxml"));
            stackPane = (StackPane) loader.load();

            Scene scene = new Scene(stackPane);
            LoginController controller =
                    loader.<LoginController>getController();
            controller.initManager(this);
            stage.setScene(scene); //Set the scene in primary stage.

            stage.show(); //Display the primary stage
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showMainView(String sessionID) {

        initRootLayout(sessionID);
        showEmployeeView();
    }

    public void initRootLayout(String sessionID) {
        try {
            //First, load root layout from RootLayout.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            RootLayoutController controller =
                    loader.<RootLayoutController>getController();
            controller.initSessionID(this, sessionID);
            //Second, show the scene containing the root layout.
            Scene scene = new Scene(rootLayout); //We are sending rootLayout to the Scene.
            stage.setScene(scene); //Set the scene in primary stage.

            /*//Give the controller access to the main.
            RootLayoutController controller = loader.getController();
            controller.setMain(this);*/

            //Third, show the primary stage
            stage.show(); //Display the primary stage
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Shows the employee operations view inside the root layout.
    public void showEmployeeView() {
        try {
            //First, load EmployeeView from EmployeeView.fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/employeeViewNew.fxml"));
            AnchorPane employeeOperationsView = (AnchorPane) loader.load();

            // Set Employee Operations view into the center of root layout.
            rootLayout.setCenter(employeeOperationsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

