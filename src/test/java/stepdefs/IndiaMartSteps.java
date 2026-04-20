package stepdefs;

import utils.ConfigReader;
import utils.DriverManager;
import utils.ExcelWriter;
import utils.ScreenshotUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IndiaMartSteps {

    private static final Logger log = LogManager.getLogger(IndiaMartSteps.class);

    private WebDriver        driver;
    private final ConfigReader config = ConfigReader.getInstance();

    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private ServiceDetailPage serviceDetailPage;
    private LoginModal loginModal;
    private ExportersPage exportersPage;

    // TC01 – intermediate data shared between steps
    private List<String> serviceNames  = new ArrayList<>();
    private List<String> servicePhones = new ArrayList<>();
    private List<String> serviceRatings = new ArrayList<>();

    // ------------------------------------------------------------------------
    //  BACKGROUND
    // ------------------------------------------------------------------------

    @Given("I open the IndiaMart website")
    public void openIndiaMart() {
        driver   = DriverManager.getDriver();          // get from ThreadLocal
        driver.get(config.get("baseUrl"));
        homePage = new HomePage(driver);
        log.info("Opened IndiaMart: {}", config.get("baseUrl"));
    }

    // ------------------------------------------------------------------------
    //  TC01 – Top 3 Car Wash Services
    // ------------------------------------------------------------------------

    @When("I set the location to the configured city")
    public void setCity() {
        homePage.setCity(config.get("city"));
    }

    @When("I search for the configured keyword")
    public void search() {
        homePage.searchFor(config.get("searchKeyword"));
        searchResultsPage = new SearchResultsPage(driver);
        searchResultsPage.waitForResults();
    }

    @Then("the top 3 service names should be displayed")
    public void displayServiceNames() {
        // Collect names, URLs, and ratings
        serviceNames   = searchResultsPage.getTop3Names();
        List<String> urls = searchResultsPage.getTop3Urls();
        serviceRatings = searchResultsPage.getTop3Ratings();

        // -- Open each service URL in a new tab and extract phone number ---
        String mainWindow = driver.getWindowHandle();

        for (int i = 0; i < urls.size(); i++) {
            // Open detail page in a new browser tab
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("window.open(arguments[0], '_blank');", urls.get(i));

            // Switch to the newly opened tab
            Set<String> allWindows = driver.getWindowHandles();
            for (String win : allWindows) {
                if (!win.equals(mainWindow)) {
                    driver.switchTo().window(win);
                    break;
                }
            }

            // Use ServiceDetailPage to extract phone from the detail page
            serviceDetailPage = new ServiceDetailPage(driver);
            serviceDetailPage.closePopupIfPresent();
            String phone = serviceDetailPage.getPhoneNumber();
            servicePhones.add(phone);

            // Close this tab and go back to results
            driver.close();
            driver.switchTo().window(mainWindow);
        }

        // -- Console output -------------------------------------------------
        System.out.println();
        System.out.println("_______ INDIA MART – TOP 3 CAR WASH SERVICES ______");
        System.out.println("---------------------------------------------------");
        for (int i = 0; i < serviceNames.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, serviceNames.get(i));
            System.out.printf("     Phone  : %s%n", i < servicePhones.size()  ? servicePhones.get(i)  : "N/A");
            System.out.printf("     Rating : %s%n", i < serviceRatings.size() ? serviceRatings.get(i) : "N/A");
        }

        log.info("TC01: Collected {} services.", serviceNames.size());

        Assert.assertFalse(serviceNames.isEmpty(),
                "TC01 FAILED: No car wash services found on search results page.");
    }

    @And("the details should be saved to Excel")
    public void saveToExcel() {
        ExcelWriter.writeCarWashData(serviceNames, servicePhones, serviceRatings);
        log.info("TC01: Car wash data saved to Excel.");
    }

    // ------------------------------------------------------------------------
    //  TC02 – Food, Agriculture & Farming Section
    // ------------------------------------------------------------------------

    @When("I scroll to the Food Agriculture and Farming section")
    public void scrollToFoodSection() {
        homePage.scrollToFoodAgriSection();
        homePage.closePopupIfPresent();
    }

    @Then("all product names in that section should be displayed")
    public void displayFoodProducts() {
        List<String> products = homePage.getFoodAgriProductNames();

        System.out.println();
        System.out.println("_______ Food, Agriculture & Farming _______");
        System.out.println("-----------------------------------------");
        products.forEach(p -> System.out.println("  * " + p));

        Assert.assertFalse(products.isEmpty(),
                "TC02 FAILED: No products found in Food, Agriculture & Farming. " +
                "Check XPath in HomePage.getFoodAgriProductNames().");
    }

    // ------------------------------------------------------------------------
    //  TC03 – Invalid Phone Number
    // ------------------------------------------------------------------------

    @When("I open the sign in dialog")
    public void openSignIn() {
        homePage.clickSignIn();
        loginModal = new LoginModal(driver);
    }

    @When("I enter the invalid phone number from config")
    public void enterInvalidPhone() {
        loginModal.enterPhoneNumber(config.get("invalidPhoneNumber"));
    }

    @When("I submit the phone number")
    public void submitPhone() {
        loginModal.submitPhone();
    }

    @Then("a validation error message should be displayed")
    public void verifyError() {
        String error = loginModal.getErrorMessage();

        System.out.println();
        System.out.println("_____ INVALID PHONE NUMBER TEST _____");
        System.out.println("---------------------------------------");
        System.out.printf("  Phone entered : %s%n", config.get("invalidPhoneNumber"));
        System.out.printf("  Error message : %s%n", error);

        Assert.assertFalse(error.isEmpty(),
                "TC03 FAILED: Expected validation error for invalid phone but none appeared.");
    }

    @Then("a screenshot of the error should be saved")
    public void screenshotError() {
        ScreenshotUtil.take(driver, "invalid_number_error");
        loginModal.closeModal();
    }

    // ------------------------------------------------------------------------
    //  TC04 – Exporters – Top Product Categories
    // ------------------------------------------------------------------------

    @When("I navigate to the Exporters section")
    public void goToExporters() {
        homePage.clickExporters();
        exportersPage = new ExportersPage(driver);
        exportersPage.waitForPage();
    }

    @Then("the top product categories from India should be displayed")
    public void displayTopCategories() {
        String title            = exportersPage.getSectionTitle();
        List<String> categories = exportersPage.getTopProductCategories();

        System.out.println();
        System.out.println("_____ " + title + " ______");
        System.out.println("---------------------------------------------------");
        categories.forEach(c -> System.out.println("  * " + c));

        Assert.assertFalse(categories.isEmpty(),
                "TC04 FAILED: No top product categories found. " +
                "Check XPath in ExportersPage.getTopProductCategories().");
    }

    @And("a screenshot of the categories should be saved")
    public void screenshotCategories() {
        exportersPage.scrollToTopCategories();
        ScreenshotUtil.take(driver, "top_product_categories_india");
    }
}
