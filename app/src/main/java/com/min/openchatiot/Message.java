package com.min.openchatiot;

/**
 * Created by Min-LAP on 2017-10-22.
 */

public class Message {
    private String id;
    private String date;
    private String time;
    private String text;
    private String who;

    public Message() { }

    public Message(String id, String date, String time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
