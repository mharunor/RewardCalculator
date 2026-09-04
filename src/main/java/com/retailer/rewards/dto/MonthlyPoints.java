package com.retailer.rewards.dto;

public class MonthlyPoints {

    private String month;
    private long points;

    public MonthlyPoints() {
    }

    public MonthlyPoints(String month, long points) {
        this.month = month;
        this.points = points;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
