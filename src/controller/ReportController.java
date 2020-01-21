/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import connection.Sqlite_Connection;
import static connection.Sqlite_Connection.conn;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * FXML Controller class
 *
 * @author DaCodin
 */
public class ReportController implements Initializable {

    String Date;
    String Time;
    String Year;
    String Month;
    String Day;
    private int minute;
    private int hour;
    private int second;
    private int period;

    @FXML
    private Label mydate;

    @FXML
    private JFXRadioButton todaysreport;

    @FXML
    private JFXRadioButton allreport;

    @FXML
    private TableView<String> result;
    private ObservableList data;

    @FXML
    void clear(ActionEvent event) {
        refresh();
        buildData();
        todaysreport.setSelected(true);
        allreport.setSelected(false);
        DatePicker1.setValue(null);
        DatePicker2.setValue(null);
        DatePicker3.setValue(null);

    }

    @FXML
    void export(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter to .xlsx files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        //If file is not null, write to file using output stream.
        FileOutputStream fileOut = null;

        HSSFWorkbook workbook = new HSSFWorkbook();
       
        //Sheet spreadsheet = workbook.createSheet("Canteen Report");
      
        HSSFSheet spreadsheet = workbook.createSheet("Canteen Report");
        HSSFRow row = spreadsheet.createRow(0);
            

        for (int j = 0; j < result.getColumns().size(); j++) {
            row.createCell(j).setCellValue(result.getColumns().get(j).getText());
           
            spreadsheet.setColumnWidth(j, 15 * 256);
        }

        for (int i = 0; i < result.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < result.getColumns().size(); j++) {
                if (result.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(result.getColumns().get(j).getCellData(i).toString());

                } else {
                    row.createCell(j).setCellValue("");
                }
            }
        }
        if (file != null) {
            try (FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
                workbook.write(outputStream);

                // workbook.write(fileOut);
                // fileOut.close();
                // Platform.exit();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }

        }
    }

    @FXML
    private JFXDatePicker DatePicker1;

    @FXML
    private JFXDatePicker DatePicker2;

    @FXML
    private JFXDatePicker DatePicker3;

    @FXML
    void showallreport(ActionEvent event
    ) {

        refresh();
        data = FXCollections.observableArrayList();;
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from REPORT_TABLE";
            //ResultSet
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                result.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList * ******************************
             */
            while (rs.next()) {
                //Iterate Row

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if (rs.getString(i) == null) {
                        row.add("");
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            result.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    @FXML
    void showreportnuw(ActionEvent event
    ) {
        refresh();

        buildData();
    }
    /**
     * Initializes the controller class.
     */
    private final List<String> patterns = Arrays.asList(
            "yyyy-MM-dd");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO\

        CurrentDate();
        // refresh();
        // buildData();

        //DATE CONVERTION
        DatePicker1.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    for (String pattern : patterns) {
                        try {
                            return DateTimeFormatter.ofPattern(pattern).format(date);
                        } catch (DateTimeException dte) {
                        }
                    }
                    System.out.println("Format Error");
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    for (String pattern : patterns) {
                        try {
                            return LocalDate.parse(string, DateTimeFormatter.ofPattern(pattern));
                        } catch (DateTimeParseException dtpe) {
                        }
                    }
                    System.out.println("Parse Error");
                }
                return null;
            }
        });
        //DATE CONVERTION
        DatePicker2.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    for (String pattern : patterns) {
                        try {
                            return DateTimeFormatter.ofPattern(pattern).format(date);
                        } catch (DateTimeException dte) {
                        }
                    }
                    System.out.println("Format Error");
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    for (String pattern : patterns) {
                        try {
                            return LocalDate.parse(string, DateTimeFormatter.ofPattern(pattern));
                        } catch (DateTimeParseException dtpe) {
                        }
                    }
                    System.out.println("Parse Error");
                }
                return null;
            }
        });
        //DATE CONVERTION
        DatePicker3.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    for (String pattern : patterns) {
                        try {
                            return DateTimeFormatter.ofPattern(pattern).format(date);
                        } catch (DateTimeException dte) {
                        }
                    }
                    System.out.println("Format Error");
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    for (String pattern : patterns) {
                        try {
                            return LocalDate.parse(string, DateTimeFormatter.ofPattern(pattern));
                        } catch (DateTimeParseException dtpe) {
                        }
                    }
                    System.out.println("Parse Error");
                }
                return null;
            }
        });

    }
