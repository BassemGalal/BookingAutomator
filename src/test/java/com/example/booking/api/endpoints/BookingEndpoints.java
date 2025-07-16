package com.example.booking.api.endpoints;

/**
 * Contains all API endpoint constants for the booking system
 */
public class BookingEndpoints {
    
    // Free slots endpoint
    public static final String FREE_SLOTS = "/therapist-api/tSlots";
    
    // Timeslot details endpoint
    public static final String ADD_SINGLE_TIMESLOT = "/therapist-api/tSlots/addSingle";
    
    // Reservation endpoint
    public static final String RESERVATION = "client/reservation/pay";
    
    // Validate transaction endpoint
    public static final String VALIDATE_TRANSACTION = "/client/reservation/pending-payments/validate";
    
    // Cron simulation endpoint
    public static final String CRON_SIMULATION = "/crons/pending-payments/validate";
    
    // Operator orders endpoint
    public static final String OPERATOR_ORDERS = "/shezlong-operator/payments/paymentAttempts";
    
}