package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for Operator Order Response
 */
public class OperatorOrderResponse {
    
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("paymentStatus")
    private String paymentStatus;
    
    @JsonProperty("gatewayId")
    private String gatewayId;
    
    @JsonProperty("amount")
    private double amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("orderDate")
    private String orderDate;
    
    @JsonProperty("timeslotId")
    private String timeslotId;
    
    @JsonProperty("clientId")
    private String clientId;
    
    @JsonProperty("serviceId")
    private String serviceId;
    
    // Constructors
    public OperatorOrderResponse() {}
    
    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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
    
    public String getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getTimeslotId() {
        return timeslotId;
    }
    
    public void setTimeslotId(String timeslotId) {
        this.timeslotId = timeslotId;
    }
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    
    @Override
    public String toString() {
        return "OperatorOrderResponse{" +
                "orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", timeslotId='" + timeslotId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}