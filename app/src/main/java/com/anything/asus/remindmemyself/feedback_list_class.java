package com.anything.asus.remindmemyself;

public class feedback_list_class {

    String text,date,id;



    public feedback_list_class(String text, String date,String id) {
        this.text = text;
        this.date = date;
        this.id=id;
    }

    public feedback_list_class() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
