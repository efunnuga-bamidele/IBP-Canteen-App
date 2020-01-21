/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import connection.Sqlite_Connection;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author DaCodin
 */
public class IndexController implements Initializable {

    String id = null;
    String checkid = null;
    String getMeal = null;
    int ProfileStatus = 0;
    String TicketNo = null;

    String Right = null;
    String stLog = null;
    String ndLog = null;
    String rdLog = null;
    //used to check initial log after id is selected
    String preLog = null;
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
    private int period;
    String Picsname = null;
    String getPicsname = null;
    String checkName = null;
    String checkAID = null;

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView picture;
    @FXML
    private JFXTextField requestID;
    @FXML
    private Label bioInfo;

    @FXML
    private Label feedbackInfo;

    @FXML
    private Label printStatus;

    @FXML
    private JFXTextField surname;

    @FXML
    private JFXTextField firstname;

    @FXML
    private JFXTextField othername;

    @FXML
    private JFXTextField staffid;

    @FXML
    private JFXTextField department;

    @FXML
    private JFXTextField subdepartment;
    @FXML
    private JFXButton acceptBTN;

    @FXML
    private JFXButton cancleBTN;
    @FXML
    private JFXTextField category;
    @FXML
    private JFXTextField duty;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextArea printout;

////////////////////////////////////////////////////////////////////////////////
//get STAFF DETAILS FROM ID NUMBER
    public void selectedAction() {
        getMeal = null;
        try {

            ProfileStatus = 0;
            picture.setImage(new Image("/image/Picture.png"));
            picture.setFitWidth(239);
            picture.setFitHeight(179);;
            picture.setPreserveRatio(true);
            picture.setSmooth(true);
            picture.setCache(true);
            conn = Sqlite_Connection.DBConnect();
            String query = "select * from STAFF_DETAILS where Staff_ID =?";
            pst = conn.prepareStatement(query);
            pst.setString(1, "IBP-" + requestID.getText().toUpperCase());

            // clear();
            rs = pst.executeQuery();
            if (rs.next()) {

                id = rs.getString("RID");
                checkAID = rs.getString("Staff_ID");

                staffid.setText(rs.getString("Staff_ID"));
                surname.setText(rs.getString("Surname"));
                checkName = rs.getString("Surname");
                firstname.setText(rs.getString("Firstname"));
                othername.setText(rs.getString("Othername"));
                department.setText(rs.getString("Department"));
                subdepartment.setText(rs.getString("Sub_Department"));
                category.setText(rs.getString("Category"));
                duty.setText(rs.getString("Duty"));
                Right = rs.getString("Right");

                getMeal = rs.getString("Right");
                Picsname = rs.getString("Picture");

                pst.execute();
                //Enable Print Function Buttons if details are found
                acceptBTN.setDisable(false);
                cancleBTN.setDisable(false);
                //Gives feedback that printing out is ready
                feedbackInfo.setStyle("-fx-text-Fill:green;-fx-background-color:white;");
                feedbackInfo.setText("Success: User Ticket is ready to be printed out (Print = 0)");

                //If picture does not exist in db use default picture
                if (Picsname.equals(null)) {
                    picture.setImage(new Image("/image/Picture.png"));
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);;
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);
                    //Else use picture of the profile
                } else {

                    //  String dir = "prof/" + Picsname + ".jpg";
                    String dir = "prof/" + Picsname + "";

                    String path = dir;

                    File file = new File(dir);
                    Image image = new Image(file.toURI().toString());
                    picture.setImage(image);
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                }

            } else {
                feedbackInfo.setStyle("-fx-text-Fill:red;-fx-background-color:white;");
                feedbackInfo.setText("Failed: User ID is not Valid (Print = 0)");

            }

        } catch (Exception ex) {

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
            //Verify PreLogging //if basic information has been logged for that day
            iniVerifyAction();
            if (preLog == "Logged") {
                System.out.println("Already Logged");
            } else {
                //Register Data
                reg2();
            }

        }

    } //

    //Verify if user has been logged for that day
    public void iniVerifyAction() {
        preLog = null;
        try {
            conn = Sqlite_Connection.DBConnect();

            String query = "select * from REPORT_TABLE where Staff_ID =?  AND MainDate = ?";
            //result.getSelectionModel().select(0);

            pst = conn.prepareStatement(query);
            pst.setString(1, staffid.getText());
            pst.setString(2, Date);

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                preLog = "Logged";
            }

        } catch (Exception ex) {

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

    } //

    //Register basic details when id is verified
    public void reg2() {
        if ((surname.getText().equals("")) || (firstname.getText().equals("")) || (othername.getText().equals("")) || (staffid.getText().equals(""))) {

        } else {
            String query = "INSERT INTO REPORT_TABLE(Staff_ID, Surname, Firstname, Othername, Department,"
                    + " Sub_Department,Category, Duty,MainDate,Meal_Right)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";//9 INPUTS

            try {
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, staffid.getText().toUpperCase());
                pst.setString(2, surname.getText().toUpperCase());
                pst.setString(3, firstname.getText().toUpperCase());
                pst.setString(4, othername.getText().toUpperCase());
                pst.setString(5, department.getText().toUpperCase());
                pst.setString(6, subdepartment.getText().toUpperCase());
                pst.setString(7, category.getText().toUpperCase());
                pst.setString(8, duty.getText().toUpperCase());
                pst.setString(9, Date);
                pst.setString(10, getMeal);
                pst.execute();

                System.out.println("Save");

            } catch (Exception e) {
                System.out.println(e);
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

        }

    }  //
