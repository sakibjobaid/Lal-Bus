package com.anything.asus.remindmemyself;

public class Password_CardView {

    public String pass,time;

    public Password_CardView() {

    }
    public Password_CardView(String time,String pass) {
        this.time=time;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTime() {
        return time;
    }

    public void setTIme(String time) {
        this.time = time;
    }
}
