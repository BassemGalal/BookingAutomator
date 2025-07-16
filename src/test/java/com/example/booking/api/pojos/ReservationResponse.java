package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for Reservation Response API payload
 * Contains only required fields: orderId, redirectToUrl, referenceNumber
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("redirectToUrl")
    private String redirectToUrl;

    @JsonProperty("referenceNumber")
    private String referenceNumber;

    public ReservationResponse() {}

    public ReservationResponse(String orderId, String redirectToUrl, String referenceNumber) {
        this.orderId = orderId;
        this.redirectToUrl = redirectToUrl;
        this.referenceNumber = referenceNumber;
    }

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

    @Override
    public String toString() {
        return "ReservationResponse{" +
                "orderId='" + orderId + '\'' +
                ", redirectToUrl='" + redirectToUrl + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
