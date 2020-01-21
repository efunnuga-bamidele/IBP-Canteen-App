/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import functions.Login;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author DaCodin
 */
public class DrawerController implements Initializable {
//public class DrawerController extends TP_SectionController {

    private DrawerController thisController;
    @FXML
    private JFXButton ticket;

    @FXML
    private JFXButton login;

    @FXML
    public JFXButton register;

    @FXML
    public JFXButton report;

    @FXML
    JFXButton setting;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        thisController = this;
//        functions.Login.getInstance().setButton1(report);
//        functions.Login.getInstance().setButton2(register);
//        functions.Login.getInstance().setButton3(setting);
        // TODO

    }

  Login button = new Login();
  @FXML
private void actionLockdown(ActionEvent event) {
    button.lockdown(thisController);
}

}
