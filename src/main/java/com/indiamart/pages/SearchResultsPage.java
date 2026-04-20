package com.indiamart.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchResultsPage – Page Object for IndiaMart Search Results.
 *
 * LOCATOR PRIORITY:
 *   XPath used here because the cards are identified by class attribute values.
 *   No meaningful ID or Name attributes are available on the card elements.
 *
 * COVERS:
 *   TC01 – reads top 3 service names, URLs, and ratings from result cards
 *
 * NOTE ON RATINGS:
 *   Ratings are attempted from the result page. If not visible (hidden behind
 *   a lock or not shown), the value is "N/A". This does not fail the test.
 *   The phone number is extracted separately in ServiceDetailPage.
 */
public class SearchResultsPage {

    private static final Logger log = LogManager.getLogger(SearchResultsPage.class);

    private static final String RATING_PICKER = "(//*[@id='sellerrating_1']/span[@class='bo color'])[position() <= 3]";

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── XPath Constants ──────────────────────────────────────────────────────

    /** Main card anchor – getText() returns the company/service name */
    private static final String CARD_XPATH = "//a[@class='cardlinks']";

    /**
     * Rating span XPath – relative to each card's parent container.
     * Tries multiple common class names used on IndiaMart for ratings.
     *
     * HOW TO FIX IF RATINGS SHOW AS "N/A":
     *   1. Open IndiaMart in Chrome, search "car wash services" Chennai
     *   2. Right-click the star rating → Inspect
     *   3. Copy the class name of the rating span/div
     *   4. Add it below alongside the existing conditions
     */
    private static final String RATING_XPATH =
            ".//span[contains(@class,'rat') " +
            "     or contains(@class,'rating') " +
            "     or contains(@class,'star')]";

    // ── Constructor ──────────────────────────────────────────────────────────

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    // ── Waits ────────────────────────────────────────────────────────────────

    /**
     * Waits until at least one result card is visible.
     * WAIT TYPE: Explicit wait – best practice for dynamic content.
     */
    public void waitForResults() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(CARD_XPATH)));
        log.info("Search results loaded.");
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /** Returns the service names of the top 3 result cards. */
    public List<String> getTop3Names() {
        List<WebElement> cards = driver.findElements(By.xpath(CARD_XPATH));
        List<String> names = new ArrayList<>();
        for (int i = 0; i < Math.min(3, cards.size()); i++) {
            names.add(cards.get(i).getText().trim());
        }
        log.debug("Top 3 names: {}", names);
        return names;
    }

    /** Returns the detail-page URLs of the top 3 result cards. */
    public List<String> getTop3Urls() {
        List<WebElement> cards = driver.findElements(By.xpath(CARD_XPATH));
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < Math.min(3, cards.size()); i++) {
            urls.add(cards.get(i).getAttribute("href"));
        }
        return urls;
    }

    /**
     * Attempts to extract the rating for each of the top 3 cards.
     * Falls back to "N/A" if not found – test does NOT fail on missing ratings.
     */
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
