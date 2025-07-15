package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for Timeslot Details Response
 */
public class TimeslotDetailsResponse {
    
    @JsonProperty("timeslotId")
    private String timeslotId;
    
    @JsonProperty("tSlotDate")
    private String tSlotDate;
    
    @JsonProperty("tSlotTime")
    private String tSlotTime;
    
    @JsonProperty("serviceId")
    private String serviceId;
    
    @JsonProperty("available")
    private boolean available;
    
    @JsonProperty("price")
    private double price;
    
    // Constructors
    public TimeslotDetailsResponse() {}
    
    public TimeslotDetailsResponse(String timeslotId, String tSlotDate, String tSlotTime, String serviceId, boolean available, double price) {
        this.timeslotId = timeslotId;
        this.tSlotDate = tSlotDate;
        this.tSlotTime = tSlotTime;
        this.serviceId = serviceId;
        this.available = available;
        this.price = price;
    }
    
    // Getters and Setters
    public String getTimeslotId() {
        return timeslotId;
    }
    
    public void setTimeslotId(String timeslotId) {
        this.timeslotId = timeslotId;
    }
    
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
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "TimeslotDetailsResponse{" +
                "timeslotId='" + timeslotId + '\'' +
                ", tSlotDate='" + tSlotDate + '\'' +
                ", tSlotTime='" + tSlotTime + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", available=" + available +
                ", price=" + price +
                '}';
    }
}