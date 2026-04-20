package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsPage {

    private static final Logger log = LogManager.getLogger(SearchResultsPage.class);

    private static final String RATING_PICKER = "(//*[@id='sellerrating_1']/span[@class='bo color'])[position() <= 3]";

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // -- XPath Constants ----------------------------------------------------

    private static final String CARD_XPATH = "//a[@class='cardlinks']";

    private static final String RATING_XPATH =
            ".//span[contains(@class,'rat') " +
            "     or contains(@class,'rating') " +
            "     or contains(@class,'star')]";

    // -- Constructor --------------------------------------------------------

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    // -- Waits -------------------------------------------------------------

    public void waitForResults() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CARD_XPATH)));
        log.info("Search results loaded.");
    }

    // -- Getters -----------------------------------------------------------

    public List<String> getTop3Names() {
        List<WebElement> cards = driver.findElements(By.xpath(CARD_XPATH));
        List<String> names = new ArrayList<>();
        for (int i = 0; i < Math.min(3, cards.size()); i++) {
            names.add(cards.get(i).getText().trim());
        }
        log.debug("Top 3 names: {}", names);
        return names;
    }

    public List<String> getTop3Urls() {
        List<WebElement> cards = driver.findElements(By.xpath(CARD_XPATH));
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < Math.min(3, cards.size()); i++) {
            urls.add(cards.get(i).getAttribute("href"));
        }
        return urls;
    }

    public List<String> getTop3Ratings() {
        List<WebElement> ratingsWebElements = driver.findElements(By.xpath(RATING_PICKER));
        List<String> ratings = new ArrayList<>();

        for (int i = 0; i < Math.min(3, ratingsWebElements.size()); i++) {
            String rating = "N/A";
            rating = ratingsWebElements.get(i).getText();

            ratings.add(rating);
        }
        return ratings;
    }
}
