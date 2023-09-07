package tests.authpagetests;


import base.TestUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.AuthPageObject;

import java.time.Duration;

public class PositiveAuthTests extends TestUtilities {
    @Test
    public void positiveAuthTest() {
        log.info("Starting positiveTest");

        // open main page
        AuthPageObject authPage = new AuthPageObject(driver, log);
        authPage.openPage();

        // enter username and password
        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // push log in button
        WebElement logInButtonLocator = driver.findElement(By.id("login-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(logInButtonLocator));
        logInButtonLocator.click();
        takeScreenshot("Login button pushed");
    }
}