////////////////////////////////////////////////////////////////////////////////
    //Dynamic Tyme and Date

    public void CurrentDate() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            GregorianCalendar cal = new GregorianCalendar();
            Calendar xcal = Calendar.getInstance();
            xcal.add(Calendar.DATE, 0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date = fmt.format(xcal.getTime());
            // timer.setText(fmt.format(xcal.getTime()));
//            int month = cal.get(2);
//            int day = cal.get(5);
//            int year = cal.get(1);
//           // Date = day + "/" + (month + 1) + "/" + year;
//            Date = year + "-" + (month + 1) + "-" + day;
//            Month = String.valueOf((month + 1));
//            Day = String.valueOf(day);

//            Calendar cal = Calendar.getInstance();
            second = cal.get(Calendar.SECOND);
            minute = cal.get(Calendar.MINUTE);
            hour = cal.get(Calendar.HOUR);
            //System.out.println(hour + ":" + (minute) + ":" + second);
           // Time = dateFormat.format(xcal.getTime());
           // Time = "Time : " + hour + " H :" + (minute) + " M :" + second + " S" +" " + period;
            SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
              Time = formatDate.format(xcal.getTime());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

//Verify if Date Section if filled
    public void VerifyAction() {
        checkid = null;
        stLog = null;
        ndLog = null;
        rdLog = null;
        try {
            conn = Sqlite_Connection.DBConnect();
            // searchResult searchR = (searchResult) result.getSelectionModel().getSelectedItem();

           // String query = "select * from REPORT_TABLE where Staff_ID =?  AND Surname = ?";
            String query = "select * from REPORT_TABLE where Staff_ID =?";
            //result.getSelectionModel().select(0);

            pst = conn.prepareStatement(query);
            pst.setString(1, checkAID);
           // pst.setString(2, surname.getText());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                stLog = rs.getString("First_Log");
                ndLog = rs.getString("Second_Log");
                rdLog = rs.getString("Third_Log");
                checkid = rs.getString("RID");

            }

        } catch (Exception ex) {

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

    }

    @FXML
    void cancel(ActionEvent event) {
        Oclear();
    }

    ///////////////////////////////////////////////////////////////////////////
//meal logs save function
    public void SaveAction() {
        VerifyAction();

        if (Right.equalsIgnoreCase("1 Meal")) {
            System.out.println(Right);
            if (stLog == null) {
                System.out.println("Printing");
                reg1();
                feedbackInfo.setStyle("-fx-text-Fill:black;-fx-background-color:green;");
                feedbackInfo.setText("Success: User Ticket is being Printed out (Print = 0)");
                printStatus.setText("Please Pick Your Ticket at the Printer");

            } else {
                feedbackInfo.setStyle("-fx-text-Fill:white;-fx-background-color:red;");
                feedbackInfo.setText("Failed: User Has Already Printed out Ticket Today (Print = 1)");
                System.out.println("Not Printing");
            }

        } else if (Right.equalsIgnoreCase("2 Meals")) {
            System.out.println(Right);
            if (stLog == null && ndLog == null) {
                System.out.println("Printing");
                reg1();
                feedbackInfo.setStyle("-fx-text-Fill:black;-fx-background-color:green;");
                feedbackInfo.setText("Success: User Ticket is being Printed out (Print = 0)");
                printStatus.setText("Please Pick Your Ticket at the Printer");

            } else if (stLog != null && ndLog == null) {
                System.out.println("Printing");
                reg3();
                feedbackInfo.setStyle("-fx-text-Fill:black;-fx-background-color:green;");
                feedbackInfo.setText("Success: User Ticket is being Printed out (Print = 1)");
                printStatus.setText("Please Pick Your Ticket at the Printer");

            } else {
                feedbackInfo.setStyle("-fx-text-Fill:white;-fx-background-color:red;");
                feedbackInfo.setText("Failed: User Has Already Printed out Ticket Today (Print = 2)");
                System.out.println("Not Printing");
            }

        } else if (Right.equalsIgnoreCase("3 Meals")) {
            System.out.println(Right);
            if (stLog == null && ndLog == null && rdLog == null) {
                System.out.println("Printing");
                reg1();
                feedbackInfo.setStyle("-fx-text-Fill:black;-fx-background-color:green;");
                feedbackInfo.setText("Success: User Ticket is being Printed out (Print = 0)");
                printStatus.setText("Please Pick Your Ticket at the Printer");

            } else if (stLog != null && ndLog == null && rdLog == null) {
                System.out.println("Printing");
                reg3();
                feedbackInfo.setStyle("-fx-text-Fill:black;-fx-background-color:green;");
                feedbackInfo.setText("Success: User Ticket is being Printed out (Print = 1)");
                printStatus.setText("Please Pick Your Ticket at the Printer");

            } else if (stLog != null && ndLog != null && rdLog == null) {
                System.out.println("Printing");
                reg4();
                feedbackInfo.setStyle("-fx-text-Fill:black;-fx-background-color:green;");
                feedbackInfo.setText("Success: User Ticket is being Printed out (Print = 2)");
                printStatus.setText("Please Pick Your Ticket at the Printer");

            } else {
                feedbackInfo.setStyle("-fx-text-Fill:white;-fx-background-color:red;");
                feedbackInfo.setText("Failed: User Has Already Printed out Ticket Today (Print = 3)");
                System.out.println("Not Printing");
            }

        }

    }//

    //first meal registration
    public void reg1() {

        String query = "UPDATE REPORT_TABLE SET First_Log = ?,First_Log_Time = ?,First_Log_Date = ?"
                + "WHERE RID ='" + checkid + "' ";//11 inputs

        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);

            pst.setString(1, "Printed");
            pst.setString(2, Time);
            pst.setString(3, Date);

            pst.execute();
            System.out.println("Save");
            print();

        } catch (Exception e) {
            System.out.println(e);

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
    }//

    //second meal registration
    public void reg3() {
//        String query = "INSERT INTO REPORT_TABLE(Staff_ID, Surname, Firstname, Othername, Department,"
//                + " Sub_Department,Category,MainDate,First_Log,First_Log_Time,First_Log_Date,Second_Log,Second_Log_Time"
//                + ",Second_Log_Date,Third_Log,Third_Log_Time,Third_Log_Date)"
//                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//17 INPUTS

        String query = "UPDATE REPORT_TABLE SET Second_Log = ?,Second_Log_Time = ?,Second_Log_Date = ?"
                + "WHERE RID ='" + checkid + "' ";//11 inputs

        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);

            pst.setString(1, "Printed");
            pst.setString(2, Time);
            pst.setString(3, Date);

            pst.execute();
            System.out.println("Save");
            print();

        } catch (Exception e) {
            System.out.println(e);

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
    }//

