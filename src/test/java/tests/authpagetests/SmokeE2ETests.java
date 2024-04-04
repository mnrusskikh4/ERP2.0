package tests.authpagetests;

import base.BaseTest;
import base.Kandinsky;
import base.TestUtilities;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AuthPageObject;
import pages.DoctorsAccountPage;
import pages.OrderDataPage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SmokeE2ETests extends TestUtilities {

    private WireMockServer wireMockServer;
    private OrderDataPage orderDataPage;
    private AuthPageObject authPage;
    private DoctorsAccountPage doctorsAccountPage;
    private Kandinsky api;

    public static final String URL = "https://api-key.fusionbrain.ai/";
    public static final String API_KEY = "B0ACDF1FD75E32D32D471EA21260FAFC";
    public static final String SECRET_KEY = "1A9B248B3EA51441B825F390840599D8";

    Kandinsky api = new Kandinsky(URL, API_KEY, SECRET_KEY);
    String expectedPartOfUrl = "https://doc.star-smile.ru/#/new/";

    @BeforeClass
    api = new Kandinsky(URL, API_KEY, SECRET_KEY);
    public void setWireMockServer() {
        // Инициализация и настройка WireMockServer
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port()); // Используем автоматически назначенный порт

        // Настройка ответа от WireMock
        stubFor(get(urlEqualTo("https://openapi_pp2.simplyceph.com/OrderFiles/279802/4271698_pre.png"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("mockfiles/1Maxillary.stl")));
    }

    @Test
    public void SmokeE2ETest() {

        authPage = new AuthPageObject(getDriver(), log);
        authPage.openPage();
        authPage.waitForLoginElementsToBeVisible();

        enterLoginAndPass();

        clickLoginButton();

        doctorsAccountPage = new DoctorsAccountPage(getDriver(), log);
        doctorsAccountPage.waitForDocAccPageToLoad();

        doctorsAccountPage.clickCreateOrder();

        selectProductFromDropdown();

        checkUrlContains(expectedPartOfUrl);

        orderDataPage = new OrderDataPage(getDriver(), log);
        orderDataPage.waitForOrderPageToLoad();

        orderDataPage.fillForm();
        takeScreenshot("fillFormDone");

        orderDataPage.openAndClickToMultiPhoto();
        takeScreenshot("multiPhotoOn");

        fillTheFormWithRandomGenderAndGenerateImage();

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


    @Step("Генерация фото пациента, в зависимости от выбранного пола")
    public void fillTheFormWithRandomGenderAndGenerateImage() {
        try {
            String malePrompt = "Цветное изображение мужчины случайной расы, " +
                    "выбери строго одну из: негроидная или европеоидная или монголоидная или американоидная или австралоидная, " +
                    "с красивой белоснежной улыбкой";
            String femalePrompt = "Цветное изображение женщины случайной расы, " +
                    "выбери строго одну из: негроидная или европеоидная или монголоидная или американоидная или австралоидная, " +
                    "с красивой белоснежной улыбкой";

            Random random = new Random();
            boolean isMale = random.nextBoolean();

            if (isMale) {
                orderDataPage.clickOnManRadio();
            } else {
                orderDataPage.clickOnWomanRadio();
            }

            String selectedPrompt = isMale ? malePrompt : femalePrompt;
            String modelId = api.get_model();  // Убраны параметры, используем поля класса
            String generatedImageUuid = api.generate(selectedPrompt, modelId);  // Убраны параметры, используем поля класса
            JsonNode images = null;
            if (generatedImageUuid != null) {
                images = api.check_generation(generatedImageUuid);  // Убраны параметры, используем поля класса
            }

            if (images != null && images.has(0)) {
                String imageUrl = images.get(0).asText();

                // Генерация уникального имени файла для сохранения изображения
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String uniqueFileName = "image_" + LocalDateTime.now().format(dtf) + ".jpg";
                Path pathToSave = Paths.get("C:", "Users", "Miha", "IdeaProjects", "ERP2.0", "src", "test", "resources", "avatars", uniqueFileName);

                // Скачивание изображения
                Path downloadedImagePath = api.downloadImage(imageUrl, pathToSave.toString());

                By inputFileLocator = By.xpath("(//*[@id='files'])[1]");

                WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(10));
                WebElement fileInputElement = wait.until(ExpectedConditions.elementToBeClickable(inputFileLocator));
                fileInputElement.sendKeys(downloadedImagePath.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @AfterClass(alwaysRun = true)
    public void teardownWireMockServer() {
        if (wireMockServer != null) {
            try {
                wireMockServer.stop();
                System.out.println("WireMockServer был успешно остановлен");
            } catch (Exception e) {
                System.err.println("Произошла ошибка при попытке остановить WireMockServer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
