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
public class searchResult {
    private final SimpleStringProperty id;
    private final SimpleStringProperty staffid;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty firstname;
    private final SimpleStringProperty othername;
    private final SimpleStringProperty department;
    private final SimpleStringProperty sub_department;
    private final SimpleStringProperty category;
    private final SimpleStringProperty duty;
    private final StringProperty right;
    
    
    

    public searchResult(String id,String staffid,String surname, String firstname, String othername,
            String department, String sub_department, String category, String duty,String right) {
        this.id = new SimpleStringProperty(id);
        this.staffid = new SimpleStringProperty(staffid);
        this.surname = new SimpleStringProperty(surname);
        this.firstname = new SimpleStringProperty(firstname);
        this.othername = new SimpleStringProperty(othername);
        this.department = new SimpleStringProperty(department);
        this.sub_department = new SimpleStringProperty(sub_department);
       this.category = new SimpleStringProperty(category);
       this.duty = new SimpleStringProperty(duty);
       this.right = new SimpleStringProperty(right);
    }

    public String getId() {
        return id.get();
    }
    public String getSTAFFID() {
        return staffid.get();
    }
    public String getSURNAME() {
        return surname.get();
    }
    public String getFIRSTNAME() {
        return firstname.get();
    }
    public String getOTHERNAME() {
        return othername.get();
    }

    public String getDEPARTMENT() {
        return department.get();
    }
    public String getSUBDEPARTMENT() {
        return sub_department.get();
    }
    public String getCATEGORY() {
        return category.get();
    }
    public String getDUTY() {
        return duty.get();
    }
    public StringProperty getRIGHT() {
        return right;
    }

   
   //
      public void setID(String ID){
        id.set(ID);
    }
      public void setSTAFFID(String STAFFID){
        staffid.set(STAFFID);
    }
        public void setSURNAME(String SURNAME){
        surname.set(SURNAME);
    }
           public void setFIRSTNAME(String FIRSTNAME){
        firstname.set(FIRSTNAME);
    }
              public void setOTHERNAME(String OTHERNAME){
        othername.set(OTHERNAME);
    }
              
    public void setDEPARTMENT(String DEPARTMENT){
        department.set(DEPARTMENT);
    }
       public void setSUBDEPARTMENT(String SUBDEPARTMENT){
        sub_department.set(SUBDEPARTMENT);
    }
          public void setCATEGORY(String CATEGORY){
        category.set(CATEGORY);
    }
             public void setDUTY(String DUTY){
        duty.set(DUTY);
    }
             public void setRIGHT(String RIGHT){
        right.set(RIGHT);
    }
  
   
    
    
}
