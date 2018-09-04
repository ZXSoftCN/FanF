package com.zxsoft.fanfanfamily.base.domain.vo;

public class MortAmount {
    private int month;
    private double saveAmount;//存量金额
    private double keepAmount;//在保金额

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getSaveAmount() {
        return saveAmount;
    }

    public void setSaveAmount(double saveAmount) {
        this.saveAmount = saveAmount;
    }

    public double getKeepAmount() {
        return keepAmount;
    }

    public void setKeepAmount(double keepAmount) {
        this.keepAmount = keepAmount;
    }
}
