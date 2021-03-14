package com.example.timememo.Entity;

public class Backlog {
    private int id = 0;
    private String title = null;
    private String time = null;
    private String rgb = null;
    private String backlogType = null;
    private int uid = 0;

    public Backlog(int id, String title, String time, String rgb, String backlogType,int uid) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.rgb = rgb;
        this.backlogType = backlogType;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public String getBacklogType() {
        return backlogType;
    }

    public void setBacklogType(String backlogType) {
        this.backlogType = backlogType;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
