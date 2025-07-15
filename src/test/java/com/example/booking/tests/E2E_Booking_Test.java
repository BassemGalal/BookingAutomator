package com.example.booking.tests;

import com.example.booking.api.ApiClient;
import com.example.booking.api.pojos.*;
import com.example.booking.ui.DriverManager;
import com.example.booking.ui.pages.FawryPaymentPage;
import com.example.booking.ui.pages.PaymobPaymentPage;
import com.example.booking.ui.pages.StripePaymentPage;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Contains all End-to-End test cases for booking functionality
 * Tests complete user journeys from search to payment confirmation
 */
public class E2E_Booking_Test extends BaseTest {
    
    private ApiClient apiClient;
    private DriverManager driverManager;
    
    @Test(description = "Group A: Stripe Gateway without Coupon", groups = {"gateway", "stripe"})
    public void test_stripeGatewayWithoutCoupon() {
        logger.info("Starting Stripe gateway test without coupon");
        
        try {
            // Initialize API client if not already done
            if (apiClient == null) {
                apiClient = new ApiClient();
            }
            
            // Step 1: Get Free Slots
            Response freeSlotsResponse = apiClient.getFreeSlots();
            Assert.assertEquals(freeSlotsResponse.getStatusCode(), 200);
            
            FreeSlotResponse[] freeSlots = freeSlotsResponse.as(FreeSlotResponse[].class);
            Assert.assertTrue(freeSlots.length > 0, "No free slots available");
            
            FreeSlotResponse selectedSlot = freeSlots[0];
            logger.info("Selected slot: {}", selectedSlot);
            
            // Step 2: Get Timeslot Details
            Response timeslotResponse = apiClient.getTimeslotDetails(
                selectedSlot.getTSlotDate(), 
                selectedSlot.getTSlotTime(), 
                selectedSlot.getServiceId()
            );
            Assert.assertEquals(timeslotResponse.getStatusCode(), 200);
            
            TimeslotDetailsResponse timeslotDetails = timeslotResponse.as(TimeslotDetailsResponse.class);
            Assert.assertNotNull(timeslotDetails.getTimeslotId(), "Timeslot ID is null");
            logger.info("Timeslot details: {}", timeslotDetails);
            
            // Step 3: Create Reservation
            String stripeGatewayId = apiClient.getGatewayId("stripe");
            ReservationRequest reservationRequest = new ReservationRequest(
                timeslotDetails.getTimeslotId(), 
                stripeGatewayId
            );
            
            Response reservationResponse = apiClient.createReservation(reservationRequest);
            Assert.assertEquals(reservationResponse.getStatusCode(), 200);
            
            ReservationResponse reservation = reservationResponse.as(ReservationResponse.class);
            Assert.assertNotNull(reservation.getOrderId(), "Order ID is null");
            Assert.assertNotNull(reservation.getRedirectToUrl(), "Redirect URL is null");
            logger.info("Reservation created: {}", reservation);
            
            // Step 4: UI Payment Flow
            driverManager = new DriverManager();
            driverManager.initializeDriver();
            
            StripePaymentPage stripePaymentPage = new StripePaymentPage(driverManager.getDriver());
            stripePaymentPage.navigateToPayment(reservation.getRedirectToUrl());
            stripePaymentPage.completePayment();
            
            // Step 5: Validate Transaction
            Response validationResponse = apiClient.validateTransaction(reservation.getOrderId());
            if (validationResponse.getStatusCode() != 200) {
                logger.warn("Validation failed, trying cron simulation");
                Response cronResponse = apiClient.simulateCron(reservation.getOrderId());
                Assert.assertEquals(cronResponse.getStatusCode(), 200);
            }
            
            // Step 6: Final Check via Operator API
            Response operatorResponse = apiClient.getOperatorOrderToday(reservation.getOrderId());
            Assert.assertEquals(operatorResponse.getStatusCode(), 200);
            
            OperatorOrderResponse operatorOrder = operatorResponse.as(OperatorOrderResponse.class);
            Assert.assertEquals(operatorOrder.getOrderId(), reservation.getOrderId());
            Assert.assertEquals(operatorOrder.getPaymentStatus(), "COMPLETED");
            
            logger.info("Stripe payment test completed successfully");
            
        } catch (Exception e) {
            logger.error("Stripe gateway test failed: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        } finally {
            if (driverManager != null) {
                driverManager.quitDriver();
            }
        }
    }
    
    @Test(description = "Group A: Paymob Gateway without Coupon", groups = {"gateway", "paymob"})
    public void test_paymobGatewayWithoutCoupon() {
        logger.info("Starting Paymob gateway test without coupon");
        
        try {
            // Step 1-2: Get Free Slots and Timeslot Details (same as Stripe)
            FreeSlotResponse selectedSlot = getAvailableSlot();
            TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);
            
            // Step 3: Create Reservation with Paymob
            String paymobGatewayId = apiClient.getGatewayId("paymob");
            ReservationRequest reservationRequest = new ReservationRequest(
                timeslotDetails.getTimeslotId(), 
                paymobGatewayId
            );
            
            Response reservationResponse = apiClient.createReservation(reservationRequest);
            Assert.assertEquals(reservationResponse.getStatusCode(), 200);
            
            ReservationResponse reservation = reservationResponse.as(ReservationResponse.class);
            Assert.assertNotNull(reservation.getOrderId(), "Order ID is null");
            Assert.assertNotNull(reservation.getRedirectToUrl(), "Redirect URL is null");
            
            // Step 4: UI Payment Flow
            driverManager = new DriverManager();
            driverManager.initializeDriver();
            
            PaymobPaymentPage paymobPaymentPage = new PaymobPaymentPage(driverManager.getDriver());
            paymobPaymentPage.navigateToPayment(reservation.getRedirectToUrl());
            paymobPaymentPage.completePayment();
            
            // Step 5-6: Validate and Check Order
            validateAndCheckOrder(reservation.getOrderId());
            
            logger.info("Paymob payment test completed successfully");
            
        } catch (Exception e) {
            logger.error("Paymob gateway test failed: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        } finally {
            if (driverManager != null) {
                driverManager.quitDriver();
            }
        }
    }
    
    @Test(description = "Group A: Fawry Gateway without Coupon", groups = {"gateway", "fawry"})
    public void test_fawryGatewayWithoutCoupon() {
        logger.info("Starting Fawry gateway test without coupon");
        
        try {
            // Step 1-2: Get Free Slots and Timeslot Details
            FreeSlotResponse selectedSlot = getAvailableSlot();
            TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);
            
            // Step 3: Create Reservation with Fawry
            String fawryGatewayId = apiClient.getGatewayId("fawry");
            ReservationRequest reservationRequest = new ReservationRequest(
                timeslotDetails.getTimeslotId(), 
                fawryGatewayId
            );
            
            Response reservationResponse = apiClient.createReservation(reservationRequest);
            Assert.assertEquals(reservationResponse.getStatusCode(), 200);
            
            ReservationResponse reservation = reservationResponse.as(ReservationResponse.class);
            Assert.assertNotNull(reservation.getOrderId(), "Order ID is null");
            Assert.assertNotNull(reservation.getReferenceNumber(), "Reference Number is null for Fawry");
            
            // Step 4: UI Payment Flow (Fawry uses static URL)
            driverManager = new DriverManager();
            driverManager.initializeDriver();
            
            FawryPaymentPage fawryPaymentPage = new FawryPaymentPage(driverManager.getDriver());
            fawryPaymentPage.navigateToFawryPayment();
            fawryPaymentPage.completePayment(reservation.getReferenceNumber());
            
            // Step 5-6: Validate and Check Order
            validateAndCheckOrder(reservation.getOrderId());
            
            logger.info("Fawry payment test completed successfully");
            
        } catch (Exception e) {
            logger.error("Fawry gateway test failed: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        } finally {
            if (driverManager != null) {
                driverManager.quitDriver();
            }
        }
    }
    
    @Test(description = "Group B: Gateway with Partial Coupon", groups = {"coupon", "partial"})
    public void test_gatewayWithPartialCoupon() {
        logger.info("Starting partial coupon test");
        
        try {
            // Step 1-2: Get Free Slots and Timeslot Details
            FreeSlotResponse selectedSlot = getAvailableSlot();
            TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);
            
            // Step 3: Create Reservation with Coupon (50% discount)
            String stripeGatewayId = apiClient.getGatewayId("stripe");
            ReservationRequest reservationRequest = new ReservationRequest(
                timeslotDetails.getTimeslotId(), 
                stripeGatewayId,
                "DISCOUNT50"
            );
            
            Response reservationResponse = apiClient.createReservation(reservationRequest);
            Assert.assertEquals(reservationResponse.getStatusCode(), 200);
            
            ReservationResponse reservation = reservationResponse.as(ReservationResponse.class);
            Assert.assertNotNull(reservation.getOrderId(), "Order ID is null");
            Assert.assertNotNull(reservation.getRedirectToUrl(), "Redirect URL is null");
            
            // Verify discounted amount
            Assert.assertTrue(reservation.getAmount() < timeslotDetails.getPrice(), 
                            "Coupon discount not applied");
            
            // Step 4: UI Payment Flow (same as normal)
            driverManager = new DriverManager();
            driverManager.initializeDriver();
            
            StripePaymentPage stripePaymentPage = new StripePaymentPage(driverManager.getDriver());
            stripePaymentPage.navigateToPayment(reservation.getRedirectToUrl());
            stripePaymentPage.completePayment();
            
            // Step 5-6: Validate and Check Order
            validateAndCheckOrder(reservation.getOrderId());
            
            logger.info("Partial coupon test completed successfully");
            
        } catch (Exception e) {
            logger.error("Partial coupon test failed: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        } finally {
            if (driverManager != null) {
                driverManager.quitDriver();
            }
        }
    }
    
    @Test(description = "Group B: 100% Coupon", groups = {"coupon", "full"})
    public void test_100PercentCoupon() {
        logger.info("Starting 100% coupon test");
        
        try {
            // Step 1-2: Get Free Slots and Timeslot Details
            FreeSlotResponse selectedSlot = getAvailableSlot();
            TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);
            
            // Step 3: Create Reservation with 100% Coupon (no gateway needed)
            ReservationRequest reservationRequest = new ReservationRequest(
                timeslotDetails.getTimeslotId(), 
                null,  // gatewayId = null for 100% coupon
                "DISCOUNT100"
            );
            
            Response reservationResponse = apiClient.createReservation(reservationRequest);
            Assert.assertEquals(reservationResponse.getStatusCode(), 200);
            
            ReservationResponse reservation = reservationResponse.as(ReservationResponse.class);
            Assert.assertNotNull(reservation.getOrderId(), "Order ID is null");
            Assert.assertNull(reservation.getRedirectToUrl(), "Redirect URL should be null for 100% coupon");
            Assert.assertEquals(reservation.getAmount(), 0.0, "Amount should be 0 for 100% coupon");
            
            // Step 4: Skip UI Payment (no redirect needed)
            logger.info("Skipping UI payment for 100% coupon");
            
            // Step 5: Direct check via Operator API
            Response operatorResponse = apiClient.getOperatorOrderToday(reservation.getOrderId());
            Assert.assertEquals(operatorResponse.getStatusCode(), 200);
            
            OperatorOrderResponse operatorOrder = operatorResponse.as(OperatorOrderResponse.class);
            Assert.assertEquals(operatorOrder.getOrderId(), reservation.getOrderId());
            Assert.assertEquals(operatorOrder.getPaymentStatus(), "COMPLETED");
            
            logger.info("100% coupon test completed successfully");
            
        } catch (Exception e) {
            logger.error("100% coupon test failed: {}", e.getMessage(), e);
            Assert.fail("Test failed: " + e.getMessage());
        }
    }
    
