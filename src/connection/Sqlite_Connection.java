/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author DaCodin
 */
public class Sqlite_Connection {
     public static Connection conn = null;
  
   public static Connection DBConnect() {

        try {
           Class.forName("org.sqlite.JDBC");
           conn = DriverManager.getConnection("jdbc:sqlite:lib/doctype.db");
           
           // System.out.println("Connection Successful");
            return conn;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }

    }  
 
    
}
