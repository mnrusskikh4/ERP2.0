package tests.authpagetests;

import base.BaseTest;
import base.TestUtilities;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        authPage.waitForLoginElementsToBeVisible();

        enterLoginAndPass();

        clickLoginButton();

        DoctorsAccountPage doctorsAccountPage = new DoctorsAccountPage(BaseTest.getDriver(), log);
        doctorsAccountPage.waitForDocAccPageToLoad();

        doctorsAccountPage.clickCreateOrder();

        selectProductFromDropdown();

        checkUrlContains(expectedPartOfUrl);

        OrderDataPage orderDataPage = new OrderDataPage(BaseTest.getDriver(), log);
        orderDataPage.waitForOrderPageToLoad();

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

    @Step("Проверка нахождения на странице Данные Пациента и повторение выбора продукта при необходимости")
    public void checkUrlContains(String expectedPartOfUrl) {
        // Ожидаем, когда URL будет содержать указанную часть
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.urlContains(expectedPartOfUrl));

        // Получаем текущий URL и проверяем, содержит ли он ожидаемую часть
        String currentUrl = BaseTest.getDriver().getCurrentUrl();
        System.out.println("Текущий URL: " + currentUrl);
        if (currentUrl.contains(expectedPartOfUrl)) {
            System.out.println("Мы находимся на странице оформления заказа.");
        } else {
            System.out.println("URL не соответствует ожидаемой странице оформления заказа.");
        }
        takeScreenshot("Корректный переход на страницу Данные Пациента");
    }

    @Step("Выбор продукта из выпадающего списка")
    public void selectProductFromDropdown() {
        WebDriverWait wait = new WebDriverWait(BaseTest.getDriver(), Duration.ofSeconds(10));
        By productsTitlesLocator = By.cssSelector(".v-menu__content.menuable__content__active .v-list-item__title");

        // Ожидаем, пока выпадающий список полностью загрузится и станет видимым
        wait.until(ExpectedConditions.visibilityOfElementLocated(productsTitlesLocator));

        // Находим все элементы списка продуктов
        List<WebElement> productTitles = BaseTest.getDriver().findElements(productsTitlesLocator);

        // Убедимся, что список продуктов не пустой
        if (productTitles.isEmpty()) {
            throw new NoSuchElementException("Нет продуктов в выпадающем списке.");
        }

        // Генерируем случайный индекс
        int randomIndex = new Random().nextInt(productTitles.size());

        // Получаем случайно выбранный элемент
        WebElement selectedProductTitle = productTitles.get(randomIndex);

        // Получаем текст элемента до клика
        String selectedItemText = selectedProductTitle.getText();

        // Кликаем по элементу
        wait.until(ExpectedConditions.elementToBeClickable(selectedProductTitle)).click();

        // Вывод информации о выбранном продукте
        System.out.println("Выбранный продукт: " + selectedItemText);
    }


    @Step("Генерация фото пациента, взависимости от выбранного пола")
    public void fillTheFormWithRandomGenderAndGenerateImage() throws Exception {
        Kandinsky api = new Kandinsky(URL, API_KEY, SECRET_KEY);

        String malePrompt = "Портрет очаровательного ...";
        String femalePrompt = "Цветное изображение женщины случайной расы, " +
                "выбери строго одну из: негроидная или европеоидная или монголоидная или американоидная или австралоидная или смешанная, " +
                "с красивой белоснежной улыбкой";

        Random random = new Random();
        boolean isMale = random.nextBoolean();
        String selectedPrompt = isMale ? malePrompt : femalePrompt;
        WebElement selectedGender = isMale ? manradioLocator : womanradioLocator;
        selectedGender.click(); // Выбор радиокнопки на UI веб-приложения

        // Использование API для генерации изображения
        String model_id = api.get_model();
        String generatedImageUuid = api.generate(selectedPrompt, model_id);

        // Здесь может быть логика ожидания завершения генерации и последующая загрузка изображения
        // Пример:
        // JsonNode images = api.check_generation(generatedImageUuid);
        // Path downloadedImagePath = api.downloadImage(images.get(0).asText(), "path/to/save/image.jpg");
    }
    @AfterClass
    public void teardownWireMockServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
