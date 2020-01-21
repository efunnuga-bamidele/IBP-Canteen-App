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
public class DeptTable {
    private final SimpleStringProperty id;
    private final SimpleStringProperty department;

    
    
    

    public DeptTable(String id,String department) {
        this.id = new SimpleStringProperty(id);
        this.department = new SimpleStringProperty(department);
   
    }

    public String getId() {
        return id.get();
    }
     public String getDEPARTMENT() {
        return department.get();
    }
  

   
   //
      public void setID(String ID){
        id.set(ID);
    }
   
              
    public void setDEPARTMENT(String DEPARTMENT){
        department.set(DEPARTMENT);
    }
     
   
    
    
}
