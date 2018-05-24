package com.example.demo.model;

import java.util.Date;

public class Undercarriage {
    private int uid;
    private int gid;
    private Date undtime;
    private String reason;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public Date getUndtime() {
        return undtime;
    }

    public void setUndtime(Date undtime) {
        this.undtime = undtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