    // Helper methods
    private FreeSlotResponse getAvailableSlot() {
        // Initialize API client if not already done
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        
        Response freeSlotsResponse = apiClient.getFreeSlots();
        Assert.assertEquals(freeSlotsResponse.getStatusCode(), 200);
        
        FreeSlotResponse[] freeSlots = freeSlotsResponse.as(FreeSlotResponse[].class);
        Assert.assertTrue(freeSlots.length > 0, "No free slots available");
        
        return freeSlots[0];
    }
    
    private TimeslotDetailsResponse getTimeslotDetails(FreeSlotResponse slot) {
        Response timeslotResponse = apiClient.getTimeslotDetails(
            slot.getTSlotDate(), 
            slot.getTSlotTime(), 
            slot.getServiceId()
        );
        Assert.assertEquals(timeslotResponse.getStatusCode(), 200);
        
        TimeslotDetailsResponse timeslotDetails = timeslotResponse.as(TimeslotDetailsResponse.class);
        Assert.assertNotNull(timeslotDetails.getTimeslotId(), "Timeslot ID is null");
        
        return timeslotDetails;
    }
    
    private void validateAndCheckOrder(String orderId) {
        // Step 5: Validate Transaction
        Response validationResponse = apiClient.validateTransaction(orderId);
        if (validationResponse.getStatusCode() != 200) {
            logger.warn("Validation failed, trying cron simulation");
            Response cronResponse = apiClient.simulateCron(orderId);
            Assert.assertEquals(cronResponse.getStatusCode(), 200);
        }
        
        // Step 6: Final Check via Operator API
        Response operatorResponse = apiClient.getOperatorOrderToday(orderId);
        Assert.assertEquals(operatorResponse.getStatusCode(), 200);
        
        OperatorOrderResponse operatorOrder = operatorResponse.as(OperatorOrderResponse.class);
        Assert.assertEquals(operatorOrder.getOrderId(), orderId);
        Assert.assertEquals(operatorOrder.getPaymentStatus(), "COMPLETED");
    }
}
