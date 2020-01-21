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
public class UserTable {

    private final SimpleStringProperty id;
    private final SimpleStringProperty firstname;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty username;
    private final SimpleStringProperty level;

    public UserTable(String id, String firstname, String surname, String username,
            String level) {
        this.id = new SimpleStringProperty(id);
        this.firstname = new SimpleStringProperty(firstname);
        this.surname = new SimpleStringProperty(surname);
        this.username = new SimpleStringProperty(username);
        this.level = new SimpleStringProperty(level);
    }

    public String getId() {
        return id.get();
    }

    public String getFIRSTNAME() {
        return firstname.get();
    }

    public String getSURNAME() {
        return surname.get();
    }

    public String getUSERNAME() {
        return username.get();
    }

    public String getLEVEL() {
        return level.get();
    }

    //
    public void setID(String ID) {
        id.set(ID);
    }

    public void setFIRSTNAME(String FIRSTNAME) {
        firstname.set(FIRSTNAME);
    }

    public void setSURNAME(String SURNAME) {
        surname.set(SURNAME);
    }

    public void setUSERNAME(String USERNAME) {
        username.set(USERNAME);
    }

    public void setLEVEL(String LEVEL) {
        level.set(LEVEL);
    }

   }
