package com.example.booking.api;

import com.example.booking.api.endpoints.BookingEndpoints;
import com.example.booking.api.pojos.*;
import com.example.booking.utils.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Central RestAssured client for API operations
 * Handles authentication, request configuration, and common API operations
 */
public class ApiClient {
    
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private final ConfigManager configManager;
    
    public ApiClient() {
        this.configManager = ConfigManager.getInstance();
        setupRestAssured();
    }
    
    private void setupRestAssured() {
        RestAssured.baseURI = configManager.getProperty("base.url");
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    
    /**
     * Creates a base request specification with authentication
     */
    private RequestSpecification getRequestSpec(String token) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json");
    }
    
    /**
     * Get free slots from API
     */
    public Response getFreeSlots() {
        try {
            logger.info("Getting free slots");
            String token = configManager.getProperty("therapist.token");
            
            Response response = getRequestSpec(token)
                    .when()
                    .get(BookingEndpoints.FREE_SLOTS);
            
            logger.info("Free slots response status: {}", response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error getting free slots: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get free slots", e);
        }
    }
    
    /**
     * Get timeslot details using slot date, time, and service ID
     */
    public Response getTimeslotDetails(String slotDate, String slotTime, String serviceId) {
        try {
            logger.info("Getting timeslot details for date: {}, time: {}, service: {}", 
                       slotDate, slotTime, serviceId);
            String token = configManager.getProperty("therapist.token");
            
            Response response = getRequestSpec(token)
                    .queryParam("tSlotDate", slotDate)
                    .queryParam("tSlotTime", slotTime)
                    .queryParam("serviceId", serviceId)
                    .when()
                    .get(BookingEndpoints.TIMESLOT_DETAILS);
            
            logger.info("Timeslot details response status: {}", response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error getting timeslot details: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get timeslot details", e);
        }
    }
    
    /**
     * Create reservation with timeslot ID and gateway ID
     */
    public Response createReservation(ReservationRequest request) {
        try {
            logger.info("Creating reservation: {}", request);
            String token = configManager.getProperty("client.token");
            
            Response response = getRequestSpec(token)
                    .body(request)
                    .when()
                    .post(BookingEndpoints.RESERVATION);
            
            logger.info("Reservation response status: {}", response.getStatusCode());
            if (response.getStatusCode() >= 400) {
                logger.error("Reservation failed with status: {}, body: {}", 
                           response.getStatusCode(), response.getBody().asString());
            }
            
            return response;
            
        } catch (Exception e) {
            logger.error("Error creating reservation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create reservation", e);
        }
    }
    
    /**
     * Validate transaction using order ID
     */
    public Response validateTransaction(String orderId) {
        try {
            logger.info("Validating transaction for order: {}", orderId);
            String token = configManager.getProperty("operator.token");
            
            Response response = getRequestSpec(token)
                    .queryParam("orderId", orderId)
                    .when()
                    .post(BookingEndpoints.VALIDATE_TRANSACTION);
            
            logger.info("Validation response status: {}", response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error validating transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to validate transaction", e);
        }
    }
    
    /**
     * Simulate cron job for order processing
     */
    public Response simulateCron(String orderId) {
        try {
            logger.info("Simulating cron for order: {}", orderId);
            String token = configManager.getProperty("operator.token");
            
            Response response = getRequestSpec(token)
                    .queryParam("orderId", orderId)
                    .when()
                    .post(BookingEndpoints.CRON_SIMULATION);
            
            logger.info("Cron simulation response status: {}", response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error simulating cron: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to simulate cron", e);
        }
    }
    
    /**
     * Get operator order details using order ID and date
     */
    public Response getOperatorOrder(String orderId, String date) {
        try {
            logger.info("Getting operator order for ID: {}, date: {}", orderId, date);
            String token = configManager.getProperty("operator.token");
            
            Response response = getRequestSpec(token)
                    .queryParam("orderId", orderId)
                    .queryParam("date", date)
                    .when()
                    .get(BookingEndpoints.OPERATOR_ORDERS);
            
            logger.info("Operator order response status: {}", response.getStatusCode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error getting operator order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get operator order", e);
        }
    }
    
    /**
     * Get operator order details using order ID and today's date
     */
    public Response getOperatorOrderToday(String orderId) {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return getOperatorOrder(orderId, today);
    }
    
    /**
     * Get gateway ID for specific payment method
     */
    public String getGatewayId(String gatewayType) {
        return configManager.getProperty("gateway.id." + gatewayType.toLowerCase());
    }
}
