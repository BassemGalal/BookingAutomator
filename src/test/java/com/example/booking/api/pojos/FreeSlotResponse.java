package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for Free Slot Response
 */
public class FreeSlotResponse {
    
    @JsonProperty("tSlotDate")
    private String tSlotDate;
    
    @JsonProperty("tSlotTime")
    private String tSlotTime;
    
    @JsonProperty("serviceId")
    private String serviceId;
    
    // Constructors
    public FreeSlotResponse() {}
    
    public FreeSlotResponse(String tSlotDate, String tSlotTime, String serviceId) {
        this.tSlotDate = tSlotDate;
        this.tSlotTime = tSlotTime;
        this.serviceId = serviceId;
    }
    
    // Getters and Setters
    public String getTSlotDate() {
        return tSlotDate;
    }
    
    public void setTSlotDate(String tSlotDate) {
        this.tSlotDate = tSlotDate;
    }
    
    public String getTSlotTime() {
        return tSlotTime;
    }
    
    public void setTSlotTime(String tSlotTime) {
        this.tSlotTime = tSlotTime;
    }
    
    public String getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    
    @Override
    public String toString() {
        return "FreeSlotResponse{" +
                "tSlotDate='" + tSlotDate + '\'' +
                ", tSlotTime='" + tSlotTime + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }
}