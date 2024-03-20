package tests.authpagetests;

import base.BaseTest;
import base.TestUtilities;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AuthPageObject;
import pages.DoctorsAccountPage;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SmokeE2ETests extends TestUtilities {

    private static final Random random = new Random();
    public void waitForPageLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")
        );
    }
    private WireMockServer wireMockServer;

    String expectedPartOfUrl = "https://doc.star-smile.ru/#/";

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
        waitForPageLoad(BaseTest.getDriver());

        enterLoginAndPass();

        clickLoginButton();

        DoctorsAccountPage doctorsAccountPage = new DoctorsAccountPage(BaseTest.getDriver(), log);
        doctorsAccountPage.waitForPageToLoad();

        doctorsAccountPage.clickCreateOrder();
        waitForPageLoad(BaseTest.getDriver());

        selectProductFromDropdown();
        waitForPageLoad(BaseTest.getDriver());


//        checkUrlContains(expectedPartOfUrl);

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
        JavascriptExecutor js = (JavascriptExecutor) BaseTest.getDriver();
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));

        // Убедитесь, что CSS-селектор выбирает именно элементы списка, а не сам список
        By productsTitlesLocator = By.cssSelector(".v-list-item__title");

        // Ожидаем появления элементов списка и получаем список элементов
        List<WebElement> productTitles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productsTitlesLocator));

        if (!productTitles.isEmpty()) {
            // Выбор случайного элемента из списка по названию
            int randomIndex = new Random().nextInt(productTitles.size());
            WebElement selectedProductTitle = productTitles.get(randomIndex);

            // Получение текста выбранного элемента
            String selectedItemText = selectedProductTitle.getText();

            // Вывод текста выбранного элемента в консоль
            System.out.println("Выбранный продукт: " + selectedItemText);

            // Ожидаем, что выбранный элемент станет кликабельным
            wait.until(ExpectedConditions.elementToBeClickable(selectedProductTitle));

            // Используем JavascriptExecutor для клика по выбранному элементу
            js.executeScript("arguments[0].click();", selectedProductTitle);

            // Вместо Thread.sleep лучше использовать явное ожидание, чтобы подтвердить какое-либо условие после клика
            // Например, ожидание, что выпадающий список закрылся
            // wait.until(ExpectedConditions.attributeContains(селектор-элемента-списка, "class", "класс-закрытого-списка"));

        } else {
            throw new IllegalStateException("Выбран пустой список или элементы не доступны.");
        }
    }

    @AfterClass
    public void teardownWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
