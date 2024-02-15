package tests.authpagetests;

import base.BaseTest;
import base.TestUtilities;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AuthPageObject;
import pages.DoctorsAccountPage;
import pages.OrderDataPage;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SmokeE2ETests extends TestUtilities {

    private WireMockServer wireMockServer;

    String expectedPartOfUrl = "https://doc.star-smile.ru/#/new/";

    @BeforeClass
    public void setWireMockServer() {
        // Инициализация WireMockServer без указания порта
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        // Настройка WireMock
        configureFor("localhost", wireMockServer.port()); // Используем автоматически назначенный порт
        stubFor(get(urlEqualTo("https://openapi_pp2.simplyceph.com/OrderFiles/279802/4271698_pre.png"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("C:\\Users\\admin\\IdeaProjects\\ERP2.0\\src\\test\\resources\\mockfiles\\1Maxillary.stl")));
    }

    @Test
    public void SmokeE2ETest() {
        AuthPageObject authPage = new AuthPageObject(BaseTest.getDriver(), log);
        authPage.openPage();

        enterLoginAndPass();

        clickLoginButton();

        DoctorsAccountPage doctorsAccountPage = new DoctorsAccountPage(BaseTest.getDriver(), log);
        doctorsAccountPage.waitForPageToLoad();

        OrderDataPage orderDataPage = doctorsAccountPage.clickCreateOrder();

        selectProductFromDropdown();

        orderDataPage.waitForPageToLoad();

        checkUrlContains(expectedPartOfUrl);

    }

    @Step("Внесение валидных данных логин и пароль")
    public void enterLoginAndPass() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        usernameElement.sendKeys("DoctorStar2023");
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        passwordElement.sendKeys("1");
    }

    @Step("Переход в кабинет доктора по кнопке логин")
    public void clickLoginButton() {
        WebElement logInButtonLocator = BaseTest.getDriver().findElement(By.id("login-btn"));
        logInButtonLocator.click();
        takeScreenshot("Login button pushed");
    }

    @Step("Проверка нахождения на странице Данные Пациента")
    public void checkUrlContains(String expectedPartOfUrl) {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        boolean urlContainsExpectedPart = wait.until(ExpectedConditions.urlContains(expectedPartOfUrl));
        Assert.assertTrue(urlContainsExpectedPart, "Текущий URL (" + BaseTest.getDriver().getCurrentUrl() + ") не содержит ожидаемую часть: " + expectedPartOfUrl);
        takeScreenshot("Корректный переход на страницу Данные Пациента");
    }

    @Step("Выбор продукта из выпадающего списка")
    public void selectProductFromDropdown() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) BaseTest.getDriver();

        List<WebElement> products = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.v-menu__content")));

        if (!products.isEmpty()) {
            Random random = new Random();
            int randomIndex;
            do {
                randomIndex = random.nextInt(products.size());
            } while (randomIndex == 5); // Исключение Виртуального сетапа

            WebElement randomProduct = products.get(randomIndex);
            js.executeScript("arguments[0].click();", randomProduct);
        }
    }

    @AfterClass
    public void teardownWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