//third meal registration
    public void reg4() {
//        String query = "INSERT INTO REPORT_TABLE(Staff_ID, Surname, Firstname, Othername, Department,"
//                + " Sub_Department,Category,MainDate,First_Log,First_Log_Time,First_Log_Date,Second_Log,Second_Log_Time"
//                + ",Second_Log_Date,Third_Log,Third_Log_Time,Third_Log_Date)"
//                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//17 INPUTS

        String query = "UPDATE REPORT_TABLE SET Third_Log = ?,Third_Log_Time = ?,Third_Log_Date = ?"
                + "WHERE RID ='" + checkid + "' ";//11 inputs

        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);

            pst.setString(1, "Printed");
            pst.setString(2, Time);
            pst.setString(3, Date);

            pst.execute();
            System.out.println("Save");
            print();

        } catch (Exception e) {
            System.out.println(e);

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
    }//

    @FXML
    //yes button function
    void printAction(ActionEvent event) {
        SaveAction();
        //printout.setText(Date);

    }//
    ////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//initialize dynamic system date 
        CurrentDate();
        feedbackInfo.setStyle("-fx-padding:0 10px 0 10px;");
        feedbackInfo.setText("");
        requestID.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    // do something
                    selectedAction();
                }
            }
        });

//New Code to Always place focus on Staff ID textfield
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(100); //buy little millieseconds
                    } catch (InterruptedException e) {
                    }

                    Platform.runLater(() -> {
                        requestID.requestFocus();
                    });
                }

            }
        }).start();
