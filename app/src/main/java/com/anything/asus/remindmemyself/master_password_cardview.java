package com.anything.asus.remindmemyself;

public class master_password_cardview {

    public String pass,busName;

    public master_password_cardview() {

    }
    public master_password_cardview(String busName,String pass) {
        this.busName=busName;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }
}
