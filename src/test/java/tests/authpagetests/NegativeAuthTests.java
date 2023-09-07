package tests.authpagetests;


import base.CsvDataProviders;
import base.TestUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.AuthPageObject;


import java.time.Duration;
import java.util.Map;

public class NegativeAuthTests extends TestUtilities {


    @Test(priority = 1, dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
    public void negativeAuthTest( Map<String, String> testData) {
        log.info("Starting negativeTest");
        //Data
        String no = testData.get("no");
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedMessage = testData.get("expectedMessage");
        String description = testData.get("description");

        // open main page
        AuthPageObject authPage = new AuthPageObject(driver, log);
        authPage.openPage();

        // enter username and password
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // push log in button
        WebElement loginButton = driver.findElement(By.id("login-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        takeScreenshot("Login button pushed");


        // WebElement errorMessage = driver.findElement(By.className("error-message"));
        // Assert.assertEquals(errorMessage.getText(), expectedErrorMessage);
    }
}
