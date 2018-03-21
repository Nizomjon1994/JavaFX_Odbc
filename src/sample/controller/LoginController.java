package sample.controller;

/**
 * Created by Nizomjon on 12/03/2017.
 */

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.manager.LoginManager;
import sample.util.DBUtil;

/**
 * Controls the login screen
 */
public class LoginController {
    @FXML
    private TextField user;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;

    public void initialize() {
    }

    public void initManager(final LoginManager loginManager) {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (DBUtil.dbConnect(user.getText(), password.getText())) {
                    loginManager.authenticated(user.getText());
                } else {

                }
            }
        });
    }

    /**
     * Check authorization credentials.
     * <p>
     * If accepted, return a sessionID for the authorized session
     * otherwise, return null.
     */
    private String authorize() {
        return
                "scott".equals(user.getText()) && "tiger".equals(password.getText())
                        ? generateSessionID()
                        : null;
    }

    private static int sessionID = 0;

    private String generateSessionID() {
        sessionID++;
        return "xyzzy - session " + sessionID;
    }
}