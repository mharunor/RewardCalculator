package com.retailer.rewards.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {

    private String transactionId;
    private String customerId;
    private String customerName;
    private LocalDate transactionDate;
    private BigDecimal amount;

    public TransactionDto() {
    }

    public TransactionDto(String transactionId, String customerId, String customerName,
                          LocalDate transactionDate, BigDecimal amount) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
