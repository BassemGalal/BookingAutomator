package com.example.booking.api;

import com.example.booking.api.endpoints.BookingEndpoints;
import com.example.booking.api.pojos.ReservationRequest;
import com.example.booking.utils.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApiClient {

    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private final ConfigManager configManager;
    private Integer lastAddedTimeslotId;

    public ApiClient() {
        this.configManager = ConfigManager.getInstance();
    }

    private void setupRestAssured(String baseUrl) {
        RestAssured.baseURI = baseUrl;
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    private String getNestBaseUrl() {
        return configManager.getProperty("base.url.nest");
    }

    private String getNodeBaseUrl() {
        return configManager.getProperty("base.url.node");
    }

    private RequestSpecification getRequestSpec(String token) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json");
    }

    private String getTodayDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String getTomorrowDate() {
        return LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public Response getFreeSlots() {
        try {
            logger.info("Getting free slots");
            setupRestAssured(getNodeBaseUrl());
            String token = configManager.getProperty("therapist.token");

            String dayDate = getTomorrowDate();

            Response response = getRequestSpec(token)
                    .queryParam("monthsAhead", 0)
                    .queryParam("servId", 1)
                    .queryParam("dayDate", dayDate)
                    .queryParam("futureOnly", true)
                    .queryParam("getAvailableToAdd", true)
                    .when()
                    .get(BookingEndpoints.FREE_SLOTS);

            logResponseStatus("Free slots", response);
            return response;

        } catch (Exception e) {
            throw handleApiError("getting free slots", e);
        }
    }

    public Response addAvailableTimeslot() {
        try {
            logger.info("Fetching available slots to add");
            setupRestAssured(getNodeBaseUrl());
            String token = configManager.getProperty("therapist.token");

            Response freeSlotsResponse = getFreeSlots();
            JSONObject data = new JSONObject(freeSlotsResponse.getBody().asString()).getJSONObject("data");
            JSONObject firstAvailableToAdd = data.getJSONArray("tSlots")
                    .getJSONObject(0)
                    .getJSONArray("availableToAdd")
                    .getJSONObject(0);

            int servId = firstAvailableToAdd.getInt("servId");
            String tSlotTime = firstAvailableToAdd.getString("tSlotTime");
            String dayDate = data.getJSONArray("tSlots")
                    .getJSONObject(0)
                    .getString("tSlotDate");

            String tSlotDateTime = dayDate + " " + convertTo24HourFormat(tSlotTime);

            JSONObject payload = new JSONObject();
            payload.put("servId", servId);
            payload.put("tSlotDateTime", tSlotDateTime);

            logger.info("Adding single timeslot with payload: {}", payload);

            Response addSlotResponse = getRequestSpec(token)
                    .body(payload.toString())
                    .when()
                    .post(BookingEndpoints.ADD_SINGLE_TIMESLOT);

            logResponseStatus("Add Single Timeslot", addSlotResponse);

            if (addSlotResponse.getStatusCode() < 400) {
                JSONObject responseData = new JSONObject(addSlotResponse.getBody().asString()).getJSONObject("data");
                this.lastAddedTimeslotId = responseData.getJSONObject("newTimeslot").getInt("id");
                logger.info("Saved Timeslot ID: {}", this.lastAddedTimeslotId);
            }

            return addSlotResponse;

        } catch (Exception e) {
            throw handleApiError("adding available timeslot", e);
        }
    }

    public Response createReservation(ReservationRequest request) {
        try {
            if (lastAddedTimeslotId != null) {
                request.setTimeslotId(String.valueOf(lastAddedTimeslotId));
                logger.info("Assigned lastAddedTimeslotId {} to reservation request", lastAddedTimeslotId);
            }

            logger.info("Creating reservation: {}", request);
            setupRestAssured(getNestBaseUrl());
            String token = configManager.getProperty("client.token");

            Response response = getRequestSpec(token)
                    .body(request)
                    .when()
                    .post(BookingEndpoints.RESERVATION);

            logResponseStatus("Reservation", response);
            return response;

        } catch (Exception e) {
            throw handleApiError("creating reservation", e);
        }
    }

    public Response validateTransaction(String orderId) {
        try {
            logger.info("Validating transaction for order: {}", orderId);
            setupRestAssured(getNestBaseUrl());
            String token = configManager.getProperty("operator.token");

            Response response = getRequestSpec(token)
                    .queryParam("orderId", orderId)
                    .when()
                    .post(BookingEndpoints.VALIDATE_TRANSACTION);

            logResponseStatus("Transaction validation", response);
            return response;

        } catch (Exception e) {
            throw handleApiError("validating transaction", e);
        }
    }

    public Response simulateCron(String orderId) {
        try {
            logger.info("Simulating cron for order: {}", orderId);
            setupRestAssured(getNestBaseUrl());
            String token = configManager.getProperty("operator.token");

            Response response = getRequestSpec(token)
                    .queryParam("orderId", orderId)
                    .when()
                    .post(BookingEndpoints.CRON_SIMULATION);

            logResponseStatus("Cron simulation", response);
            return response;

        } catch (Exception e) {
            throw handleApiError("simulating cron", e);
        }
    }

    public Response getOperatorOrder(String orderId, String date) {
        try {
            logger.info("Getting operator order for ID: {}, date: {}", orderId, date);
            setupRestAssured(getNodeBaseUrl());
            String token = configManager.getProperty("operator.token");

            Response response = getRequestSpec(token)
                    .queryParam("page", 1)
                    .queryParam("dateFrom", date)
                    .queryParam("dateTo", date)
                    .queryParam("gatewayTransactionId", orderId)
                    .when()
                    .get(BookingEndpoints.OPERATOR_ORDERS);

            logResponseStatus("Operator order", response);
            return response;

        } catch (Exception e) {
            throw handleApiError("getting operator order", e);
        }
    }

    public Response getOperatorOrderToday(String orderId) {
        return getOperatorOrder(orderId, getTodayDate());
    }

    private String convertTo24HourFormat(String time12h) {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("h:mm a");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return java.time.LocalTime.parse(time12h, parser).format(formatter);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert time format: " + time12h, e);
        }
    }

    public Integer getLastAddedTimeslotId() {
        return lastAddedTimeslotId;
    }

    private void logResponseStatus(String context, Response response) {
        logger.info("{} response status: {}", context, response.getStatusCode());
        if (response.getStatusCode() >= 400) {
            logger.error("{} failed with status: {}, body: {}", context, response.getStatusCode(), response.getBody().asString());
        }
    }

    private RuntimeException handleApiError(String context, Exception e) {
        logger.error("Error {}: {}", context, e.getMessage(), e);
        return new RuntimeException("Failed while " + context, e);
    }
    /**
     * Utility method to return Gateway ID based on the gateway name.
     */
    public String getGatewayId(String gatewayName) {
        return switch (gatewayName.toLowerCase()) {
            case "stripe" -> configManager.getProperty("gateway.stripe.id");
            case "paymob" -> configManager.getProperty("gateway.paymob.id");
            case "fawry" -> configManager.getProperty("gateway.fawry.id");
            default -> throw new IllegalArgumentException("Unknown gateway: " + gatewayName);
        };
    }

}

