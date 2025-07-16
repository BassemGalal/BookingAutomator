package com.example.booking.ui.pages;

import com.example.booking.utils.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for Fawry Payment Page
 */
public class FawryPaymentPage extends BasePaymentPage {

    private final ConfigManager configManager;
    private String currentReferenceNumber;

    @FindBy(css = "input[name='reference_number']")
    private WebElement referenceNumberField;

    @FindBy(css = "input[name='mobile_number']")
    private WebElement mobileNumberField;

    @FindBy(css = "button[id='pay-btn']")
    private WebElement payButton;

    @FindBy(css = "button[id='confirm-btn']")
    private WebElement confirmButton;

    @FindBy(css = ".payment-success")
    private WebElement successMessage;

    @FindBy(css = ".payment-error")
    private WebElement errorMessage;

    public FawryPaymentPage(WebDriver driver) {
        super(driver);
        this.configManager = ConfigManager.getInstance();
    }

    @Override
    public BasePaymentPage navigateToPayment(String redirectUrl) {
        logger.info("Navigating to Fawry payment page: {}", redirectUrl);
        navigateTo(redirectUrl);
        waitForPageLoad();
        return this;
    }

    public BasePaymentPage navigateToFawryPayment() {
        String fawryUrl = configManager.getProperty("fawry.payment.url");
        return navigateToPayment(fawryUrl);
    }

    public FawryPaymentPage completePayment(String referenceNumber) {
        this.currentReferenceNumber = referenceNumber;
        completePayment();
        return this;
    }

    @Override
    public void completePayment() {
        try {
            if (currentReferenceNumber == null) {
                throw new IllegalStateException("Reference number is required for Fawry payment.");
            }

            logger.info("Starting Fawry payment process with reference: {}", currentReferenceNumber);

            wait.until(ExpectedConditions.visibilityOf(referenceNumberField)).clear();
            referenceNumberField.sendKeys(currentReferenceNumber);

            String mobileNumber = configManager.getProperty("fawry.test.mobile", "01234567890");
            mobileNumberField.clear();
            mobileNumberField.sendKeys(mobileNumber);

            logger.info("Reference number and mobile number entered");

            payButton.click();
            logger.info("Pay button clicked");

            waitForOptionalElement(confirmButton, 5);
            if (isElementPresent(confirmButton)) {
                confirmButton.click();
                logger.info("Confirm button clicked");
            }

            waitForOptionalElement(successMessage, 5);
            waitForOptionalElement(errorMessage, 5);

            if (isElementPresent(successMessage)) {
                logger.info("Fawry payment completed successfully");
            } else if (isElementPresent(errorMessage)) {
                String errorText = errorMessage.getText();
                logger.error("Payment failed: {}", errorText);
                throw new RuntimeException("Payment failed: " + errorText);
            } else {
                logger.warn("Payment result is unknown - no success or error message detected");
            }

        } catch (Exception e) {
            logger.error("Error during Fawry payment: {}", e.getMessage(), e);
            throw new RuntimeException("Fawry payment failed", e);
        }
    }

    private boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForOptionalElement(WebElement element, int seconds) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            shortWait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception ignored) {
        }
    }
}
