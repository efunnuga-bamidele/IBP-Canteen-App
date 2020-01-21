/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import connection.Sqlite_Connection;
import functions.DeptTable;
import functions.UserTable;
import functions.subdeptTable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author DaCodin
 */
public class SettingsController implements Initializable {

    public static PreparedStatement pst;
    public static ResultSet rs;
    public static Connection conn;
    String id = null;
    String id2 = null;
    String id3 = null;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField firstname;

    @FXML
    private JFXTextField surname;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField stpass;

    @FXML
    private JFXPasswordField repass;

    @FXML
    private JFXComboBox<String> accessLevel;
    @FXML
    ObservableList<String> aoptions
            = FXCollections.observableArrayList(
                    "...Select Access Level",
                    "Admin Access",
                    "Canteen Access"
            );

    @FXML
    TableView<UserTable> userTable = new TableView<>(); //Registered Workers Table
    final ObservableList<UserTable> data = FXCollections.observableArrayList();

    @FXML
    private JFXTextField enterDept;

    @FXML
    TableView<DeptTable> deptTable = new TableView<>(); //Registered Workers Table
    final ObservableList<DeptTable> data1 = FXCollections.observableArrayList();

    @FXML
    private JFXTextField enterSubDept;

    @FXML
    TableView<subdeptTable> subDeptTable = new TableView<>(); //Registered Workers Table
    final ObservableList<subdeptTable> data2 = FXCollections.observableArrayList();

    @FXML
    void dclear(ActionEvent event) {
        dclear();
    }

    @FXML
    void ddelete(ActionEvent event) {
        ddeleteAction();
    }

    @FXML
    void dedit(ActionEvent event) {
        deditAction();
    }

    @FXML
    void dsave(ActionEvent event) {
        dSaveAction();
    }

    @FXML
    void sbclear(ActionEvent event) {
        sdclear();
    }

    @FXML
    void sbdelete(ActionEvent event) {
        sddeleteAction();
    }

    @FXML
    void sbedit(ActionEvent event) {
        sdeditAction();
    }

    @FXML
    void sbsave(ActionEvent event) {
        sdSaveAction();
    }

    @FXML
    void uclear(ActionEvent event) {
        clear();
    }

    @FXML
    void udelete(ActionEvent event) {
        deleteAction();
    }

    @FXML
    void uedit(ActionEvent event) {
        editAction();
    }

    @FXML
    void usave(ActionEvent event) {
        SaveAction();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accessLevel.setItems(aoptions);
        refresh();
        buildData();
        drefresh();
        dbuildData();
        sdrefresh();
        sdbuildData();

        // TODO
    }

