package com.example.demo.model;

import java.sql.Date;

public class CompleteInfo extends Goods {
    private String type;
    private int saleCount;
    private int saleAmount;
    private String time;
    private Date undtime;
    private String reason;
    private Date invtime;
    private int unitprice;
    private int totalprice;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public int getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(int saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public Date getInvtime() {
        return invtime;
    }

    public void setInvtime(Date invtime) {
        this.invtime = invtime;
    }

    public int getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }
}
