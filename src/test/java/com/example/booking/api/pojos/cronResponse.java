package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for CRON Response - Minimal version for optional logging
 */
public class cronResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    public cronResponse() {}

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
