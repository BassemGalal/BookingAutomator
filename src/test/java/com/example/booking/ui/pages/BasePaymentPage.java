package com.example.booking.ui.pages;

import org.openqa.selenium.WebDriver;

public abstract class BasePaymentPage extends BasePage {

    public BasePaymentPage(WebDriver driver) {
        super(driver);
    }

    public abstract BasePaymentPage navigateToPayment(String redirectUrl);

    public abstract void completePayment();
}
