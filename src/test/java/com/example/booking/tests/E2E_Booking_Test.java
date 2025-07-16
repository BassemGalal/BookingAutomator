package com.example.booking.tests;

import com.example.booking.api.pojos.*;
import com.example.booking.ui.DriverManager;
import com.example.booking.ui.pages.*;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class E2E_Booking_Test extends BaseTest {

    private DriverManager driverManager;
    private WebDriver driver;

    @Test(description = "Group A: Stripe Gateway without Coupon", groups = {"gateway", "stripe"})
    public void test_stripeGatewayWithoutCoupon() {
        runGatewayTest("stripe", null, StripePaymentPage.class);
    }

    @Test(description = "Group A: Paymob Gateway without Coupon", groups = {"gateway", "paymob"})
    public void test_paymobGatewayWithoutCoupon() {
        runGatewayTest("paymob", null, PaymobPaymentPage.class);
    }

    @Test(description = "Group A: Fawry Gateway without Coupon", groups = {"gateway", "fawry"})
    public void test_fawryGatewayWithoutCoupon() {
        try {
            initDriver();
            FreeSlotResponse selectedSlot = getAvailableSlot();
            TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);
            String fawryGatewayId = apiClient.getGatewayId("fawry");

            ReservationRequest reservationRequest = new ReservationRequest(timeslotDetails.getTimeslotId(), fawryGatewayId);
            ReservationResponse reservation = createReservation(reservationRequest);

            Assert.assertNotNull(reservation.getReferenceNumber(), "Reference Number is null for Fawry");

            new FawryPaymentPage(driver)
                    .navigateToFawryPayment()
                    .completePayment(reservation.getReferenceNumber());

            validateAndCheckOrder(reservation.getOrderId());
        } finally {
            quitDriver();
        }
    }

    @Test(description = "Group B: Gateway with Partial Coupon", groups = {"coupon", "partial"})
    public void test_gatewayWithPartialCoupon() {
        runGatewayTest("stripe", "DISCOUNT50", StripePaymentPage.class);
    }

    @Test(description = "Group B: 100% Coupon", groups = {"coupon", "full"})
    public void test_100PercentCoupon() {
        FreeSlotResponse selectedSlot = getAvailableSlot();
        TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);

        ReservationRequest reservationRequest = new ReservationRequest(timeslotDetails.getTimeslotId(), null, "DISCOUNT100");
        ReservationResponse reservation = createReservation(reservationRequest);

        Assert.assertNull(reservation.getRedirectToUrl(), "Redirect URL should be null for 100% coupon");

        Response operatorResponse = apiClient.getOperatorOrderToday(reservation.getOrderId());
        Assert.assertEquals(operatorResponse.getStatusCode(), 200);

        OperatorOrderResponse operatorOrder = operatorResponse.as(OperatorOrderResponse.class);
        Assert.assertEquals(operatorOrder.getTransactionStatus(), "SUCCESS");
    }

    // ==== Helper Methods ====

    private FreeSlotResponse getAvailableSlot() {
        Response response = apiClient.getFreeSlots();
        Assert.assertEquals(response.getStatusCode(), 200);
        FreeSlotResponse[] slots = response.as(FreeSlotResponse[].class);
        Assert.assertTrue(slots.length > 0, "No free slots available");
        return slots[0];
    }

    private TimeslotDetailsResponse getTimeslotDetails(FreeSlotResponse slot) {
        Response response = apiClient.getTimeslotDetails(slot.getTSlotDate(), slot.getTSlotTime(), slot.getServiceId());
        Assert.assertEquals(response.getStatusCode(), 200);
        return response.as(TimeslotDetailsResponse.class);
    }

    private ReservationResponse createReservation(ReservationRequest request) {
        Response response = apiClient.createReservation(request);
        Assert.assertEquals(response.getStatusCode(), 200);
        ReservationResponse reservation = response.as(ReservationResponse.class);
        Assert.assertNotNull(reservation.getOrderId(), "Order ID is null");
        return reservation;
    }

    private void validateAndCheckOrder(String orderId) {
        Response validationResponse = apiClient.validateTransaction(orderId);
        if (validationResponse.getStatusCode() != 200) {
            apiClient.simulateCron(orderId);
        }
        Response operatorResponse = apiClient.getOperatorOrderToday(orderId);
        Assert.assertEquals(operatorResponse.getStatusCode(), 200);
        OperatorOrderResponse operatorOrder = operatorResponse.as(OperatorOrderResponse.class);
        Assert.assertEquals(operatorOrder.getTransactionStatus(), "SUCCESS");
    }

    private void initDriver() {
        if (driverManager == null) {
            driverManager = new DriverManager();
            driverManager.initializeDriver();
            driver = driverManager.getDriver();
        }
    }

    private void quitDriver() {
        if (driverManager != null) {
            driverManager.quitDriver();
            driverManager = null;
            driver = null;
        }
    }

    private <T extends BasePaymentPage> void runGatewayTest(String gateway, String coupon, Class<T> paymentPageClass) {
        try {
            initDriver();
            FreeSlotResponse selectedSlot = getAvailableSlot();
            TimeslotDetailsResponse timeslotDetails = getTimeslotDetails(selectedSlot);
            String gatewayId = apiClient.getGatewayId(gateway);

            ReservationRequest reservationRequest = new ReservationRequest(timeslotDetails.getTimeslotId(), gatewayId, coupon);
            ReservationResponse reservation = createReservation(reservationRequest);

            String redirectUrl = reservation.getRedirectToUrl();
            Assert.assertNotNull(redirectUrl, "Redirect URL is null for gateway: " + gateway);

            BasePaymentPage paymentPage = paymentPageClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
            paymentPage.navigateToPayment(redirectUrl).completePayment();

            validateAndCheckOrder(reservation.getOrderId());
        } catch (Exception e) {
            throw new RuntimeException("Failed during gateway test for: " + gateway, e);
        } finally {
            quitDriver();
        }
    }
}
