package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for Reservation Request API payload
 * Contains all fields required for creating a booking reservation
 */
public class ReservationRequest {
    
    @JsonProperty("timeslotId")
    private String timeslotId;
    
    @JsonProperty("gatewayId")
    private String gatewayId;
    
    @JsonProperty("couponCode")
    private String couponCode;
    
    @JsonProperty("clientId")
    private String clientId;
    
    @JsonProperty("serviceId")
    private String serviceId;
    
    // Constructors
    public ReservationRequest() {}
    
    public ReservationRequest(String timeslotId, String gatewayId) {
        this.timeslotId = timeslotId;
        this.gatewayId = gatewayId;
    }
    
    public ReservationRequest(String timeslotId, String gatewayId, String couponCode) {
        this.timeslotId = timeslotId;
        this.gatewayId = gatewayId;
        this.couponCode = couponCode;
    }
    
    // Getters and Setters
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
    
    public String getCouponCode() {
        return couponCode;
    }
    
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
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
        return "ReservationRequest{" +
                "timeslotId='" + timeslotId + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", clientId='" + clientId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}
