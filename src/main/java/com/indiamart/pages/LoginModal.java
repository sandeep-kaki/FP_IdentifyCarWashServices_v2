package com.indiamart.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * LoginModal – Page Object for the IndiaMart Sign-In modal.
 *
 * LOCATOR PRIORITY:
 *   ID used for all elements here (mobile, mobile_err, close_s) –
 *   these are unique IDs, so ID is the best and fastest locator choice.
 *
 * COVERS:
 *   TC03 – enters invalid phone, submits, reads error message
 */
public class LoginModal {

    private static final Logger log = LogManager.getLogger(LoginModal.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── @FindBy Locators ──────────────────────────────────────────────────────
    // All use ID – top locator priority

    /** Phone number input field inside the modal. LOCATOR: ID */
    @FindBy(id = "mobile")
    private WebElement mobileInput;

    /** Error message shown below mobile field on invalid input. LOCATOR: ID */
    @FindBy(id = "mobile_err")
    private WebElement mobileError;

    /** Close button (X) of the modal. LOCATOR: ID */
    @FindBy(id = "close_s")
    private WebElement closeButton;

    // ── Constructor ──────────────────────────────────────────────────────────

    public LoginModal(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    /**
     * Waits for the mobile field to be visible, then types the phone number.
     * WAIT TYPE: Explicit wait – required because modal animates into view.
     */
    public void enterPhoneNumber(String phone) {
        wait.until(ExpectedConditions.visibilityOf(mobileInput));
        mobileInput.clear();
        mobileInput.sendKeys(phone);
        log.info("Entered phone number: {}", phone);
    }

    /**
     * Submits the mobile input form.
     * Matches the original linear code: mobile.submit()
     */
    public void submitPhone() {
        mobileInput.submit();
        log.info("Phone number submitted.");
    }

    /**
     * Waits for the error element to appear and returns its text.
     * Called AFTER submitPhone() – the error appears asynchronously.
     * WAIT TYPE: Explicit wait.
     */
    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(mobileError));
        String msg = mobileError.getText().trim();
        log.info("Error message displayed: {}", msg);
        return msg;
    }

    /**
     * Closes the sign-in modal by clicking the X button.
     * Wrapped in try-catch – modal may already be dismissed in some cases.
     */
    public void closeModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
            log.info("Sign-in modal closed.");
        } catch (Exception e) {
            log.warn("Modal close button not found – may already be closed.");
        }
    }
}
