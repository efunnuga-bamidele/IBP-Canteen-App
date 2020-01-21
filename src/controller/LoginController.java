/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import functions.Login;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author DaCodin
 */
public class LoginController extends TP_SectionController implements Initializable {

//    DrawerController dc = new DrawerController();
   
    public static PreparedStatement pst;
    public static ResultSet rs;
    public static Connection conn;

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton clear;

    @FXML
    private JFXButton login;
    @FXML
    private AnchorPane rootPane;
   

   

    @FXML
    void ClearAction(ActionEvent event
    ) {
        username.setText("");
        password.setText("");
    }

    @FXML
    void LoginAction(ActionEvent event
    ) {
        functions.Login.getInstance().setTxtField1(username);
        functions.Login.getInstance().setTxtField2(password);
        functions.Login.getInstance().loginFunc();
    }

    @FXML
    void Logout(ActionEvent event
    ) {
        functions.Login.getInstance().LogoutFunc();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        password.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    // do something
                    functions.Login.getInstance().setTxtField1(username);
                    functions.Login.getInstance().setTxtField2(password);
                    functions.Login.getInstance().loginFunc();
                }
            }
        });

        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                username.requestFocus();
            }
        });

    }

}
