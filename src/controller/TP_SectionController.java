/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import connection.Sqlite_Connection;
import static functions.Login.movelevel;
import static functions.Login.user_name;

import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author DaCodin
 */
public class TP_SectionController implements Initializable {

    public static PreparedStatement pst;
    public static ResultSet rs;
    public static Connection conn;
    String Date;
    String Time;
    String Year;
    String Month;
    String Day;
    private int minute;
    private int hour;
    private int second;
    @FXML
    private JFXHamburger menu;

    @FXML
    private JFXDrawer drawer;

    @FXML
    public BorderPane body;

    @FXML
    JFXButton close;
    @FXML
    private JFXButton dbStatus;
    @FXML
    private Label Timer;
    @FXML
    private Label user;
    Robot r;

    public void closed(ActionEvent event) {
        System.exit(0);
    }

    //Dynamic Tyme and Date
    public void CurrentDate() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            GregorianCalendar cal = new GregorianCalendar();
            Calendar xcal = Calendar.getInstance();
            xcal.add(Calendar.DATE, 0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date = fmt.format(xcal.getTime());
            //System.out.print(Date);
            int month = cal.get(2);
            int day = cal.get(5);
            int year = cal.get(1);
            //Date = day + "/" + (month + 1) + "/" + year;
            //Date = year + "-" + (month + 1) + "-" + day;
            //Month = String.valueOf((month + 1));
            //Day = String.valueOf(day);

//            Calendar cal = Calendar.getInstance();
            second = cal.get(Calendar.SECOND);
            minute = cal.get(Calendar.MINUTE);
            hour = cal.get(Calendar.HOUR);
            //System.out.println(hour + ":" + (minute) + ":" + second);

            SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
            Timer.setText("Time : " + formatDate.format(xcal.getTime()) + " - Date: " + Date);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        functions.Login.getInstance().setBpane(body);
//        functions.Login.getInstance().setLTxt(U);
        CurrentDate();

        // Timer.setText("Time: "+Time+" - Date : "+Date+"");
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(menu);
        //Drawer Control
        try {
            AnchorPane main = FXMLLoader.load(getClass().getResource("/views/index.fxml"));
            main.getHeight();
            main.getWidth();
            body.setCenter(main);

        } catch (IOException ex) {
            Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/views/drawer.fxml"));
            box.getHeight();
            box.getWidth();

            drawer.setSidePane(box);

            for (Node node : box.getChildren()) {

                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getAccessibleText()) {
                            case "home": {
                                try {

                                    AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/index.fxml"));
                                    gpane.getHeight();
                                    gpane.getWidth();
                                    body.setCenter(gpane);

                                } catch (IOException ex) {
                                    Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            }
                            case "login": {
                                try {
                                    AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                                    gpane.getHeight();
                                    gpane.getWidth();
                                    body.setCenter(gpane);

                                } catch (IOException ex) {
                                    Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            }
                            case "registration": {
                                try {

                                    if (movelevel != null) {
                                        if (movelevel.equals("Admin Access")) {
                                            AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/registration.fxml"));
                                            gpane.getHeight();
                                            gpane.getWidth();
                                            body.setCenter(gpane);

                                        } else {
                                            AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                                            gpane.getHeight();
                                            gpane.getWidth();
                                            body.setCenter(gpane);

                                        }
                                    } else {

                                    }

                                } catch (IOException ex) {
                                    Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            }
                            case "report": {
                                try {

                                    if (movelevel != null) {
                                        if (movelevel.equals("Admin Access")) {
                                            AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/report.fxml"));
                                            gpane.getHeight();
                                            gpane.getWidth();
                                            body.setCenter(gpane);

                                        } else {
                                            AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                                            gpane.getHeight();
                                            gpane.getWidth();
                                            body.setCenter(gpane);

                                        }
                                    } else {

                                    }

                                } catch (IOException ex) {
                                    Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            }
                            case "settings": {
                                try {

                                    if (movelevel != null) {
                                        if (movelevel.equals("Admin Access")) {
                                            AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/settings.fxml"));
                                            gpane.getHeight();
                                            gpane.getWidth();
                                            body.setCenter(gpane);

                                        } else {
                                            AnchorPane gpane = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                                            gpane.getHeight();
                                            gpane.getWidth();
                                            body.setCenter(gpane);

                                        }
                                    } else {

                                    }

                                } catch (IOException ex) {
                                    Logger.getLogger(TP_SectionController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                break;
                            }
                        }
                    });
                }
            }
            transition.setRate(-1);
            menu.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                transition.setRate(transition.getRate() * -1);
                transition.play();
                if (drawer.isShown()) {
                    drawer.setStyle("-fx-padding: 0;");
                    drawer.close();

                } else {
                    drawer.open();
//                    
                    drawer.setStyle("-fx-padding:0 10px 0 10px;");
                    if (movelevel != null) {
                        if (movelevel.equals("Admin Access")) {
                            user.setText(user_name);
//                            yourController.report.setDisable(false);
//                            yourController.register.setDisable(false);
//                            yourController.setting.setDisable(false);
//                        functions.Login.getInstance().getButton1().isDisable();
//
//                        report.setDisable(false);
//                        setting.setDisable(false);
                            System.out.println("I am Here");

                        } else {

//                            register.setDisable(true);
//                            report.setDisable(true);
//                            setting.setDisable(true);
//                            System.out.println("I am not");
                        }
                    } else {

                    }
                }

            });
        } catch (IOException ex) {

        }

        //Connection Testing Code
        try {
            conn = Sqlite_Connection.DBConnect();
            String sql = "select Username From USER";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            dbStatus.setText("Connected");
            dbStatus.setStyle("-fx-text-Fill:green;-fx-background-color:white;");

            rs.close();
            pst.close();
        } catch (Exception e) {
            System.out.println(e);
            dbStatus.setText("Disconnected");
            dbStatus.setStyle("-fx-text-Fill:red;-fx-background-color:white;");
        } finally {
            try {
                rs.close();
                System.out.println("ResultSet Closed");
            } catch (Exception e) {
                /* ignored */ }
            try {
                pst.close();
                System.out.println("PreparedStatement Closed");
            } catch (Exception e) {
                /* ignored */ }
            try {
                conn.close();
                System.out.println("Connection Closed");
            } catch (Exception e) {
                /* ignored */ }

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(1000); //buy little millieseconds
                    } catch (InterruptedException e) {
                    }

                    Platform.runLater(() -> {

                        user.setText(user_name);

                    });

                }

            }
        }).start();
        
//        invalidate();
    }

}
