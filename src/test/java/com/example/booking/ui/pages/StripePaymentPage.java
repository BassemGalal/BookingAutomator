package com.example.booking.ui.pages;

import com.example.booking.utils.ConfigManager;
import com.example.booking.utils.JsonUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Map;

/**
 * Page Object for Stripe Payment Page
 */
public class StripePaymentPage extends BasePaymentPage {

    private final ConfigManager configManager;
    private final JsonUtil jsonUtil;

    @FindBy(css = "input[name='cardNumber']")
    private WebElement cardNumberField;

    @FindBy(css = "input[name='expiry']")
    private WebElement expiryField;

    @FindBy(css = "input[name='cvc']")
    private WebElement cvcField;

    @FindBy(css = "input[name='name']")
    private WebElement cardHolderNameField;

    @FindBy(css = "button[type='submit']")
    private WebElement submitButton;

    @FindBy(css = ".payment-success")
    private WebElement successMessage;

    @FindBy(css = ".payment-error")
    private WebElement errorMessage;

    public StripePaymentPage(WebDriver driver) {
        super(driver);
        this.configManager = ConfigManager.getInstance();
        this.jsonUtil = new JsonUtil();
    }

    @Override
    public BasePaymentPage navigateToPayment(String redirectUrl) {
        logger.info("Navigating to Stripe payment page: {}", redirectUrl);
        navigateTo(redirectUrl);
        waitForPageLoad();
        return this;
    }

    @Override
    public void completePayment() {
        try {
            logger.info("Starting Stripe payment process");

            Map<String, Object> testCardData = jsonUtil.loadTestData("stripe_test_card.json");

            wait.until(ExpectedConditions.visibilityOf(cardNumberField));

            cardNumberField.clear();
            cardNumberField.sendKeys(testCardData.get("card_number").toString());

            expiryField.clear();
            expiryField.sendKeys(testCardData.get("expiry").toString());

            cvcField.clear();
            cvcField.sendKeys(testCardData.get("cvc").toString());

            cardHolderNameField.clear();
            cardHolderNameField.sendKeys(testCardData.get("card_holder_name").toString());

            logger.info("Card details entered successfully");

            submitButton.click();
            logger.info("Payment submitted");

            Thread.sleep(5000);

            if (isElementPresent(successMessage)) {
                logger.info("Payment completed successfully");
            } else if (isElementPresent(errorMessage)) {
                logger.error("Payment failed: {}", errorMessage.getText());
                throw new RuntimeException("Payment failed: " + errorMessage.getText());
            } else {
                logger.info("Payment processing completed");
            }

        } catch (Exception e) {
            logger.error("Error during Stripe payment: {}", e.getMessage(), e);
            throw new RuntimeException("Stripe payment failed", e);
        }
    }

    private boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
