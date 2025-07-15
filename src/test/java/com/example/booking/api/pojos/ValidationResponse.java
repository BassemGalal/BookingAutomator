package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for Validation Response
 */
public class ValidationResponse {
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("valid")
    private boolean valid;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("transactionId")
    private String transactionId;
    
    @JsonProperty("amount")
    private double amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("validatedAt")
    private String validatedAt;
    
    // Constructors
    public ValidationResponse() {}
    
    public ValidationResponse(String orderId, boolean valid, String status, String message) {
        this.orderId = orderId;
        this.valid = valid;
        this.status = status;
        this.message = message;
    }
    
    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getValidatedAt() {
        return validatedAt;
    }
    
    public void setValidatedAt(String validatedAt) {
        this.validatedAt = validatedAt;
    }
    
    @Override
    public String toString() {
        return "ValidationResponse{" +
                "orderId='" + orderId + '\'' +
                ", valid=" + valid +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", validatedAt='" + validatedAt + '\'' +
                '}';
    }
}