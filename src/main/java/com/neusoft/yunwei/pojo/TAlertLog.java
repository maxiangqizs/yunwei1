package com.neusoft.yunwei.pojo;

public class TAlertLog {
    private String alertid;

    private String alertproject;

    private String alertthreshold;

    private String alerttype;

    private String alertcontent;

    private String alerttime;

    private String note;

    public String getAlertid() {
        return alertid;
    }

    public void setAlertid(String alertid) {
        this.alertid = alertid == null ? null : alertid.trim();
    }

    public String getAlertproject() {
        return alertproject;
    }

    public void setAlertproject(String alertproject) {
        this.alertproject = alertproject == null ? null : alertproject.trim();
    }

    public String getAlertthreshold() {
        return alertthreshold;
    }

    public void setAlertthreshold(String alertthreshold) {
        this.alertthreshold = alertthreshold == null ? null : alertthreshold.trim();
    }

    public String getAlerttype() {
        return alerttype;
    }

    public void setAlerttype(String alerttype) {
        this.alerttype = alerttype == null ? null : alerttype.trim();
    }

    public String getAlertcontent() {
        return alertcontent;
    }

    public void setAlertcontent(String alertcontent) {
        this.alertcontent = alertcontent == null ? null : alertcontent.trim();
    }

    public String getAlerttime() {
        return alerttime;
    }

    public void setAlerttime(String alerttime) {
        this.alerttime = alerttime == null ? null : alerttime.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}