//Get All Reports in Table

    public void buildData1() {
        data = FXCollections.observableArrayList();;
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from REPORT_TABLE";
            //ResultSet
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                result.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList * ******************************
             */
            while (rs.next()) {
                //Iterate Row

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if (rs.getString(i) == null) {
                        row.add("");
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            result.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
    //Dynamic Tyme and Date
//Get Table Report By Todays Date

    public void buildData() {
        data = FXCollections.observableArrayList();;
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            //String SQL = "SELECT * from REPORT_TABLE";
            String SQL = "select * from REPORT_TABLE where MainDate like '%" + Date + "%'";
            System.out.println(Date);
            //String SQL = "SELECT * from REPORT_TABLE WHERE MainDate = \"3/5/2018\"";
            //ResultSet
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                result.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList * ******************************
             */
            while (rs.next()) {
                //Iterate Row

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if (rs.getString(i) == null) {
                        row.add("");
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            result.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    //clear table
    public void refresh() {
        //tableview.getColumns().get(0).setVisible(false);
        result.getColumns().clear();
        //data.removeAll(data);

    }

    public void CurrentDate() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            GregorianCalendar cal = new GregorianCalendar();
            Calendar xcal = Calendar.getInstance();
            xcal.add(Calendar.DATE, 0);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date = fmt.format(xcal.getTime());
//            // timer.setText(fmt.format(xcal.getTime()));
//            int month = cal.get(2);
//            int day = cal.get(5);
//            int year = cal.get(1);
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
            mydate.setText("Date: " + Date);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    @FXML
    void GenerateReport(ActionEvent event) {
        refresh();
        data = FXCollections.observableArrayList();;
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            //String SQL = "SELECT * from REPORT_TABLE";
            String mrgDate = DatePicker1.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String SQL = "select * from REPORT_TABLE where MainDate like '%" + mrgDate + "%'";

            System.out.println(mrgDate);

            //String SQL = "SELECT * from REPORT_TABLE WHERE MainDate = \"3/5/2018\"";
            //ResultSet
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                result.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList * ******************************
             */
            while (rs.next()) {
                //Iterate Row

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if (rs.getString(i) == null) {
                        row.add("");
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            result.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }

    @FXML
    void GenerateReportRange(ActionEvent event) {
        refresh();
        data = FXCollections.observableArrayList();;
        try {
            conn = Sqlite_Connection.DBConnect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            //String SQL = "SELECT * from REPORT_TABLE";
            String mrgDate = DatePicker2.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String mrgDate1 = DatePicker3.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String SQL = "SELECT * FROM REPORT_TABLE WHERE MainDate >= '" + mrgDate + "' AND  MainDate <='" + mrgDate1 + "'";
            // String SQL = "SELECT * FROM REPORT_TABLE WHERE strftime(MainDate) BETWEEN '%" + mrgDate +"%'"+ "AND" +"'%"+ mrgDate1 +"%'";
//            String SQL = "SELECT * FROM REPORT_TABLE WHERE MainDate BETWEEN '"+ mrgDate +"' AND '"+ mrgDate1 +"'";
            System.out.println(mrgDate);
            System.out.println("to");
            System.out.println(mrgDate1);

            //ResultSet
            ResultSet rs = conn.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY * ********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                result.getColumns().addAll(col);

                // System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList * ******************************
             */
            while (rs.next()) {
                //Iterate Row

                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if (rs.getString(i) == null) {
                        row.add("");
                    } else {
                        row.add(rs.getString(i));
                    }
                }
                //System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            result.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }

}
