/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import connection.Sqlite_Connection;
import controller.DrawerController;
import controller.TP_SectionController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author DaCodin
 */
public class Login {

    private static Login instance = new Login();

    public static Login getInstance() {
        return instance;
    }

    // connection variables
    public PreparedStatement pst;
    public ResultSet rs;
    public Connection conn;
    /////////////////////////////////////////////////////////
    private JFXTextField txtField1;
    private JFXPasswordField txtField2;
    public static String movelevel = null;
    public static String user_name = "Guest";

    //password instance
    public TextField getTxtField2() {
        return txtField2;
    }

    public void setTxtField2(JFXPasswordField txtField2) {
        this.txtField2 = txtField2;
    }

    //username instance
    public TextField getTxtField1() {
        return txtField1;
    }

    public void setTxtField1(JFXTextField txtField1) {
        this.txtField1 = txtField1;
    }
///////////////////
    private static JFXButton xreport = null;

    public JFXButton getButton1() {
        return xreport;
    }

    public void setButton1(JFXButton xreport) {
        this.xreport = xreport;
    }
    ////////////
    private JFXButton xregister;

    public JFXButton getButton2() {
        return xregister;
    }

    public void setButton2(JFXButton xregister) {
        this.xregister = xregister;
    }
    //////////////
    private JFXButton xsettings;

    public JFXButton getButton3() {
        return xsettings;
    }

    public void setButton3(JFXButton xsettings) {
        this.xsettings = xsettings;
    }
    private Label emessage1;

    public Label label() {
        return emessage1;
    }

    public void setLTxt(Label emessage1) {
        this.emessage1 = emessage1;
    }
    //border pane
    private BorderPane bpane;

    public BorderPane getBpane() {
        return bpane;
    }

    public void setBpane(BorderPane bpane) {
        this.bpane = bpane;
    }

    //String instance
    public static String CurrentUser = null;
    
       public void lockdown(DrawerController controller) {
        controller.report.setDisable(false);
        //ockdown(controller) = true;
        controller.register.setDisable(false);
    }
       
       

    public void loginFunc() {
        CurrentUser = null;
        String sql = "select * from USER where Username=? and Password=?";
        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(sql);

            pst.setString(1, txtField1.getText());

            pst.setString(2, txtField2.getText());

            rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Successful Login");

                try {
                    AnchorPane main = FXMLLoader.load(getClass().getResource("/views/index.fxml"));
                    main.getHeight();
                    main.getWidth();
                    bpane.setCenter(main);

                } catch (IOException ex) {
                    Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (rs.getString("Level").equals("Admin Access")) {
                    movelevel = rs.getString("Level");
                    user_name = rs.getString("Surname");
                  //  lockdown(controller.);
//                    xreport.setDisable(false);

                } else if (rs.getString("Level").equals("Canteen Access")) {
                    movelevel = rs.getString("Level");
                    user_name = rs.getString("Surname");

                }
                txtField1.setText("");
                txtField2.setText("");

            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Invalid Username or Password");
            System.out.println(e);
            // respond.setText("");
//            JFXSnackbar snackbar = new JFXSnackbar(null);
//            snackbar.show("Invalid Username or Password", 3000);

        } finally {
            try {
                rs.close();
            } catch (Exception e) {
                /* ignored */ }
            try {
                pst.close();
            } catch (Exception e) {
                /* ignored */ }
            try {
                conn.close();
            } catch (Exception e) {
                /* ignored */ }
        }

    }
    
  

    public void LogoutFunc() {
        user_name = "Guest";
        movelevel = "Guest";
        try {
            AnchorPane main = FXMLLoader.load(getClass().getResource("/views/index.fxml"));
            main.getHeight();
            main.getWidth();
            bpane.setCenter(main);

        } catch (IOException ex) {
            Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
   