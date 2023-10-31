package tests.authpagetests;

import base.BaseTest;
import base.TestUtilities;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
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

        checkForErrorMessageOnAuthPage();

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

        // Ожидание, пока элемент с id="username" станет видимым
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameInput.sendKeys("awsavichev@gmail.com");

        // Ожидание, пока элемент с id="password" станет видимым
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordInput.sendKeys("k@O2");
    }


    @Step("Переход в кабинет доктора по кнопке логин")
    public void clickLoginButton() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement logInButtonLocator = BaseTest.getDriver().findElement(By.id("login-btn"));
        wait.until(ExpectedConditions.elementToBeClickable(logInButtonLocator));
        logInButtonLocator.click();
        takeScreenshot("Login button pushed");
    }

    @Step("Обнаружение выпадающей кнопки Профиль/Выйти")
    public void checkExpandMoreIcon(DoctorsAccountPage doctorsAccountPage) {
        boolean isIconPresent = doctorsAccountPage.isExpandMoreIconPresent();
        Assert.assertTrue(isIconPresent, "Expand More icon is not present on the page");
    }
    @Step("Проверка состояния чекбоксов")
    public void checkCheckboxes(DoctorsProfilePage doctorsProfilePage) {

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
    @Step("Проверка сообщения об ошибке на странице AuthPageObject")
    public void checkForErrorMessageOnAuthPage() {
        By errorMessageLocator = By.xpath("//div[@role='status'][contains(@class, 'v-snack__content')][contains(text(), 'Неверный логин или пароль')]");
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(1));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
            String errorMessage = BaseTest.getDriver().findElement(errorMessageLocator).getText();
            Assert.fail("Ошибка авторизации: " + errorMessage);  // Тест завершится неудачей
        } catch (TimeoutException e) {
            // Если сообщение об ошибке не появляется, то считаем, что авторизация прошла успешно.
        }
    }

    private void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();  // или другая ваша обработка
        }
    }
}
