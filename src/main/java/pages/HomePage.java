package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ── @FindBy Locators (PageFactory) -------------------------------------

    @FindBy(id = "hd_usercity")
    private WebElement cityDisplay;

    @FindBy(id = "hd_city_sugg")
    private WebElement cityInput;

    @FindBy(xpath = "//*[@id='anchor']")
    private WebElement cityAutoSuggest;

    @FindBy(name = "ss")
    private WebElement searchBox;

    @FindBy(linkText = "Food, Agriculture & Farming")
    private WebElement foodAgriLink;

    @FindBy(id = "user_sign_in")
    private WebElement signInButton;

    @FindBy(id = "exporters")
    private WebElement exportersTab;

    // -- Constructor ----------------------------------------------------

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js     = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);   // wires @FindBy elements
    }

    // -- TC01 Actions ----------------------------------------------------

    public void setCity(String targetCity) {
        String current = cityDisplay.getText().trim();
        log.info("Current city: '{}', Target city: '{}'", current, targetCity);

        if (!current.equalsIgnoreCase(targetCity)) {
            cityDisplay.click();
            cityInput.sendKeys(targetCity);                                   // type in city input box
            wait.until(ExpectedConditions.elementToBeClickable(cityAutoSuggest)).click(); // pick suggestion
            log.info("City set to: {}", targetCity);
        } else {
            log.info("City already set to {}. Skipping city selection.", targetCity);
        }
    }

    public void searchFor(String keyword) {
        log.info("Searching for: {}", keyword);
        searchBox.sendKeys(keyword, Keys.ENTER);
    }

    // -- TC02 Actions ----------------------------------------------------

    public void scrollToFoodAgriSection() {
        wait.until(ExpectedConditions.visibilityOf(foodAgriLink));
        js.executeScript("arguments[0].scrollIntoView(true)", foodAgriLink);
        log.info("Scrolled to Food, Agriculture & Farming section.");
    }

    public void closePopupIfPresent() {
        try {
            WebElement popup = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("idfpclose")));
            js.executeScript("arguments[0].click()", popup);
            log.info("Popup dismissed.");
        } catch (Exception e) {
            log.debug("No popup found – continuing.");
        }
    }

    public List<String> getFoodAgriProductNames() {
        List<WebElement> items = driver.findElements(By.xpath(
                "//h2[normalize-space()='Food, Agriculture & Farming']" +
                "/following-sibling::div //div[contains(@class,'product-item')]//h3"));

        List<String> names = new ArrayList<>();
        for (WebElement el : items) {
            String text = el.getText().trim();
            if (!text.isEmpty()) names.add(text);
        }
        log.info("Found {} products in Food, Agriculture & Farming.", names.size());
        return names;
    }

    // -- TC03 Action ----------------------------------------------------

    public void clickSignIn() {
        log.info("Clicking Sign In button.");
        signInButton.click();
    }

    // -- TC04 Action ----------------------------------------------------

    public void clickExporters() {
        log.info("Clicking Exporters tab.");
        exportersTab.click();
    }
}
