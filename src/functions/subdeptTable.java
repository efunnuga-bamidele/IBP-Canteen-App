/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author DaCodin
 */
public class subdeptTable {
    private final SimpleStringProperty id;
    private final SimpleStringProperty sub_department;

    
    

    public subdeptTable(String id,String sub_department) {
        this.id = new SimpleStringProperty(id);
     
        this.sub_department = new SimpleStringProperty(sub_department);
     
    }

    public String getId() {
        return id.get();
    }

    public String getSUBDEPARTMENT() {
        return sub_department.get();
    }


   
   //
      public void setID(String ID){
        id.set(ID);
    }
    
       public void setSUBDEPARTMENT(String SUBDEPARTMENT){
        sub_department.set(SUBDEPARTMENT);
    }
      
   
    
    
}
