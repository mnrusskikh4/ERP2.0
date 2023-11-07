package tests.authpagetests;

import base.BaseTest;
import base.TestUtilities;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
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
    @Description("Тест подключенных уведомлений по-умолчанию")
    public void positiveAuthTest() {
        log.info("Starting positiveTest");

        // open main page
        AuthPageObject authPage = new AuthPageObject(BaseTest.getDriver(), log);
        authPage.openPage();

        enterLoginAndPass();

        clickLoginButton();

        // Переход на новую страницу и ожидание ее загрузки
        DoctorsAccountPage doctorsAccountPage = new DoctorsAccountPage(BaseTest.getDriver(), log);
        doctorsAccountPage.waitForPageToLoad();

        checkExpandMoreIcon(doctorsAccountPage);

        // Добавляем ожидание перед созданием скриншота
        safeSleep(2000);

        takeScreenshot("Expand More icon is present on the page");

        DoctorsProfilePage doctorsProfilePage = doctorsAccountPage.clickExpandMoreAndSelectProfile();
        checkCheckboxes(doctorsProfilePage);
    }

    @Step("Внесение валидных данных логин и пароль")
    public void enterLoginAndPass() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));

        usernameInput.sendKeys("awsavichev@gmail.com");
        passwordInput.sendKeys("k@O23");
    }

    @Step("Переход в кабинет доктора по кнопке логин")
    public void clickLoginButton() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement logInButtonLocator = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        logInButtonLocator.click();
        takeScreenshot("Login button pushed");
    }

    @Step("Обнаружение выпадающей кнопки Профиль/Выйти")
    public void checkExpandMoreIcon(DoctorsAccountPage doctorsAccountPage) {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement expandMoreIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ml-5")));
        boolean isIconPresent = expandMoreIcon.isDisplayed();
        Assert.assertTrue(isIconPresent, "Expand More icon is not present on the page");
    }

    @Step("Проверка состояния чекбоксов")
    public void checkCheckboxes(DoctorsProfilePage doctorsProfilePage) {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(doctorsProfilePage.getFirstCheckboxElement()));

        Assert.assertTrue(doctorsProfilePage.areAllCheckboxesSelected(), "Checkboxes are not selected");

        // Скролл до элемента
        scrollToElement(doctorsProfilePage.getFirstCheckboxElement());

        // Добавляем ожидание перед созданием скриншота
        safeSleep(2000);

        takeScreenshot("checkboxesState");
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) BaseTest.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();  // или другая ваша обработка
        }
    }
}
