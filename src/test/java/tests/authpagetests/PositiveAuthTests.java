package tests.authpagetests;

import base.TestUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AuthPageObject;
import pages.DoctorsAccountPage;
import pages.DoctorsProfilePage;

import java.time.Duration;

public class PositiveAuthTests extends TestUtilities {

    @Test
    public void positiveAuthTest() {
        log.info("Starting positiveTest");

        // open main page
        AuthPageObject authPage = new AuthPageObject(driver, log);
        authPage.openPage();

        // enter username and password
        driver.findElement(By.id("username")).sendKeys("awsavichev@gmail.com");
        driver.findElement(By.id("password")).sendKeys("k@O23");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // push log in button
        WebElement logInButtonLocator = driver.findElement(By.id("login-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(logInButtonLocator));
        logInButtonLocator.click();
        takeScreenshot("Login button pushed");

        // Переход на новую страницу и ожидание ее загрузки
        DoctorsAccountPage doctorsAccountPage = new DoctorsAccountPage(driver, log);
        doctorsAccountPage.waitForPageToLoad();

        // Проверка выпадающей иконки
        boolean isIconPresent = doctorsAccountPage.isExpandMoreIconPresent();
        Assert.assertTrue(isIconPresent, "Expand More icon is not present on the page");

        // Добавляем ожидание перед созданием скриншота
        safeSleep(2000);

        takeScreenshot("Expand More icon is present on the page");

        DoctorsProfilePage doctorsProfilePage = doctorsAccountPage.clickExpandMoreAndSelectProfile();
        checkCheckboxes(doctorsProfilePage);
    }

    public void checkCheckboxes(DoctorsProfilePage doctorsProfilePage) {
        // Проверка состояния чекбоксов
        Assert.assertTrue(doctorsProfilePage.areAllCheckboxesSelected(), "Checkboxes are not selected");

        // Скролл до элемента
        scrollToElement(doctorsProfilePage.getFirstCheckboxElement());

        // Добавляем ожидание перед созданием скриншота
        safeSleep(2000);

        takeScreenshot("checkboxesState");
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();  // или другая ваша обработка
        }
    }
}
