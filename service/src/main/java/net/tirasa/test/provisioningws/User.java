package net.tirasa.test.provisioningws;

import java.util.Date;

public class User {

    private String initials;

    private String firstname;

    private String surname;

    private Date birhdate;

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirhdate() {
        return birhdate;
    }

    public void setBirhdate(Date birhdate) {
        this.birhdate = birhdate;
    }

}
