package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for Reservation Response API payload
 * Contains all fields returned from booking reservation API
 */
public class ReservationResponse {
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("redirectToUrl")
    private String redirectToUrl;
    
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("timeslotId")
    private String timeslotId;
    
    @JsonProperty("gatewayId")
    private String gatewayId;
    
    @JsonProperty("amount")
    private double amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("message")
    private String message;
    
    // Constructors
    public ReservationResponse() {}
    
    public ReservationResponse(String orderId, String redirectToUrl, String status) {
        this.orderId = orderId;
        this.redirectToUrl = redirectToUrl;
        this.status = status;
    }
    
    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getRedirectToUrl() {
        return redirectToUrl;
    }
    
    public void setRedirectToUrl(String redirectToUrl) {
        this.redirectToUrl = redirectToUrl;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTimeslotId() {
        return timeslotId;
    }
    
    public void setTimeslotId(String timeslotId) {
        this.timeslotId = timeslotId;
    }
    
    public String getGatewayId() {
        return gatewayId;
    }
    
    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
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
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ReservationResponse{" +
                "orderId='" + orderId + '\'' +
                ", redirectToUrl='" + redirectToUrl + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", status='" + status + '\'' +
                ", timeslotId='" + timeslotId + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
