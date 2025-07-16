package com.example.booking.api.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperatorOrderResponse {

    @JsonProperty("transactionStatus")
    private String transactionStatus;

    public OperatorOrderResponse() {}

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public String toString() {
        return "OperatorOrderResponse{" +
                "transactionStatus='" + transactionStatus + '\'' +
                '}';
    }
}
