package tests.authpagetests;


import base.TestUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class NegativeAuthTests extends TestUtilities {

    @Parameters({ "username", "password", "expectedMessage" })
    @Test(priority = 1)
    public void negativeTest(String username, String password, String expectedErrorMessage) {
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