//clear fields after 3 minutes of no activity
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(30000); //buy little millieseconds
                    } catch (InterruptedException e) {
                    }

                    Platform.runLater(() -> {
                        requestID.setText("");
                        requestID.requestFocus();
                        id = "";
                        staffid.setText("");
                        surname.setText("");
                        firstname.setText("");
                        othername.setText("");
                        department.setText("");
                        subdepartment.setText("");
                        category.setText("");
                        acceptBTN.setDisable(true);
                        cancleBTN.setDisable(true);
                        feedbackInfo.setStyle("-fx-padding:0 10px 0 10px;");
                        feedbackInfo.setText("");
                        printStatus.setText("");
                        picture.setImage(new Image("/image/Picture.png"));
                        picture.setFitWidth(187);
                        picture.setFitHeight(150);;
                        picture.setPreserveRatio(true);
                        picture.setSmooth(true);
                        picture.setCache(true);
                        //bring your UI on top of everyone
                    });
                }

            }
        }).start();
    }

    @FXML
    void overclear(ActionEvent event) {
        Oclear();
    }

    public void Oclear() {
        requestID.setText("");
        requestID.requestFocus();
        id = "";
        staffid.setText("");
        surname.setText("");
        firstname.setText("");
        othername.setText("");
        department.setText("");
        subdepartment.setText("");
        category.setText("");
        acceptBTN.setDisable(true);
        cancleBTN.setDisable(true);
        feedbackInfo.setStyle("-fx-padding:0 10px 0 10px;");
        feedbackInfo.setText("");
        printStatus.setText("");
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(187);
        picture.setFitHeight(150);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);
        duty.setText("");
    }
//Send to Printer
    public void print() {
        String dump = "International Breweries Canteen Ticket"
                + "\n"
                + "-----------------------------------------------"
                + "\n"
                + "Name: " + surname.getText() + " " + firstname.getText() + " " + othername.getText()
                + "\n"
                + "Department: " + department.getText()
                + "\n"
                + "Sub Department: " + subdepartment.getText()
                + "\n"
                + "Duty: " + duty.getText()
                + "\n"
                + "Meal Right: " + Right
                + "\n"
                + "Ticket Date: " + Date
                + "\n"
                + "Ticket Date: " + Time
                + "\n"
                + "-----------------------------------------------"
                + "\n"
                + "Ticket No#: " + Long.toHexString(Double.doubleToLongBits(Math.random()));

        Node node = new TextArea(dump);

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        }
    }

}
