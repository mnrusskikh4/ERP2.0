package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class BasePageObject {

    protected WebDriver driver;
    protected Logger log;

    public BasePageObject(WebDriver driver, Logger log) {
        this.driver = driver;
        this.log = log;
    }

    /** Open page with given URL */
    protected void openUrl(String url) {
        driver.get(url);
    }

    /** Find element using given locator */
    protected WebElement find(By locator) {
        return driver.findElement(locator);
    }

    /**
     * Click on element with given locator when it's visible
     * @param locator The locator of the element
     */
    protected void click(By locator) {
        waitForVisibilityOf(locator, 5);
        find(locator).click();
    }

    /**
     * Type given text into element with given locator
     * @param text The text to type
     * @param locator The locator of the element
     */
    protected void type(String text, By locator) {
        waitForVisibilityOf(locator, 5);
        find(locator).sendKeys(text);
    }

    /** Get URL of current page from browser */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Wait for specific ExpectedCondition for the given amount of time in seconds
     * @param condition The expected condition to wait for
     * @param timeOutInSeconds The timeout in seconds
     */
    private void waitFor(ExpectedCondition<WebElement> condition, Integer timeOutInSeconds) {
        Duration timeout = Duration.ofSeconds(timeOutInSeconds != null ? timeOutInSeconds : 30);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(condition);
    }

    /**
     * Wait for given number of seconds for element with given locator to be visible
     * on the page
     * @param locator The locator of the element
     * @param timeOutInSeconds The timeout in seconds
     */
    private void waitForVisibilityOf(By locator, Integer... timeOutInSeconds) {
        int timeout = (timeOutInSeconds.length > 0 && timeOutInSeconds[0] != null) ? timeOutInSeconds[0] : 30;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
