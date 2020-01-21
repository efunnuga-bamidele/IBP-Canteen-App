/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Dacodin
 */
public class NewClass {

    public static PreparedStatement pst;
    public static ResultSet rs;
    public static Connection conn;

    public static void main(String[] args) {

        try {
           conn = Sqlite_Connection.DBConnect();
            String sql = "select Username From USER";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            System.out.println("Connected");
            rs.close();
            pst.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                rs.close();
                System.out.println("ResultSet Closed");
            } catch (Exception e) { /* ignored */ }
            try {
                pst.close();
                System.out.println("PreparedStatement Closed");
            } catch (Exception e) { /* ignored */ }
            try {
                conn.close();
                System.out.println("Connection Closed");
            } catch (Exception e) { /* ignored */ }


        

    }}

}
