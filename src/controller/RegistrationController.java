/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;

import connection.Sqlite_Connection;
import functions.searchResult;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import java.awt.event.*;

//import java.awt.event.KeyEvent;
/**
 * FXML Controller class
 *
 * @author DaCodin
 */
public class RegistrationController implements Initializable {

    FileInputStream fileIn;
    public static PreparedStatement pst;
    public static ResultSet rs;
    public static Connection conn;
    String id = null;
    String staffT = "Staff";
    String visitorT = "Visitor";
    String contractorT = "Contractor";
    String viewname;

    //ROBOT VAIRABLES
    int toMove1 = 0;
    int Moves1 = 0;
    //Stage stage;

    //File k; //image file declaration
    int s = 0;
    byte[] personimage = null; //store image for new image selection
    byte[] personimage2 = null;  // store image for old image selection
    File f;  //image file declaration from filechooser
    private FileInputStream fis;
    String filename = null; //filechooser file name on selection from dialog
    //for browse image button
    int ProfileStatus2 = 0; //changes to 1 if new image was selected or 0 if no new image was selected

    String getPicsname = null;

    @FXML
    TableView<searchResult> result = new TableView<>(); //Registered Workers Table
    final ObservableList<searchResult> data = FXCollections.observableArrayList();
    //private ObservableList<String> dataa;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField sname;

    @FXML
    private JFXTextField sdepartment;
    @FXML
    private JFXTextField ssid;
    @FXML
    private JFXTextField duty;

    @FXML
    private JFXButton csearch;

    @FXML
    private JFXButton save;

    @FXML
    private JFXButton update;

    @FXML
    private JFXButton delete;

    @FXML
    private JFXButton clear;

    @FXML
    private AnchorPane main;

    @FXML
    private VBox main1;

    @FXML
    private ImageView picture;

    @FXML
    private JFXButton browseImage;

    @FXML
    void getImg(ActionEvent event) {

        getPics();

    }

    //function to get profile picture using filechooser
    public void getPics() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Profile Picture");
        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

//        chooser.showOpenDialog(null);
        f = chooser.showOpenDialog(null);

        filename = f.getAbsolutePath();
        //String path = filename;

        try {

            fis = new FileInputStream(f);
            if (fis != null) {
                ProfileStatus2 = 1;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
                personimage = bos.toByteArray();
                picture.setImage(new Image(f.toURI().toString(), 239, 179, true, true));
                picture.setFitWidth(239);
                picture.setFitHeight(179);
                picture.setPreserveRatio(true);
                picture.setSmooth(true);
                picture.setCache(true);
            } else {
                ProfileStatus2 = 0;
                //do nothing
            }

        } catch (Exception e) {

        }
    }
    //function to get profile picture using directory

    @FXML
    private JFXTextField surname;

    @FXML
    private JFXTextField firstname;

    @FXML
    private JFXTextField othername;

    @FXML
    private JFXTextField staffID;

    @FXML
    public JFXComboBox<String> department;
    @FXML
    ObservableList<String> doptions = FXCollections.observableArrayList();

    public void showdept() {
        try {
            String query = "select DEPARTMENT from DEPARTMENT ";
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                doptions.add(rs.getString("DEPARTMENT"));
            }
        } catch (Exception ea) {

        }

    }

    @FXML
    private JFXComboBox<String> subdepartment;

    @FXML
    ObservableList<String> sdoptions = FXCollections.observableArrayList();

    public void showsdept() {
        try {
            String query = "select s_DEPARTMENT from SUB_DEPARTMENT ";
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                sdoptions.add(rs.getString("s_DEPARTMENT"));
            }
        } catch (Exception ea) {

        }

    }

    @FXML
    private JFXComboBox<String> category;
    @FXML
    ObservableList<String> coptions
            = FXCollections.observableArrayList(
                    "..Select Category",
                    "Staff",
                    "Contractor",
                    "Visitor"
            );

    @FXML
    private JFXComboBox<String> tright;
    @FXML
    ObservableList<String> roptions
            = FXCollections.observableArrayList(
                    "..Select Ticket Right",
                    "1 Meal",
                    "2 Meals",
                    "3 Meals"
            );
    @FXML
    private JFXComboBox<String> dutysel;
    @FXML
    ObservableList<String> odoptions
            = FXCollections.observableArrayList(
                    "..Select Duty",
                    "Day Duty",
                    "Shift Duty"
            );

    @FXML
    void clearDetails(ActionEvent event) {
        clear();
    }

    @FXML
    void saveDetails(ActionEvent event) {
        SaveAction();
    }

    @FXML
    void deleteDetails(ActionEvent event) {
        deleteAction();
    }

    @FXML
    void editDetails(ActionEvent event) {
        editAction();
    }

    @FXML
    void ccDetails(ActionEvent event) {
        clear();
    }
    //function to show contractors table

    @FXML
    void sctable(ActionEvent event) {
        refresh();
        buildDataCT();

    }
    //function to show staffs table

    @FXML
    void sstable(ActionEvent event) {
        refresh();
        buildDataST();
    }
    //function to show visitors table

    @FXML
    void svtable(ActionEvent event) {
        refresh();
        buildDataVT();
    }

    public void SaveAction() {

//        String query = "INSERT INTO STAFF_DETAILS(Staff_ID, Surname, Firstname, Othername)"
//                + " VALUES (?,?,?,?)";//10 INPUTS
        //database code
        if (fis != null) {
            String query = "INSERT INTO STAFF_DETAILS(Staff_ID, Surname, Firstname, Othername, Department,"
                    + " Sub_Department,Category,Duty,Right,Picture)"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)";//9 INPUTS
            try {
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, "IBP-" + staffID.getText().toUpperCase());
                pst.setString(2, surname.getText().toUpperCase());
                pst.setString(3, firstname.getText().toUpperCase());
                pst.setString(4, othername.getText().toUpperCase());
                pst.setString(5, (String) department.getSelectionModel().getSelectedItem());
                pst.setString(6, (String) subdepartment.getSelectionModel().getSelectedItem());
                pst.setString(7, (String) category.getSelectionModel().getSelectedItem());
                pst.setString(8, (String) dutysel.getSelectionModel().getSelectedItem());
                pst.setString(9, (String) tright.getSelectionModel().getSelectedItem());

                String dir = "prof";
                File d = new File(dir);
                if (d.exists()) {

                } else {
                    d.mkdir();
                }

                String alphabet = "abcdefghijklmnopqrstuvwxyz";
                String picsname = "";
                Random random = new Random();
                int randomLen = 1 + random.nextInt(5);
                for (int i = 0; i < randomLen; i++) {
                    char c = alphabet.charAt(random.nextInt(26));
                    picsname += c;
                }
                picsname += staffID.getText();
                filename = f.getAbsolutePath();
                fis = new FileInputStream(f);
                OutputStream os = new FileOutputStream(new File(dir + "/" + "" + picsname));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = fis.read(content)) != -1) {
                    os.write(content, 0, size);

                }
                os.close();
                //
                pst.setString(10, picsname);
                pst.execute();
