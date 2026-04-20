package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginModal {

    private static final Logger log = LogManager.getLogger(LoginModal.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    // -- @FindBy Locators ----------------------------------------------------

    @FindBy(id = "mobile")
    private WebElement mobileInput;

    @FindBy(id = "mobile_err")
    private WebElement mobileError;

    @FindBy(id = "close_s")
    private WebElement closeButton;

    // -- Constructor ---------------------------------------------------------

    public LoginModal(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    // -- Actions -------------------------------------------------------------

    public void enterPhoneNumber(String phone) {
        wait.until(ExpectedConditions.visibilityOf(mobileInput));
        mobileInput.clear();
        mobileInput.sendKeys(phone);
        log.info("Entered phone number: {}", phone);
    }

    public void submitPhone() {
        mobileInput.submit();
        log.info("Phone number submitted.");
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(mobileError));
        String msg = mobileError.getText().trim();
        log.info("Error message displayed: {}", msg);
        return msg;
    }

    public void closeModal() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
            log.info("Sign-in modal closed.");
        } catch (Exception e) {
            log.warn("Modal close button not found – may already be closed.");
        }
    }
}
