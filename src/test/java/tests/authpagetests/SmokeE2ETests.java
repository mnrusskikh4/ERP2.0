package tests.authpagetests;

import base.BaseTest;
import base.TestUtilities;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AuthPageObject;
import pages.DoctorsAccountPage;

import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SmokeE2ETests extends TestUtilities {

    private WireMockServer wireMockServer;

    @BeforeClass
    public void setWireMockServer() {
        // Инициализация WireMockServer без указания порта
        wireMockServer = new WireMockServer();
        wireMockServer.start();

        // Настройка WireMock
        configureFor("localhost", wireMockServer.port()); // Используем автоматически назначенный порт
        stubFor(get(urlEqualTo("https://openapi_pp2.simplyceph.com/OrderFiles/279802/4271698_pre.png")) // или api/order/saveOrder или api/order/update-3ds-data
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("C:\\Users\\admin\\IdeaProjects\\ERP2.0\\src\\test\\resources\\mockfiles\\1Maxillary.stl")));
    }

    @Test
    public void SmokeE2ETest() {
        // open main page
        AuthPageObject authPage = new AuthPageObject(BaseTest.getDriver(), log);
        authPage.openPage();

        enterLoginAndPass();

        clickLoginButton();

        // Переход на новую страницу и ожидание ее загрузки
        DoctorsAccountPage doctorsAccountPage = new DoctorsAccountPage(BaseTest.getDriver(), log);
        doctorsAccountPage.waitForPageToLoad();

        // Обнаружение и нажатие кнопки "Создать заказ"

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

    @AfterClass
    public void teardownWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}