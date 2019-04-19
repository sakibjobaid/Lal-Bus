package com.example.asus.remindmemyself;

public class Schedule_CardView {

    public String time,from,to,type;

    public Schedule_CardView() {

    }

    public Schedule_CardView(String time, String from, String to, String type) {
        this.time = time;
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
