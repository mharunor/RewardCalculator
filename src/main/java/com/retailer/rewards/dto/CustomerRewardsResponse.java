package com.retailer.rewards.dto;

import java.util.List;

public class CustomerRewardsResponse {

    private String customerId;
    private String customerName;
    private List<MonthlyPoints> monthlyPoints;
    private long totalPoints;

    public CustomerRewardsResponse() {
    }

    public CustomerRewardsResponse(String customerId, String customerName,
                                   List<MonthlyPoints> monthlyPoints, long totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<MonthlyPoints> getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(List<MonthlyPoints> monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }
}
