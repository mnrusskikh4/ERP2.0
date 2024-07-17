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
        doctorsAccountPage.waitForDocAccPageToLoad();

        checkExpandMoreIcon(doctorsAccountPage);

        takeScreenshot("Expand More icon is present on the page");

        DoctorsProfilePage doctorsProfilePage = doctorsAccountPage.clickExpandMoreAndSelectProfile();

        WebElement firstCheckbox = doctorsProfilePage.getFirstCheckboxElement();
        scrollToElement(firstCheckbox);

        checkCheckboxes(doctorsProfilePage);
    }

    @Step("Внесение валидных данных логин и пароль")
    public void enterLoginAndPass() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameElement.sendKeys("***");
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordElement.sendKeys("***");
    }

    @Step("Переход в кабинет доктора по кнопке логин")
    public void clickLoginButton() {
        WebElement logInButtonLocator = BaseTest.getDriver().findElement(By.id("login-btn"));
        logInButtonLocator.click();
        takeScreenshot("Login button pushed");
    }

    @Step("Обнаружение выпадающей кнопки Профиль/Выйти")
    public void checkExpandMoreIcon(DoctorsAccountPage doctorsAccountPage) {
        boolean isIconPresent = doctorsAccountPage.isExpandMoreIconPresent();
        Assert.assertTrue(isIconPresent, "Expand More icon is not present on the page");
    }
    @Step("Скролл к элементу")
    public void scrollToElement(WebElement element) {
        // Прокрутка к элементу с использованием JavaScript
        ((JavascriptExecutor) BaseTest.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }
    @Step("Проверка состояния чекбоксов")
    public void checkCheckboxes(DoctorsProfilePage doctorsProfilePage) {
        Assert.assertTrue(doctorsProfilePage.areAllCheckboxesSelected(), "Checkboxes are not selected");
        takeScreenshot("checkboxesState");
    }
}
