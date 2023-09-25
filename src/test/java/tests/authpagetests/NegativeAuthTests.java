package tests.authpagetests;

import base.BaseTest;
import base.CsvDataProviders;
import base.TestUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AuthPageObject;

import java.time.Duration;
import java.util.Map;

public class NegativeAuthTests extends TestUtilities {

    @Test(priority = 1, dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class)
    public void negativeAuthTest(Map<String, String> testData) {
        log.info("Starting negativeTest");

        // Extracting data from CSV
        String no = testData.get("no");
        String username = testData.get("username");
        String password = testData.get("password");
        String expectedMessage = testData.get("expectedMessage");
        String description = testData.get("description");

        // Logging the extracted data
        log.info("Username from CSV: " + username);
        log.info("Password from CSV: " + password);
        log.info("Expected Message from CSV: " + expectedMessage);

        // Opening authentication page
        AuthPageObject authPage = new AuthPageObject(BaseTest.getDriver(), log);
        authPage.openPage();

        BaseTest.getDriver().findElement(By.id("username")).sendKeys(username);
        BaseTest.getDriver().findElement(By.id("password")).sendKeys(password);

        // Clicking on login button
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement loginButton = BaseTest.getDriver().findElement(By.id("login-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        takeScreenshot("Login button pushed");

        // Verification
        String authErrorMessage = authPage.getErrorMessageText();
        String expectedErrorMessage = "Неверный логин или пароль";
        Assert.assertTrue(authErrorMessage.contains(expectedErrorMessage),
                "authErrorMessage does not contain expectedErrorMessage\nexpectedErrorMessage: "
                        + expectedErrorMessage + "\nauthErrorMessage: " + authErrorMessage);


    }
}
