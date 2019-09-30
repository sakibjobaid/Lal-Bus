package com.anything.asus.remindmemyself;

public class admin_profile_info {
    String contact_info,name_info,dept_info,email_info
            ,fbId_info,picString_info,trip_info;


    public admin_profile_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getName_info() {
        return name_info;
    }

    public void setName_info(String name_info) {
        this.name_info = name_info;
    }

    public String getDept_info() {
        return dept_info;
    }

    public void setDept_info(String dept_info) {
        this.dept_info = dept_info;
    }

    public String getEmail_info() {
        return email_info;
    }

    public void setEmail_info(String email_info) {
        this.email_info = email_info;
    }

    public String getFbId_info() {
        return fbId_info;
    }

    public void setFbId_info(String fbId_info) {
        this.fbId_info = fbId_info;
    }

    public String getPicString_info() {
        return picString_info;
    }

    public void setPicString_info(String picString_info) {
        this.picString_info = picString_info;
    }

    public String getTrip_info() {
        return trip_info;
    }

    public void setTrip_info(String trip_info) {
        this.trip_info = trip_info;
    }
}
