package tests.authpagetests;


import base.CsvDataProviders;
import base.TestUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.Map;

public class NegativeAuthTests extends TestUtilities {


    @Test(priority = 1, dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
    public void negativeAuthTest( Map<String, String> testData) {
        //Data
        String no = testData.get("no");
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedMessage = testData.get("expectedMessage");
        String description = testData.get("description");

        log.info("Starting negativeTest");

        // open main page
        String url = "https://doc.star-smile.ru/#/auth";
        driver.get(url);
        log.info("Main page is opened.");

        // Click on Form Authentication link
        driver.findElement(By.linkText("Form Authentication")).click();

        // enter username and password
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        // push log in button
        WebElement loginButton = driver.findElement(By.id("login-btn"));
        loginButton.click();

        // Add your assertion here for the expected error message
        // For example:
        // WebElement errorMessage = driver.findElement(By.className("error-message"));
        // Assert.assertEquals(errorMessage.getText(), expectedErrorMessage);
    }
}
