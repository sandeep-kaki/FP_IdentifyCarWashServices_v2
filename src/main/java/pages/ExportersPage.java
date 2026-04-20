package pages;

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

public class ExportersPage {

    private static final Logger log = LogManager.getLogger(ExportersPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // -- @FindBy Locators ----------------------------------------------------

    @FindBy(xpath = "//h2[@class='text-center font-normal text-gray-800 mb-6 " +
                    "text-[20px] md:text-[28px] leading-[28px]']")
    private WebElement sectionHeading;

    @FindBy(css = "div.px-4.py-8.bg-gray-50")
    private WebElement topCategoriesContainer;

    private static final String CATEGORY_XPATH =
            "//a[contains(@class,'text-base') and contains(@class,'font-semibold')]";

    // -- Constructor ----------------------------------------------------

    public ExportersPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js     = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    // -- Actions --------------------------------------------------------

    public void waitForPage() {
        wait.until(ExpectedConditions.visibilityOf(sectionHeading));
        log.info("Exporters page loaded.");
    }

    public String getSectionTitle() {
        return sectionHeading.getText().trim();
    }

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

    public void scrollToTopCategories() {
        wait.until(ExpectedConditions.visibilityOf(topCategoriesContainer));
        js.executeScript("arguments[0].scrollIntoView(true)", topCategoriesContainer);
        log.info("Scrolled to top categories section.");
    }
}
