package com.example.booking.ui.pages;

import com.example.booking.utils.ConfigManager;
import com.example.booking.utils.JsonUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Map;

/**
 * Page Object for Stripe Payment Page
 */
public class StripePaymentPage extends BasePage {
    
    private final ConfigManager configManager;
    private final JsonUtil jsonUtil;
    
    // Stripe payment form elements
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
    
    /**
     * Navigate to Stripe payment page
     */
    public void navigateToPayment(String redirectUrl) {
        logger.info("Navigating to Stripe payment page: {}", redirectUrl);
        navigateTo(redirectUrl);
        waitForPageLoad();
    }
    
    /**
     * Complete Stripe payment using test card data
     */
    public void completePayment() {
        try {
            logger.info("Starting Stripe payment process");
            
            // Load test card data
            Map<String, Object> testCardData = jsonUtil.loadTestData("stripe_test_card.json");
            
            // Wait for payment form to load
            wait.until(ExpectedConditions.visibilityOf(cardNumberField));
            
            // Fill card details
            cardNumberField.clear();
            cardNumberField.sendKeys(testCardData.get("card_number").toString());
            
            expiryField.clear();
            expiryField.sendKeys(testCardData.get("expiry").toString());
            
            cvcField.clear();
            cvcField.sendKeys(testCardData.get("cvc").toString());
            
            cardHolderNameField.clear();
            cardHolderNameField.sendKeys(testCardData.get("card_holder_name").toString());
            
            logger.info("Card details entered successfully");
            
            // Submit payment
            submitButton.click();
            logger.info("Payment submitted");
            
            // Wait for payment processing
            Thread.sleep(5000);
            
            // Check for success or error
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
    
    /**
     * Check if element is present on page
     */
    private boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}