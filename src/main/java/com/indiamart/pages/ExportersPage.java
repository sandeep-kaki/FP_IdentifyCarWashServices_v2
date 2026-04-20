package com.indiamart.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * ExportersPage – Page Object for the IndiaMart Exporters section.
 *
 * LOCATOR PRIORITY:
 *   XPath  – for the heading (Tailwind class string used as XPath attribute)
 *   CSS    – for the container (class-based CSS selector from original code)
 *   XPath  – for category links (two class conditions on anchor elements)
 *   All three taken directly from the original working linear code.
 *
 * COVERS:
 *   TC04 – reads section title, top 5 product categories, scrolls for screenshot
 */
public class ExportersPage {

    private static final Logger log = LogManager.getLogger(ExportersPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ── @FindBy Locators ──────────────────────────────────────────────────────

    /**
     * "Top Product Categories from India" section heading.
     * LOCATOR: XPath with @class (Tailwind utility string – from original code).
     * NOTE: If this stops working, inspect the <h2> in DevTools and update the class value.
     */
    @FindBy(xpath = "//h2[@class='text-center font-normal text-gray-800 mb-6 " +
                    "text-[20px] md:text-[28px] leading-[28px]']")
    private WebElement sectionHeading;

    /**
     * Top categories container – used as scroll target before screenshot.
     * LOCATOR: CSS selector (from original code).
     */
    @FindBy(css = "div.px-4.py-8.bg-gray-50")
    private WebElement topCategoriesContainer;

    /** Category links XPath – from original code. LOCATOR: XPath with two class conditions. */
    private static final String CATEGORY_XPATH =
            "//a[contains(@class,'text-base') and contains(@class,'font-semibold')]";

    // ── Constructor ──────────────────────────────────────────────────────────

    public ExportersPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js     = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    /**
     * Waits for the page heading to be visible – confirms Exporters page loaded.
     * WAIT TYPE: Explicit wait.
     */
    public void waitForPage() {
        wait.until(ExpectedConditions.visibilityOf(sectionHeading));
        log.info("Exporters page loaded.");
    }

    /** Returns the section heading text. */
    public String getSectionTitle() {
        return sectionHeading.getText().trim();
    }

    /**
     * Returns the top 5 product category names.
     * Capped at 5 to match the original code.
     */
    public List<String> getTopProductCategories() {
        List<WebElement> els = driver.findElements(By.xpath(CATEGORY_XPATH));
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < Math.min(5, els.size()); i++) {
            String text = els.get(i).getText().trim();
            if (!text.isEmpty()) categories.add(text);
        }
        log.info("Extracted {} top product categories.", categories.size());
        return categories;
    }

    /**
     * Scrolls the top-categories container into view.
     * Called just before taking the screenshot so the section is fully visible.
     * WAIT TYPE: Explicit wait on the container before scrolling.
     */
    public void scrollToTopCategories() {
        wait.until(ExpectedConditions.visibilityOf(topCategoriesContainer));
        js.executeScript("arguments[0].scrollIntoView(true)", topCategoriesContainer);
        log.info("Scrolled to top categories section.");
    }
}
