package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO class for Reservation Request API payload
 * Contains only fields required for creating a booking reservation
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationRequest {

    @JsonProperty("timeslotId")
    private String timeslotId;

    @JsonProperty("gatewayId")
    private String gatewayId;

    @JsonProperty("couponCode")
    private String couponCode;

    public ReservationRequest() {}

    public ReservationRequest(String timeslotId, String gatewayId, String couponCode) {
        this.timeslotId = timeslotId;
        this.gatewayId = gatewayId;
        this.couponCode = couponCode;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "timeslotId='" + timeslotId + '\'' +
                ", gatewayId='" + gatewayId + '\'' +
                ", couponCode='" + couponCode + '\'' +
                '}';
    }
}
