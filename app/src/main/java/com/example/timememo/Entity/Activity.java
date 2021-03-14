package com.example.timememo.Entity;

public class Activity {
    private int id = 0;
    private String aTitle;
    private String aTime;
    private String aType;
    private String aDate;
    private String aStartTime;
    private String aEndTime;
    private String aState;
    private int uid = 0;

    public Activity(int id, String aTitle, String aTime,String aType, String aDate, String aStartTime,
                    String aEndTime, String aState, int uid) {
        this.id = id;
        this.aTitle = aTitle;
        this.aTime = aTime;
        this.aType = aType;
        this.aDate = aDate;
        this.aStartTime = aStartTime;
        this.aEndTime = aEndTime;
        this.aState = aState;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle;
    }

    public String getaTime() {
        return aTime;
    }

    public void setaTime(String aTime) {
        this.aTime = aTime;
    }

    public String getaType() {
        return aType;
    }

    public void setaType(String aType) {
        this.aType = aType;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getaStartTime() {
        return aStartTime;
    }

    public void setaStartTime(String aStartTime) {
        this.aStartTime = aStartTime;
    }

    public String getaEndTime() {
        return aEndTime;
    }

    public void setaEndTime(String aEndTime) {
        this.aEndTime = aEndTime;
    }

    public String getaState() {
        return aState;
    }

    public void setaState(String aState) {
        this.aState = aState;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
