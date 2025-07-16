package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for Timeslot Details Response
 */
public class TimeslotDetailsResponse {

    @JsonProperty("timeslotId")
    private String timeslotId;

    // Constructors
    public TimeslotDetailsResponse() {}

    public TimeslotDetailsResponse(String timeslotId) {
        this.timeslotId = timeslotId;
    }

    // Getters and Setters
    public String getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(String timeslotId) {
        this.timeslotId = timeslotId;
    }

    @Override
    public String toString() {
        return "TimeslotDetailsResponse{" +
                "timeslotId='" + timeslotId + '\'' +
                '}';
    }
}