//          
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Details Saved Successfully", 3000);
                fis.close();

                clear();
//            System.out.println("Save");

            } catch (Exception e) {
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("ERROR : " + e, 3000);

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

        } else {
            try {
                String query = "INSERT INTO STAFF_DETAILS(Staff_ID, Surname, Firstname, Othername, Department,"
                        + " Sub_Department,Category,Duty,Right)"
                        + " VALUES (?,?,?,?,?,?,?,?,?)";//9 INPUTS
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, "IBP-" + staffID.getText().toUpperCase());
                pst.setString(2, surname.getText().toUpperCase());
                pst.setString(3, firstname.getText().toUpperCase());
                pst.setString(4, othername.getText().toUpperCase());
                pst.setString(5, (String) department.getSelectionModel().getSelectedItem());
                pst.setString(6, (String) subdepartment.getSelectionModel().getSelectedItem());
                pst.setString(7, (String) category.getSelectionModel().getSelectedItem());
                pst.setString(8, (String) dutysel.getSelectionModel().getSelectedItem());
                pst.setString(9, (String) tright.getSelectionModel().getSelectedItem());

                pst.execute();
//          
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Details Saved Successfully", 3000);
               

                clear();
//            System.out.println("Save");

            } catch (Exception e) {
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("ERROR : " + e, 3000);

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

    }

    public void editfunc() {

        if ((surname.getText().equals("")) || (firstname.getText().equals("")) || (othername.getText().equals("")) || (staffID.getText().equals(""))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {
            if (ProfileStatus2 == 1) {
                String query = "UPDATE STAFF_DETAILS SET RID = ?, Staff_ID = ?,Surname = ?,Firstname = ?,"
                        + " Othername= ?, Department= ?,Sub_Department= ?, Category= ?,Duty= ?, Right= ?, Picture =  ? "
                        + "WHERE RID ='" + id + "' ";//11 inputs

                //database code
                try {
                    conn = Sqlite_Connection.DBConnect();
                    pst = conn.prepareStatement(query);
                    pst.setString(1, id);
                    pst.setString(2, staffID.getText().toUpperCase());
                    pst.setString(3, surname.getText().toUpperCase());
                    pst.setString(4, firstname.getText().toUpperCase());
                    pst.setString(5, othername.getText().toUpperCase());
                    pst.setString(6, (String) department.getSelectionModel().getSelectedItem());
                    pst.setString(7, (String) subdepartment.getSelectionModel().getSelectedItem());
                    pst.setString(8, (String) category.getSelectionModel().getSelectedItem());
                    pst.setString(9, (String) dutysel.getSelectionModel().getSelectedItem());
                    pst.setString(10, (String) tright.getSelectionModel().getSelectedItem());

                    if (fis != null) {
                        String dir = "prof";
                        File d = new File(dir);

                        File f1 = new File("prof/" + getPicsname);
                        f1.delete();

                        String alphabet = "abcdefghijklmnopqrstuvwxyz";
                        String picsname = "";
                        Random random = new Random();
                        int randomLen = 1 + random.nextInt(5);
                        for (int i = 0; i < randomLen; i++) {
                            char c = alphabet.charAt(random.nextInt(26));
                            picsname += c;
                        }
                        picsname += staffID.getText();
                        filename = f.getAbsolutePath();
                        fis = new FileInputStream(f);
                        OutputStream os = new FileOutputStream(new File(dir + "/" + "" + picsname));
                        byte[] content = new byte[1024];
                        int size = 0;
                        while ((size = fis.read(content)) != -1) {
                            os.write(content, 0, size);
                        }
                        os.close();
                        //
                        pst.setString(11, picsname);

                    } else {
                        //pst.setString(10, "");
                    }

                    pst.execute();
                    JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                    snackbar.show("Updated Successfully", 3000);

                    clear();

                } catch (Exception e) {
                    //System.out.println(e);
                    JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                    snackbar.show("ERROR : " + e, 3000);

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
            } else {
                String query = "UPDATE STAFF_DETAILS SET RID = ?, Staff_ID = ?,Surname = ?,Firstname = ?,"
                        + " Othername= ?, Department= ?,Sub_Department= ?, Category= ?, Duty = ?, Right= ? "
                        + "WHERE RID ='" + id + "' ";//11 inputs

                //database code
                try {
                    conn = Sqlite_Connection.DBConnect();
                    pst = conn.prepareStatement(query);
                    pst.setString(1, id);
                    pst.setString(2, staffID.getText());
                    pst.setString(3, surname.getText().toUpperCase());
                    pst.setString(4, firstname.getText().toUpperCase());
                    pst.setString(5, othername.getText().toUpperCase());
                    pst.setString(6, (String) department.getSelectionModel().getSelectedItem());
                    pst.setString(7, (String) subdepartment.getSelectionModel().getSelectedItem());
                    pst.setString(8, (String) category.getSelectionModel().getSelectedItem());
                    pst.setString(9, (String) dutysel.getSelectionModel().getSelectedItem());
                    pst.setString(10, (String) tright.getSelectionModel().getSelectedItem());

                    pst.execute();
                    JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                    snackbar.show("Updated Successfully", 3000);
                    clear();

                } catch (Exception e) {
                    //System.out.println(e);
                    JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                    snackbar.show("ERROR : " + e, 3000);

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

        }
    }

    public void editAction() {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to update this details!", "Yes", 5000, e -> editfunc());

    }

    public void delFunc() {
        try {

            String query = "DELETE FROM STAFF_DETAILS WHERE RID = ?";
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id);
            pst.executeUpdate();

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("Information Deleted Sussessfully", 3000);
            File f1 = new File("prof/" + getPicsname);
            f1.delete();
            clear();

        } catch (SQLException ex) {
            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("Error " + ex, 3000);
            //System.out.print(ex);
            //Logger.getLogger(FormController.class.getName()).log(Level.SEVERE, null, ex);
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

    public void deleteAction() {

        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to delete this data!", "Yes", 5000, e -> delFunc());
    }

    public void clear() {
        id = null;
        cclear();
        refresh();
        buildData();
        surname.setText("");
        firstname.setText("");
        othername.setText("");
        staffID.setText("");
        department.setValue(null);
        subdepartment.setValue(null);
        category.setValue(null);
        dutysel.setValue("");
        tright.setValue(null);
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(239);
        picture.setFitHeight(179);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);

    }

    public void cclear() {
        sname.setText("");
        sdepartment.setText("");
        ssid.setText("IBP-");
        duty.setText("");

        refresh();
        buildData();

    }

    @FXML
    private void getResult(KeyEvent event) {
        //if(search_bar.getText() != null){
        // result.setVisible(true);
        FilteredList<searchResult> filterData = new FilteredList<>(data, e -> true);
        sname.textProperty().addListener((observablevalue, oldValue, newValue) -> {
            filterData.setPredicate((Predicate<? super searchResult>) filterdetail -> {

                if (newValue == null || newValue.isEmpty()) {
                    // result.setVisible(false);
                    return true;

                }
                String upperCaseFilter = newValue.toUpperCase();
                if (filterdetail.getSURNAME().toUpperCase().contains(upperCaseFilter)) {
                    //if(filterstaff.getUID().contains(newValue)){
                    // result.setVisible(true);
                    return true;

                }
                return false;
            });

            SortedList<searchResult> sortedData = new SortedList<>(filterData);
            // sortedData.comparatorProperty().bind(Student.comparatorProperty());

            result.setItems(sortedData);
            //}else{
            //   result.setVisible(false);
            // }
        });
    }

    @FXML
    private void getResult1(KeyEvent event) {
        //if(search_bar.getText() != null){
        // result.setVisible(true);
        FilteredList<searchResult> filterData = new FilteredList<>(data, e -> true);
        sdepartment.textProperty().addListener((observablevalue, oldValue, newValue) -> {
            filterData.setPredicate((Predicate<? super searchResult>) filterdetail -> {

                if (newValue == null || newValue.isEmpty()) {
                    // result.setVisible(false);
                    return true;

                }
                String upperCaseFilter = newValue.toUpperCase();
                if (filterdetail.getSUBDEPARTMENT().toUpperCase().contains(upperCaseFilter)) {
                    //if(filterstaff.getUID().contains(newValue)){
                    // result.setVisible(true);
                    return true;

                }
                return false;
            });

            SortedList<searchResult> sortedData = new SortedList<>(filterData);
            // sortedData.comparatorProperty().bind(Student.comparatorProperty());

            result.setItems(sortedData);
            //}else{
            //   result.setVisible(false);
            // }
        });
    }

    @FXML
    private void getResult2(KeyEvent event) {
        //if(search_bar.getText() != null){
        // result.setVisible(true);
        FilteredList<searchResult> filterData = new FilteredList<>(data, e -> true);
        ssid.textProperty().addListener((observablevalue, oldValue, newValue) -> {
            filterData.setPredicate((Predicate<? super searchResult>) filterdetail -> {

                if (newValue == null || newValue.isEmpty()) {
                    // result.setVisible(false);
                    return true;

                }
                String upperCaseFilter = newValue.toUpperCase();
                if (filterdetail.getSTAFFID().toUpperCase().contains(upperCaseFilter)) {
                    //if(filterstaff.getUID().contains(newValue)){
                    // result.setVisible(true);
                    return true;

                }
                return false;
            });

            SortedList<searchResult> sortedData = new SortedList<>(filterData);
            // sortedData.comparatorProperty().bind(Student.comparatorProperty());

            result.setItems(sortedData);
            //}else{
            //   result.setVisible(false);
            // }
        });
    }

    @FXML
    private void getResult3(KeyEvent event) {
        //if(search_bar.getText() != null){
        // result.setVisible(true);
        FilteredList<searchResult> filterData = new FilteredList<>(data, e -> true);
        duty.textProperty().addListener((observablevalue, oldValue, newValue) -> {
            filterData.setPredicate((Predicate<? super searchResult>) filterdetail -> {

                if (newValue == null || newValue.isEmpty()) {
                    // result.setVisible(false);
                    return true;

                }
                String upperCaseFilter = newValue.toUpperCase();
                if (filterdetail.getDUTY().toUpperCase().contains(upperCaseFilter)) {
                    //if(filterstaff.getUID().contains(newValue)){
                    // result.setVisible(true);
                    return true;

                }
                return false;
            });

            SortedList<searchResult> sortedData = new SortedList<>(filterData);
            // sortedData.comparatorProperty().bind(Student.comparatorProperty());

            result.setItems(sortedData);
            //}else{
            //   result.setVisible(false);
            // }
        });
    }

    @FXML
    void firstMeal(ActionEvent event) {
//    result.getItems().forEach(item -> item.setRIGHT("RIGHT").set("1 Meal"));
        massUpdate1();
    }

    @FXML
    void secondMeal(ActionEvent event) {
        massUpdate2();
    }

    @FXML
    void thirdMeal(ActionEvent event) {
        massUpdate3();
    }

    //get click response
    @FXML
    void selectedAction(MouseEvent event) {
        id = null;
        ProfileStatus2 = 0;
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(239);
        picture.setFitHeight(179);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);
        String Picsname = null;
        try {

            conn = Sqlite_Connection.DBConnect();
            searchResult searchR = (searchResult) result.getSelectionModel().getSelectedItem();

            String query = "select * from STAFF_DETAILS where RID =?";
            result.getSelectionModel().select(searchR);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getString("RID");
                staffID.setText(rs.getString("Staff_ID"));
                surname.setText(rs.getString("Surname"));
                firstname.setText(rs.getString("Firstname"));
                othername.setText(rs.getString("Othername"));
                department.setValue(rs.getString("Department"));
                subdepartment.setValue(rs.getString("Sub_Department"));
                category.setValue(rs.getString("Category"));
                dutysel.setValue(rs.getString("Duty"));
                tright.setValue(rs.getString("Right"));
                //image store in temp folder

                Picsname = rs.getString("Picture");
                pst.execute();

                getPicsname = Picsname;

//                System.out.println("Contains :"+ProfileStatus );
                if (Picsname.equals(null)) {
                    picture.setImage(new Image("/image/Picture.png"));
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);;
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

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
            }

        } catch (Exception ex) {
            System.out.println(ex);
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
    void kselectedAction(KeyEvent event) {
        id = null;
        ProfileStatus2 = 0;
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(239);
        picture.setFitHeight(179);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);
        String Picsname = null;
        try {

            conn = Sqlite_Connection.DBConnect();
            searchResult searchR = (searchResult) result.getSelectionModel().getSelectedItem();

            String query = "select * from STAFF_DETAILS where RID =?";
            result.getSelectionModel().select(searchR);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getString("RID");
                staffID.setText(rs.getString("Staff_ID"));
                surname.setText(rs.getString("Surname"));
                firstname.setText(rs.getString("Firstname"));
                othername.setText(rs.getString("Othername"));
                department.setValue(rs.getString("Department"));
                subdepartment.setValue(rs.getString("Sub_Department"));
                category.setValue(rs.getString("Category"));
                dutysel.setValue(rs.getString("Duty"));
                tright.setValue(rs.getString("Right"));
                //image store in temp folder

                Picsname = rs.getString("Picture");
                pst.execute();

                getPicsname = Picsname;

//                System.out.println("Contains :"+ProfileStatus );
                if (Picsname.equals(null)) {
                    picture.setImage(new Image("/image/Picture.png"));
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);;
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

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
            }

        } catch (Exception ex) {
            System.out.println(ex);
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
    void alltable(ActionEvent event) {
        refresh();
        buildData();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        department.setItems(doptions);
        subdepartment.setItems(sdoptions);
        category.setItems(coptions);
        tright.setItems(roptions);
        dutysel.setItems(odoptions);

        Rectangle clip = new Rectangle(
                picture.getFitWidth(), picture.getFitHeight()
        );
        clip.setArcWidth(500);
        clip.setArcHeight(1000);
        picture.setClip(clip);

        showdept();
        showsdept();

    }

    //table data
    public void buildData() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("STAFF ID");
        col2 = new TableColumn("STAFF ID");
        col2.setMinWidth(30);
        col2.setCellValueFactory(new PropertyValueFactory<>("STAFFID"));
        TableColumn col3 = new TableColumn("SURNAME");
        col3 = new TableColumn("SURNAME");
        // col3.setMinWidth(70);
        col3.setCellValueFactory(new PropertyValueFactory<>("SURNAME"));
        TableColumn col4 = new TableColumn("FIRSTNAME");
        col4 = new TableColumn("FIRSTNAME");
        //col4.setMinWidth(70);
        col4.setCellValueFactory(new PropertyValueFactory<>("FIRSTNAME"));
        TableColumn col5 = new TableColumn("OTHERNAME");
        col5 = new TableColumn("OTHERNAME");
        // col5.setMinWidth(70);
        col5.setCellValueFactory(new PropertyValueFactory<>("OTHERNAME"));
        TableColumn col6 = new TableColumn("DEPARTMENT");
        col6 = new TableColumn("DEPARTMENT");
        //col6.setMinWidth(70);
        col6.setCellValueFactory(new PropertyValueFactory<>("DEPARTMENT"));
        TableColumn col7 = new TableColumn("SUB DEPARTMENT");
        col7 = new TableColumn("SUB DEPARTMENT");
        //col6.setMinWidth(70);
        col7.setCellValueFactory(new PropertyValueFactory<>("SUBDEPARTMENT"));
        TableColumn col8 = new TableColumn("CATEGORY");
        col8 = new TableColumn("CATEGORY");
        //col6.setMinWidth(70);
        col8.setCellValueFactory(new PropertyValueFactory<>("CATEGORY"));
        TableColumn col9 = new TableColumn("DUTY");
        col9 = new TableColumn("DUTY");
        //col6.setMinWidth(70);
        col9.setCellValueFactory(new PropertyValueFactory<>("DUTY"));

        TableColumn<searchResult, StringProperty> col10 = new TableColumn<>("RIGHT");
        col10.setMinWidth(40);
        col10.setCellValueFactory(i -> {
            final StringProperty value = i.getValue().getRIGHT();
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });
        col10.setCellFactory(col -> {
            TableCell<searchResult, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(roptions);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;

        });

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        result.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from STAFF_DETAILS";

            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("RID");
                String staffid = rs.getString("Staff_ID");
                String surname = rs.getString("Surname");
                String firstname = rs.getString("Firstname");
                String othername = rs.getString("Othername");
                String department = rs.getString("Department");
                String sub_department = rs.getString("Sub_Department");
                String category = rs.getString("Category");
                String duty = rs.getString("Duty");
                String right = rs.getString("Right");

                data.add(new searchResult(id, staffid, surname, firstname,
                        othername, department, sub_department, category, duty, right));
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            result.getItems().setAll(data);
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
        System.out.print(result.getItems().size());

    }

    public void buildDataCT() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("STAFF ID");
        col2 = new TableColumn("STAFF ID");
        col2.setMinWidth(30);
        col2.setCellValueFactory(new PropertyValueFactory<>("STAFFID"));
        TableColumn col3 = new TableColumn("SURNAME");
        col3 = new TableColumn("SURNAME");
        // col3.setMinWidth(70);
        col3.setCellValueFactory(new PropertyValueFactory<>("SURNAME"));
        TableColumn col4 = new TableColumn("FIRSTNAME");
        col4 = new TableColumn("FIRSTNAME");
        //col4.setMinWidth(70);
        col4.setCellValueFactory(new PropertyValueFactory<>("FIRSTNAME"));
        TableColumn col5 = new TableColumn("OTHERNAME");
        col5 = new TableColumn("OTHERNAME");
        // col5.setMinWidth(70);
        col5.setCellValueFactory(new PropertyValueFactory<>("OTHERNAME"));
        TableColumn col6 = new TableColumn("DEPARTMENT");
        col6 = new TableColumn("DEPARTMENT");
        //col6.setMinWidth(70);
        col6.setCellValueFactory(new PropertyValueFactory<>("DEPARTMENT"));
        TableColumn col7 = new TableColumn("SUB DEPARTMENT");
        col7 = new TableColumn("SUB DEPARTMENT");
        //col6.setMinWidth(70);
        col7.setCellValueFactory(new PropertyValueFactory<>("SUBDEPARTMENT"));
        TableColumn col8 = new TableColumn("CATEGORY");
        col8 = new TableColumn("CATEGORY");
        //col6.setMinWidth(70);
        col8.setCellValueFactory(new PropertyValueFactory<>("CATEGORY"));
        TableColumn col9 = new TableColumn("DUTY");
        col9 = new TableColumn("DUTY");
        //col6.setMinWidth(70);
        col9.setCellValueFactory(new PropertyValueFactory<>("DUTY"));

//        TableColumn col9 = new TableColumn("MEAL RIGHT");
        //        col9 = new TableColumn("MEAL RIGHT");
        //        //col6.setMinWidth(70);
        //        col9.setCellValueFactory(new PropertyValueFactory<>("RIGHT"));
        TableColumn<searchResult, StringProperty> col10 = new TableColumn<>("RIGHT");
        col10.setMinWidth(40);
        col10.setCellValueFactory(i -> {
            final StringProperty value = i.getValue().getRIGHT();
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });
        col10.setCellFactory(col -> {
            TableCell<searchResult, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(roptions);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;

        });

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        result.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String contractorT = "Contractor";
            String SQL = "SELECT * from STAFF_DETAILS WHERE Category = \"Contractor\"";
            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("RID");
                String staffid = rs.getString("Staff_ID");
                String surname = rs.getString("Surname");
                String firstname = rs.getString("Firstname");
                String othername = rs.getString("Othername");
                String department = rs.getString("Department");
                String sub_department = rs.getString("Sub_Department");
                String category = rs.getString("Category");
                String duty = rs.getString("Duty");
                String right = rs.getString("Right");
                /*boolean approval;
                if(rs.getString("approval") =="1"){
                   approval = Boolean.TRUE;
                }else{
                   approval = Boolean.FALSE; 
                }*/

                data.add(new searchResult(id, staffid, surname, firstname,
                        othername, department, sub_department, category, duty, right));

                //tableview = new TableView();
                //buildData();
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            result.getItems().setAll(data);
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
        System.out.print(result.getItems().size());

    }

    public void buildDataVT() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("STAFF ID");
        col2 = new TableColumn("STAFF ID");
        col2.setMinWidth(30);
        col2.setCellValueFactory(new PropertyValueFactory<>("STAFFID"));
        TableColumn col3 = new TableColumn("SURNAME");
        col3 = new TableColumn("SURNAME");
        // col3.setMinWidth(70);
        col3.setCellValueFactory(new PropertyValueFactory<>("SURNAME"));
        TableColumn col4 = new TableColumn("FIRSTNAME");
        col4 = new TableColumn("FIRSTNAME");
        //col4.setMinWidth(70);
        col4.setCellValueFactory(new PropertyValueFactory<>("FIRSTNAME"));
        TableColumn col5 = new TableColumn("OTHERNAME");
        col5 = new TableColumn("OTHERNAME");
        // col5.setMinWidth(70);
        col5.setCellValueFactory(new PropertyValueFactory<>("OTHERNAME"));
        TableColumn col6 = new TableColumn("DEPARTMENT");
        col6 = new TableColumn("DEPARTMENT");
        //col6.setMinWidth(70);
        col6.setCellValueFactory(new PropertyValueFactory<>("DEPARTMENT"));
        TableColumn col7 = new TableColumn("SUB DEPARTMENT");
        col7 = new TableColumn("SUB DEPARTMENT");
        //col6.setMinWidth(70);
        col7.setCellValueFactory(new PropertyValueFactory<>("SUBDEPARTMENT"));
        TableColumn col8 = new TableColumn("CATEGORY");
        col8 = new TableColumn("CATEGORY");
        //col6.setMinWidth(70);
        col8.setCellValueFactory(new PropertyValueFactory<>("CATEGORY"));
        TableColumn col9 = new TableColumn("DUTY");
        col9 = new TableColumn("DUTY");
        //col6.setMinWidth(70);
        col9.setCellValueFactory(new PropertyValueFactory<>("DUTY"));

//        TableColumn col9 = new TableColumn("MEAL RIGHT");
        //        col9 = new TableColumn("MEAL RIGHT");
        //        //col6.setMinWidth(70);
        //        col9.setCellValueFactory(new PropertyValueFactory<>("RIGHT"));
        TableColumn<searchResult, StringProperty> col10 = new TableColumn<>("RIGHT");
        col10.setMinWidth(40);
        col10.setCellValueFactory(i -> {
            final StringProperty value = i.getValue().getRIGHT();
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });
        col10.setCellFactory(col -> {
            TableCell<searchResult, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(roptions);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;

        });

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        result.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String contractorT = "Contractor";
            String SQL = "SELECT * from STAFF_DETAILS WHERE Category = \"Visitor\"";
            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("RID");
                String staffid = rs.getString("Staff_ID");
                String surname = rs.getString("Surname");
                String firstname = rs.getString("Firstname");
                String othername = rs.getString("Othername");
                String department = rs.getString("Department");
                String sub_department = rs.getString("Sub_Department");
                String category = rs.getString("Category");
                String duty = rs.getString("Duty");
                String right = rs.getString("Right");
                /*boolean approval;
                if(rs.getString("approval") =="1"){
                   approval = Boolean.TRUE;
                }else{
                   approval = Boolean.FALSE; 
                }*/

                data.add(new searchResult(id, staffid, surname, firstname,
                        othername, department, sub_department, category, duty, right));

                //tableview = new TableView();
                //buildData();
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            result.getItems().setAll(data);
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
        System.out.print(result.getItems().size());

    }

    public void buildDataST() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("STAFF ID");
        col2 = new TableColumn("STAFF ID");
        col2.setMinWidth(30);
        col2.setCellValueFactory(new PropertyValueFactory<>("STAFFID"));
        TableColumn col3 = new TableColumn("SURNAME");
        col3 = new TableColumn("SURNAME");
        // col3.setMinWidth(70);
        col3.setCellValueFactory(new PropertyValueFactory<>("SURNAME"));
        TableColumn col4 = new TableColumn("FIRSTNAME");
        col4 = new TableColumn("FIRSTNAME");
        //col4.setMinWidth(70);
        col4.setCellValueFactory(new PropertyValueFactory<>("FIRSTNAME"));
        TableColumn col5 = new TableColumn("OTHERNAME");
        col5 = new TableColumn("OTHERNAME");
        // col5.setMinWidth(70);
        col5.setCellValueFactory(new PropertyValueFactory<>("OTHERNAME"));
        TableColumn col6 = new TableColumn("DEPARTMENT");
        col6 = new TableColumn("DEPARTMENT");
        //col6.setMinWidth(70);
        col6.setCellValueFactory(new PropertyValueFactory<>("DEPARTMENT"));
        TableColumn col7 = new TableColumn("SUB DEPARTMENT");
        col7 = new TableColumn("SUB DEPARTMENT");
        //col6.setMinWidth(70);
        col7.setCellValueFactory(new PropertyValueFactory<>("SUBDEPARTMENT"));
        TableColumn col8 = new TableColumn("CATEGORY");
        col8 = new TableColumn("CATEGORY");
        //col6.setMinWidth(70);
        col8.setCellValueFactory(new PropertyValueFactory<>("CATEGORY"));
        TableColumn col9 = new TableColumn("DUTY");
        col9 = new TableColumn("DUTY");
        //col6.setMinWidth(70);
        col9.setCellValueFactory(new PropertyValueFactory<>("DUTY"));

//        TableColumn col9 = new TableColumn("MEAL RIGHT");
        //        col9 = new TableColumn("MEAL RIGHT");
        //        //col6.setMinWidth(70);
        //        col9.setCellValueFactory(new PropertyValueFactory<>("RIGHT"));
        TableColumn<searchResult, StringProperty> col10 = new TableColumn<>("RIGHT");
        col10.setMinWidth(40);
        col10.setCellValueFactory(i -> {
            final StringProperty value = i.getValue().getRIGHT();
            // binding to constant value
            return Bindings.createObjectBinding(() -> value);
        });
        col10.setCellFactory(col -> {
            TableCell<searchResult, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(roptions);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;

        });

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        result.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String contractorT = "Contractor";
            String SQL = "SELECT * from STAFF_DETAILS WHERE Category = \"Staff\"";
            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("RID");
                String staffid = rs.getString("Staff_ID");
                String surname = rs.getString("Surname");
                String firstname = rs.getString("Firstname");
                String othername = rs.getString("Othername");
                String department = rs.getString("Department");
                String sub_department = rs.getString("Sub_Department");
                String category = rs.getString("Category");
                String duty = rs.getString("Duty");
                String right = rs.getString("Right");
                /*boolean approval;
                if(rs.getString("approval") =="1"){
                   approval = Boolean.TRUE;
                }else{
                   approval = Boolean.FALSE; 
                }*/

                data.add(new searchResult(id, staffid, surname, firstname,
                        othername, department, sub_department, category, duty, right));

                //tableview = new TableView();
                //buildData();
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            result.getItems().setAll(data);
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
        System.out.print(result.getItems().size());

    }

    //clear table
    public void refresh() {
        //tableview.getColumns().get(0).setVisible(false);
        result.getColumns().clear();
        data.removeAll(data);

    }

    @FXML
    void ImportEX(ActionEvent event) {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Registration Form");

        f = chooser.showOpenDialog(null);
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to update this details!", "Yes", 5000, e -> importFunc());

        // filename = f.getAbsolutePath();
    }
    HSSFWorkbook wb = null;
    HSSFSheet sheet = null;
    HSSFRow row = null;

    public void importFunc() {

        try {
            fileIn = new FileInputStream(new File(f.getAbsolutePath()));
            //String path = f.getAbsolutePath();

            wb = new HSSFWorkbook(fileIn);
            sheet = wb.getSheetAt(0);

            String query = "INSERT INTO STAFF_DETAILS(Staff_ID, Surname, Firstname, Othername, Department,"
                    + " Sub_Department,Category,Duty,Right)"
                    + " VALUES (?,?,?,?,?,?,?,?,?)";//8 INPUTS
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                pst.setString(1, ("IBP-" + (row.getCell(0).getStringCellValue())));
                pst.setString(2, row.getCell(1).getStringCellValue().toUpperCase());
                pst.setString(3, row.getCell(2).getStringCellValue().toUpperCase());
                pst.setString(4, row.getCell(3).getStringCellValue().toUpperCase());
                pst.setString(5, row.getCell(4).getStringCellValue());
                pst.setString(6, row.getCell(5).getStringCellValue());
                pst.setString(7, row.getCell(6).getStringCellValue());
                pst.setString(8, row.getCell(7).getStringCellValue());
                pst.setString(9, row.getCell(8).getStringCellValue());
                pst.execute();
            }

        } catch (FileNotFoundException ex) {
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("Details Imported Failed : " + ex, 3000);

        } catch (IOException | SQLException ex) {
            Logger.getLogger(RegistrationController.class
                    .getName()).log(Level.SEVERE, null, ex);
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("Details Imported Failed : " + ex, 3000);
        } finally {
            try {
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Details Imported Successfully", 3000);
                clear();
                fileIn.close();
                rs.close();
                System.out.println("ResultSet Closed");
            } catch (IOException | SQLException e) {
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

    public void massUpdate1() {

        toMove1 = result.getItems().size();
        try {

            com.sun.glass.ui.Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();

            for (int i = 0; i <= toMove1; i++) {
                result.requestFocus();
                result.getSelectionModel().select(i);
                result.getFocusModel().focus(i);
                robot.keyPress(KeyCode.ENTER.impl_getCode());
                System.out.println("Completed : " + i);
                System.out.println("Completed : " + id);
                maxSelectedAction1();

                robot.keyPress(KeyCode.DOWN.impl_getCode());
                Thread.sleep(10);

            }
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("Updated Successfully", 3000);
            clear();

        } catch (InterruptedException ex) {
            Logger.getLogger(RegistrationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get click response
    public void maxSelectedAction1() {
        id = null;
        ProfileStatus2 = 0;
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(239);
        picture.setFitHeight(179);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);
        String Picsname = null;
        try {

            conn = Sqlite_Connection.DBConnect();
            searchResult searchR = (searchResult) result.getSelectionModel().getSelectedItem();

            String query = "select * from STAFF_DETAILS where RID =?";
            result.getSelectionModel().select(searchR);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getString("RID");
                staffID.setText(rs.getString("Staff_ID"));
                surname.setText(rs.getString("Surname"));
                firstname.setText(rs.getString("Firstname"));
                othername.setText(rs.getString("Othername"));
                department.setValue(rs.getString("Department"));
                subdepartment.setValue(rs.getString("Sub_Department"));
                category.setValue(rs.getString("Category"));
                dutysel.setValue(rs.getString("Duty"));
                tright.setValue(rs.getString("Right"));
                //image store in temp folder

                Picsname = rs.getString("Picture");
                pst.execute();
                getPicsname = Picsname;

//                System.out.println("Contains :"+ProfileStatus );
                if (Picsname.equals(null)) {
                    picture.setImage(new Image("/image/Picture.png"));
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);;
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                } else {

                    String dir = "prof/" + Picsname + ".jpg";

                    String path = dir;
                    System.out.println("" + path + "" + Picsname + ".jpg");
                    File file = new File(dir);
                    Image image = new Image(file.toURI().toString());
                    picture.setImage(image);
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                }
            }
            pst.execute();
//                System.out.println("Contains :"+ProfileStatus );

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
        maxEditfunc1();

    }

    public void maxEditfunc1() {

        String query = "UPDATE STAFF_DETAILS SET RID = ?, Staff_ID = ?,Surname = ?,Firstname = ?,"
                + " Othername= ?, Department= ?,Sub_Department= ?, Category= ?, Duty= ?,Right= ? "
                + "WHERE RID ='" + id + "' ";//11 inputs

        //database code
        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id);
            pst.setString(2, staffID.getText().toUpperCase());
            pst.setString(3, surname.getText().toUpperCase());
            pst.setString(4, firstname.getText().toUpperCase());
            pst.setString(5, othername.getText().toUpperCase());
            pst.setString(6, (String) department.getSelectionModel().getSelectedItem());
            pst.setString(7, (String) subdepartment.getSelectionModel().getSelectedItem());
            pst.setString(8, (String) category.getSelectionModel().getSelectedItem());
            pst.setString(9, (String) dutysel.getSelectionModel().getSelectedItem());

            pst.setString(10, "1 Meal");
            pst.execute();

        } catch (Exception e) {
            //System.out.println(e);
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("ERROR : " + e, 3000);

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
                /* ignored */

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
    }

    public void massUpdate2() {

        toMove1 = result.getItems().size();
        try {

            com.sun.glass.ui.Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();

            for (int i = 0; i <= toMove1; i++) {
                result.requestFocus();
                result.getSelectionModel().select(i);
                result.getFocusModel().focus(i);
                robot.keyPress(KeyCode.ENTER.impl_getCode());
                System.out.println("Completed : " + i);
                System.out.println("Completed : " + id);
                maxSelectedAction2();

                robot.keyPress(KeyCode.DOWN.impl_getCode());
                Thread.sleep(10);

            }
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("Updated Successfully", 3000);
            clear();

        } catch (InterruptedException ex) {
            Logger.getLogger(RegistrationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get click response
    public void maxSelectedAction2() {
        id = null;
        ProfileStatus2 = 0;
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(239);
        picture.setFitHeight(179);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);
        String Picsname = null;
        try {

            conn = Sqlite_Connection.DBConnect();
            searchResult searchR = (searchResult) result.getSelectionModel().getSelectedItem();

            String query = "select * from STAFF_DETAILS where RID =?";
            result.getSelectionModel().select(searchR);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getString("RID");
                staffID.setText(rs.getString("Staff_ID"));
                surname.setText(rs.getString("Surname"));
                firstname.setText(rs.getString("Firstname"));
                othername.setText(rs.getString("Othername"));
                department.setValue(rs.getString("Department"));
                subdepartment.setValue(rs.getString("Sub_Department"));
                category.setValue(rs.getString("Category"));
                dutysel.setValue(rs.getString("Duty"));
                tright.setValue(rs.getString("Right"));
                //image store in temp folder

                Picsname = rs.getString("Picture");
                pst.execute();
                getPicsname = Picsname;

//                System.out.println("Contains :"+ProfileStatus );
                if (Picsname.equals(null)) {
                    picture.setImage(new Image("/image/Picture.png"));
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);;
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                } else {

                    String dir = "prof/" + Picsname + ".jpg";

                    String path = dir;
                    System.out.println("" + path + "" + Picsname + ".jpg");
                    File file = new File(dir);
                    Image image = new Image(file.toURI().toString());
                    picture.setImage(image);
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                }
            }

            pst.execute();
//                System.out.println("Contains :"+ProfileStatus );

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
        maxEditfunc2();

    }

    public void maxEditfunc2() {

        String query = "UPDATE STAFF_DETAILS SET RID = ?, Staff_ID = ?,Surname = ?,Firstname = ?,"
                + " Othername= ?, Department= ?,Sub_Department= ?, Category= ?, Duty= ?,Right= ? "
                + "WHERE RID ='" + id + "' ";//11 inputs

        //database code
        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id);
            pst.setString(2, staffID.getText().toUpperCase());
            pst.setString(3, surname.getText().toUpperCase());
            pst.setString(4, firstname.getText().toUpperCase());
            pst.setString(5, othername.getText().toUpperCase());
            pst.setString(6, (String) department.getSelectionModel().getSelectedItem());
            pst.setString(7, (String) subdepartment.getSelectionModel().getSelectedItem());
            pst.setString(8, (String) category.getSelectionModel().getSelectedItem());
            pst.setString(9, (String) dutysel.getSelectionModel().getSelectedItem());

            pst.setString(10, "2 Meals");
            pst.execute();

        } catch (Exception e) {
            //System.out.println(e);
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("ERROR : " + e, 3000);

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
                /* ignored */

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
    }

    public void massUpdate3() {

        toMove1 = result.getItems().size();
        try {

            com.sun.glass.ui.Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();

            for (int i = 0; i <= toMove1; i++) {
                result.requestFocus();
                result.getSelectionModel().select(i);
                result.getFocusModel().focus(i);
                robot.keyPress(KeyCode.ENTER.impl_getCode());
                System.out.println("Completed : " + i);
                System.out.println("Completed : " + id);
                maxSelectedAction3();

                robot.keyPress(KeyCode.DOWN.impl_getCode());
                Thread.sleep(10);

            }
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("Updated Successfully", 3000);
            clear();

        } catch (InterruptedException ex) {
            Logger.getLogger(RegistrationController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get click response
    public void maxSelectedAction3() {
        id = null;
        ProfileStatus2 = 0;
        picture.setImage(new Image("/image/Picture.png"));
        picture.setFitWidth(239);
        picture.setFitHeight(179);;
        picture.setPreserveRatio(true);
        picture.setSmooth(true);
        picture.setCache(true);
        String Picsname = null;
        try {

            conn = Sqlite_Connection.DBConnect();
            searchResult searchR = (searchResult) result.getSelectionModel().getSelectedItem();

            String query = "select * from STAFF_DETAILS where RID =?";
            result.getSelectionModel().select(searchR);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getString("RID");
                staffID.setText(rs.getString("Staff_ID"));
                surname.setText(rs.getString("Surname"));
                firstname.setText(rs.getString("Firstname"));
                othername.setText(rs.getString("Othername"));
                department.setValue(rs.getString("Department"));
                subdepartment.setValue(rs.getString("Sub_Department"));
                category.setValue(rs.getString("Category"));
                dutysel.setValue(rs.getString("Duty"));
                tright.setValue(rs.getString("Right"));
                //image store in temp folder

                Picsname = rs.getString("Picture");
                pst.execute();
                getPicsname = Picsname;

//                System.out.println("Contains :"+ProfileStatus );
                if (Picsname.equals(null)) {
                    picture.setImage(new Image("/image/Picture.png"));
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);;
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                } else {

                    String dir = "prof/" + Picsname + ".jpg";

                    String path = dir;
                    System.out.println("" + path + "" + Picsname + ".jpg");
                    File file = new File(dir);
                    Image image = new Image(file.toURI().toString());
                    picture.setImage(image);
                    picture.setFitWidth(239);
                    picture.setFitHeight(179);
                    picture.setPreserveRatio(true);
                    picture.setSmooth(true);
                    picture.setCache(true);

                }
            }

            pst.execute();
//                System.out.println("Contains :"+ProfileStatus );

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
        maxEditfunc3();

    }

    public void maxEditfunc3() {

        String query = "UPDATE STAFF_DETAILS SET RID = ?, Staff_ID = ?,Surname = ?,Firstname = ?,"
                + " Othername= ?, Department= ?,Sub_Department= ?, Category= ?, Duty= ?,Right= ? "
                + "WHERE RID ='" + id + "' ";//11 inputs

        //database code
        try {
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id);
            pst.setString(2, staffID.getText().toUpperCase());
            pst.setString(3, surname.getText().toUpperCase());
            pst.setString(4, firstname.getText().toUpperCase());
            pst.setString(5, othername.getText().toUpperCase());
            pst.setString(6, (String) department.getSelectionModel().getSelectedItem());
            pst.setString(7, (String) subdepartment.getSelectionModel().getSelectedItem());
            pst.setString(8, (String) category.getSelectionModel().getSelectedItem());
            pst.setString(9, (String) dutysel.getSelectionModel().getSelectedItem());

            pst.setString(10, "3 Meals");
            pst.execute();

        } catch (Exception e) {
            //System.out.println(e);
            JFXSnackbar snackbar = new JFXSnackbar(rootPane);
            snackbar.show("ERROR : " + e, 3000);

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
                /* ignored */

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
    }

}
