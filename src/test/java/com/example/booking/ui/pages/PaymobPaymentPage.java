package com.example.booking.ui.pages;

import com.example.booking.utils.ConfigManager;
import com.example.booking.utils.JsonUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Map;

/**
 * Page Object for Paymob Payment Page
 */
public class PaymobPaymentPage extends BasePaymentPage {

    private final ConfigManager configManager;
    private final JsonUtil jsonUtil;
    
    // Paymob payment form elements
    @FindBy(css = "input[name='pan']")
    private WebElement cardNumberField;
    
    @FindBy(css = "input[name='expiry_month']")
    private WebElement expiryMonthField;
    
    @FindBy(css = "input[name='expiry_year']")
    private WebElement expiryYearField;
    
    @FindBy(css = "input[name='cvv']")
    private WebElement cvvField;
    
    @FindBy(css = "input[name='holder_name']")
    private WebElement cardHolderNameField;
    
    @FindBy(css = "button[id='pay-btn']")
    private WebElement payButton;
    
    @FindBy(css = ".success-message")
    private WebElement successMessage;
    
    @FindBy(css = ".error-message")
    private WebElement errorMessage;
    
    public PaymobPaymentPage(WebDriver driver) {
        super(driver);
        this.configManager = ConfigManager.getInstance();
        this.jsonUtil = new JsonUtil();
    }
    
    /**
     * Navigate to Paymob payment page
     */
    @Override
    public BasePaymentPage navigateToPayment(String redirectUrl) {
        logger.info("Navigating to Paymob payment page: {}", redirectUrl);
        navigateTo(redirectUrl);
        waitForPageLoad();
        return this;
    }
    
    /**
     * Complete Paymob payment using test card data
     */
    public void completePayment() {
        try {
            logger.info("Starting Paymob payment process");
            
            // Load test card data
            Map<String, Object> testCardData = jsonUtil.loadTestData("stripe_test_card.json");
            
            // Wait for payment form to load
            wait.until(ExpectedConditions.visibilityOf(cardNumberField));
            
            // Fill card details
            cardNumberField.clear();
            cardNumberField.sendKeys(testCardData.get("card_number").toString());
            
            // Split expiry into month and year
            String expiry = testCardData.get("expiry").toString();
            String[] expiryParts = expiry.split("/");
            
            expiryMonthField.clear();
            expiryMonthField.sendKeys(expiryParts[0]);
            
            expiryYearField.clear();
            expiryYearField.sendKeys(expiryParts[1]);
            
            cvvField.clear();
            cvvField.sendKeys(testCardData.get("cvc").toString());
            
            cardHolderNameField.clear();
            cardHolderNameField.sendKeys(testCardData.get("card_holder_name").toString());
            
            logger.info("Card details entered successfully");
            
            // Submit payment
            payButton.click();
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
            logger.error("Error during Paymob payment: {}", e.getMessage(), e);
            throw new RuntimeException("Paymob payment failed", e);
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