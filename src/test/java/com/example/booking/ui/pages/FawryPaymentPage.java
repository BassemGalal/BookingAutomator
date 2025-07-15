package com.example.booking.ui.pages;

import com.example.booking.utils.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for Fawry Payment Page
 */
public class FawryPaymentPage extends BasePage {
    
    private final ConfigManager configManager;
    
    // Fawry payment form elements
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
    
    /**
     * Navigate to Fawry payment page (static URL)
     */
    public void navigateToFawryPayment() {
        String fawryUrl = configManager.getProperty("fawry.payment.url");
        logger.info("Navigating to Fawry payment page: {}", fawryUrl);
        navigateTo(fawryUrl);
        waitForPageLoad();
    }
    
    /**
     * Complete Fawry payment using reference number
     */
    public void completePayment(String referenceNumber) {
        try {
            logger.info("Starting Fawry payment process with reference: {}", referenceNumber);
            
            // Wait for payment form to load
            wait.until(ExpectedConditions.visibilityOf(referenceNumberField));
            
            // Enter reference number
            referenceNumberField.clear();
            referenceNumberField.sendKeys(referenceNumber);
            
            // Enter mobile number (test data)
            mobileNumberField.clear();
            mobileNumberField.sendKeys("01234567890");
            
            logger.info("Reference number and mobile entered successfully");
            
            // Click pay button
            payButton.click();
            logger.info("Pay button clicked");
            
            // Wait for confirmation screen
            Thread.sleep(3000);
            
            // Click confirm button if present
            if (isElementPresent(confirmButton)) {
                confirmButton.click();
                logger.info("Payment confirmed");
            }
            
            // Wait for payment processing
            Thread.sleep(5000);
            
            // Check for success or error
            if (isElementPresent(successMessage)) {
                logger.info("Fawry payment completed successfully");
            } else if (isElementPresent(errorMessage)) {
                logger.error("Payment failed: {}", errorMessage.getText());
                throw new RuntimeException("Payment failed: " + errorMessage.getText());
            } else {
                logger.info("Fawry payment processing completed");
            }
            
        } catch (Exception e) {
            logger.error("Error during Fawry payment: {}", e.getMessage(), e);
            throw new RuntimeException("Fawry payment failed", e);
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