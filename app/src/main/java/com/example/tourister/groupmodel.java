package com.example.tourister;

public class groupmodel {
    String groupname;
    String owepayment;

    public groupmodel(String groupname) {
        this.groupname = groupname;
    }

    public groupmodel(String groupname, String owepayment) {
        this.groupname = groupname;
        this.owepayment = owepayment;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getOwepayment() {
        return owepayment;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void setOwepayment(String owepayment) {
        this.owepayment = owepayment;
    }
}