    //USER REGISTRATION ACCESS
    public void SaveAction() {
        if ((surname.getText().equals("")) || (firstname.getText().equals("")) || (username.getText().equals("")) || (stpass.getText().equals(""))
                || (accessLevel.getSelectionModel().getSelectedItem().equals(""))
                || (accessLevel.getSelectionModel().getSelectedItem().equals("...Select Access Level"))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {
            if (stpass.getText().equals(repass.getText())) {
                String query = "INSERT INTO USER(Firstname, Surname, Username, Password, Level)"
                        + " VALUES (?,?,?,?,?)";//9 INPUTS

                //database code
                try {
                    conn = Sqlite_Connection.DBConnect();
                    pst = conn.prepareStatement(query);
                    pst.setString(1, firstname.getText().toUpperCase());
                    pst.setString(2, surname.getText().toUpperCase());
                    pst.setString(3, username.getText());
                    pst.setString(4, stpass.getText());
                    pst.setString(5, (String) accessLevel.getSelectionModel().getSelectedItem());
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
            } else {
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("ERROR : Password entered not a Match ", 3000);

            }
        }
    }

    public void editfunc() {

        if ((surname.getText().equals("")) || (firstname.getText().equals("")) || (username.getText().equals("")) || (stpass.getText().equals(""))
                || (accessLevel.getSelectionModel().getSelectedItem().equals(""))
                || (accessLevel.getSelectionModel().getSelectedItem().equals("...Select Access Level"))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {
            if (stpass.getText().equals(repass.getText())) {

                String query = "UPDATE USER SET ID = ?, Firstname = ?,Surname = ?,"
                        + " Username= ?, Password= ?,Level= ?"
                        + "WHERE ID ='" + id + "' ";//11 inputs

                //database code
                try {
                    conn = Sqlite_Connection.DBConnect();
                    pst = conn.prepareStatement(query);
                    pst.setString(1, id);
                    pst.setString(2, firstname.getText().toUpperCase());
                    pst.setString(3, surname.getText().toUpperCase());
                    pst.setString(4, username.getText());
                    pst.setString(5, stpass.getText());
                    pst.setString(6, (String) accessLevel.getSelectionModel().getSelectedItem());
//               
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
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("ERROR : Password entered not a Match ", 3000);

            }
        }
    }

    public void editAction() {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to update this details!", "Yes", 5000, e -> editfunc());

    }

    public void delFunc() {
        try {

            String query = "DELETE FROM USER WHERE ID = ?";
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id);
            pst.executeUpdate();
            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("Information Deleted Sussessfully", 3000);

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
        surname.setText("");
        firstname.setText("");
        username.setText("");
        stpass.setText("");
        repass.setText("");
        accessLevel.getSelectionModel().select(0);
        refresh();
        buildData();

    }

    //table data
    public void buildData() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("FIRSTNAME");
        col2 = new TableColumn("FIRSTNAME");
        col2.setMinWidth(30);
        col2.setCellValueFactory(new PropertyValueFactory<>("FIRSTNAME"));
        TableColumn col3 = new TableColumn("SURNAME");
        col3 = new TableColumn("SURNAME");
        // col3.setMinWidth(70);
        col3.setCellValueFactory(new PropertyValueFactory<>("SURNAME"));
        TableColumn col4 = new TableColumn("USERNAME");
        col4 = new TableColumn("USERNAME");
        //col4.setMinWidth(70);
        col4.setCellValueFactory(new PropertyValueFactory<>("USERNAME"));
        TableColumn col5 = new TableColumn("LEVEL");
        col5 = new TableColumn("LEVEL");
        // col5.setMinWidth(70);
        col5.setCellValueFactory(new PropertyValueFactory<>("LEVEL"));

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        userTable.getColumns().addAll(col1, col2, col3, col4, col5);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from USER";

            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ID");
                String firstname = rs.getString("Firstname");
                String surname = rs.getString("Surname");
                String username = rs.getString("Username");
                String level = rs.getString("Level");

                data.add(new UserTable(id, firstname, surname, username, level));

                //tableview = new TableView();
                //buildData();
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            userTable.getItems().setAll(data);
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

    //clear table
    public void refresh() {
        //tableview.getColumns().get(0).setVisible(false);
        userTable.getColumns().clear();

        data.removeAll(data);

    }

    //get click response
    @FXML
    void selectedAction(MouseEvent event) {
        id = null;

        try {
            conn = Sqlite_Connection.DBConnect();
            UserTable searchR = (UserTable) userTable.getSelectionModel().getSelectedItem();

            String query = "select * from USER where ID =?";
            userTable.getSelectionModel().select(0);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id = rs.getString("ID");
                firstname.setText(rs.getString("Firstname"));
                surname.setText(rs.getString("Surname"));
                username.setText(rs.getString("Username"));
                stpass.setText(rs.getString("Password"));
                repass.setText(rs.getString("Password"));
                accessLevel.setValue(rs.getString("Level"));
                pst.execute();

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

    //DEPARTMENT REGISTRATION ACCESS
    public void dSaveAction() {
        if ((enterDept.getText().equals(""))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {
            String query = "INSERT INTO DEPARTMENT(DEPARTMENT)"
                    + " VALUES (?)";//9 INPUTS

            //database code
            try {
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, enterDept.getText().toUpperCase());
                pst.execute();
//          
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Details Saved Successfully", 3000);

                dclear();
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

    public void deditfunc() {

        if ((enterDept.getText().equals(""))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {

            String query = "UPDATE DEPARTMENT SET ID = ?, DEPARTMENT = ? WHERE ID ='" + id2 + "' ";//11 inputs

            //database code
            try {
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, id2);
                pst.setString(2, enterDept.getText().toUpperCase());
                pst.execute();
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Updated Successfully", 3000);
                dclear();

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

    public void deditAction() {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to update this details!", "Yes", 5000, e -> deditfunc());

    }

    public void ddelFunc() {
        try {

           String query = "DELETE FROM DEPARTMENT WHERE ID = ?";
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id2);
            pst.executeUpdate();
            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("Information Deleted Sussessfully", 3000);

            dclear();

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

    public void ddeleteAction() {

         JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to delete this data!", "Yes", 5000, e -> ddelFunc());
    }

    public void dclear() {
        id2 = null;
        enterDept.setText("");
        drefresh();
        dbuildData();

    }

    //table data
    public void dbuildData() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("DEPARTMENT");
        col2 = new TableColumn("DEPARTMENT");
        col2.setMinWidth(50);
        col2.setCellValueFactory(new PropertyValueFactory<>("DEPARTMENT"));

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        deptTable.getColumns().addAll(col1, col2);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from DEPARTMENT";

            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ID");
                String department = rs.getString("DEPARTMENT");

                data1.add(new DeptTable(id, department));

                //tableview = new TableView();
                //buildData();
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            deptTable.getItems().setAll(data1);
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

    //clear table
    public void drefresh() {
        //tableview.getColumns().get(0).setVisible(false);
        deptTable.getColumns().clear();

        data1.removeAll(data1);

    }

    //get click response
    @FXML
    void dselectedAction(MouseEvent event) {
        id2 = null;

        try {
            conn = Sqlite_Connection.DBConnect();
            DeptTable searchR = (DeptTable) deptTable.getSelectionModel().getSelectedItem();

            String query = "select * from DEPARTMENT where ID =?";
            deptTable.getSelectionModel().select(0);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id2 = rs.getString("ID");
                enterDept.setText(rs.getString("DEPARTMENT"));

                pst.execute();

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

    //SUB-DEPARTMENT REGISTRATION ACCESS
    public void sdSaveAction() {
        if ((enterSubDept.getText().equals(""))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {
            String query = "INSERT INTO SUB_DEPARTMENT(s_DEPARTMENT)"
                    + " VALUES (?)";//9 INPUTS

            //database code
            try {
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, enterSubDept.getText().toUpperCase());
                pst.execute();
//          
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Details Saved Successfully", 3000);

                sdclear();
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

    public void sdeditfunc() {

        if ((enterSubDept.getText().equals(""))) {

            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("ERROR : Please Fill All Field with (*) Sign", 3000);

        } else {

            String query = "UPDATE SUB_DEPARTMENT SET ID = ?, s_DEPARTMENT = ? WHERE ID ='" + id3 + "' ";//11 inputs

            //database code
            try {
                conn = Sqlite_Connection.DBConnect();
                pst = conn.prepareStatement(query);
                pst.setString(1, id3);
                pst.setString(2, enterSubDept.getText().toUpperCase());
                pst.execute();
                JFXSnackbar snackbar = new JFXSnackbar(rootPane);
                snackbar.show("Updated Successfully", 3000);
                sdclear();

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

    public void sdeditAction() {
        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to update this details!", "Yes", 5000, e -> sdeditfunc());

    }

    public void sddelFunc() {
        try {

            String query = "DELETE FROM SUB_DEPARTMENT WHERE ID = ?";
            conn = Sqlite_Connection.DBConnect();
            pst = conn.prepareStatement(query);
            pst.setString(1, id3);
            pst.executeUpdate();
            JFXSnackbar snackbarx = new JFXSnackbar(rootPane);
            snackbarx.show("Information Deleted Sussessfully", 3000);

            sdclear();

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

    public void sddeleteAction() {

        JFXSnackbar snackbar = new JFXSnackbar(rootPane);
        snackbar.show("Are you sure you want to delete this data!", "Yes", 5000, e -> sddelFunc());
    }

    public void sdclear() {
        id3 = null;
        enterSubDept.setText("");
        sdrefresh();
        sdbuildData();

    }

    //table data
    public void sdbuildData() {
        //Table Columns Add
        TableColumn col1 = new TableColumn("ID");
        col1 = new TableColumn("ID");
        col1.setMinWidth(10);
        col1.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn col2 = new TableColumn("SUB DEPARTMENT");
        col2 = new TableColumn("SUB DEPARTMENT");
        col2.setMinWidth(50);
        col2.setCellValueFactory(new PropertyValueFactory<>("SUBDEPARTMENT"));

        // col8.setCellValueFactory(new PropertyValueFactory<>("approval"));
        subDeptTable.getColumns().addAll(col1, col2);

        //  tableview.getColumns().addAll(col2, col3, col4, col5);
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from SUB_DEPARTMENT";

            pst = conn.prepareStatement(SQL);
            rs = pst.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ID");
                String sdepartment = rs.getString("s_DEPARTMENT");

                data2.add(new subdeptTable(id, sdepartment));

                //tableview = new TableView();
                //buildData();
            }
            pst.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        } finally {
            subDeptTable.getItems().setAll(data2);
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

    //clear table
    public void sdrefresh() {
        //tableview.getColumns().get(0).setVisible(false);
        subDeptTable.getColumns().clear();

        data2.removeAll(data2);

    }

    //get click response
    @FXML
    void sdselectedAction(MouseEvent event) {
        id3 = null;

        try {
            conn = Sqlite_Connection.DBConnect();
            subdeptTable searchR = (subdeptTable) subDeptTable.getSelectionModel().getSelectedItem();

            String query = "select * from SUB_DEPARTMENT where ID =?";
            subDeptTable.getSelectionModel().select(0);

            pst = conn.prepareStatement(query);
            pst.setString(1, searchR.getId());

            // clear();
            rs = pst.executeQuery();
            while (rs.next()) {
                id3 = rs.getString("ID");
                enterSubDept.setText(rs.getString("s_DEPARTMENT"));

                pst.execute();

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
}
