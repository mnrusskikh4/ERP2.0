package pages;


import com.github.javafaker.Faker;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class OrderDataPage extends BasePageObject {

    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    private final By sendButtonLocator = By.id("send-btn");

    private By photoTabLocator = By.xpath("(//div[contains(@class, 'v-list-item__content')])[3]");

    private By multiPhotoLocator = By.cssSelector(".v-input--selection-controls__ripple");

    private By surnameLocator = By.id("fe-0-0");
    private By nameLocator = By.id("fe-0-1");
    private By middlenameLocator = By.id("fe-0-2");
    private By birthdateLocator = By.xpath("(//div[contains(@class, 'v-text-field__slot')])[4]");
    private By phoneLocator = By.id("fe-0-140");
    private By emailLocator = By.id("fe-0-141");
    public By manradioLocator = By.xpath("(//*[contains(@class, 'v-input--selection-controls__input')])[1]");
    public By womanradioLocator = By.xpath("(//*[contains(@class, 'v-input--selection-controls__input')])[2]");

    public OrderDataPage(WebDriver driver, Logger log) {
        super(driver, log);
    }

    public void waitForOrderPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sendButtonLocator));
        System.out.println("Страница Оформление заказа загружена");
    }

    public void fillForm() {

        // Создание экземпляра Faker с русской локализацией
        Faker faker = new Faker(new Locale("ru"));

        // Генерация данных имя, фамилия, e-mail, мобильный телефон, дата рождения
        String surnameGenerated = faker.name().lastName();
        WebElement surnameField = wait.until(ExpectedConditions.visibilityOfElementLocated(surnameLocator));
        surnameField.sendKeys(surnameGenerated);

        String firstnameGenerated = faker.name().firstName();
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(nameLocator));
        nameField.sendKeys(firstnameGenerated);

        Faker emailFaker = new Faker(Locale.ENGLISH);
        String emailGenerated = emailFaker.internet().emailAddress();
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(emailLocator));
        emailField.sendKeys(emailGenerated);

        Faker ruFaker = new Faker(new Locale("ru"));
        String ruPhoneNumber = ruFaker.phoneNumber().phoneNumber();
        WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneLocator));
        phoneField.sendKeys(ruPhoneNumber);

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String birthdateGenerated = faker.date().birthday().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
        WebElement birthdateField = wait.until(ExpectedConditions.visibilityOfElementLocated(birthdateLocator));
        wait.until(ExpectedConditions.elementToBeClickable(birthdateLocator));
        jsExecutor.executeScript("arguments[0].value=arguments[1];", birthdateField, birthdateGenerated);
        String valueInField = birthdateField.getAttribute("value");
        System.out.println("Значение введенной даты рождения: " + valueInField);

        String middleName = "Автотест";
        WebElement middlenameField = wait.until(ExpectedConditions.visibilityOfElementLocated(middlenameLocator));
        middlenameField.sendKeys(middleName);

        // Рандомно выбираем пол и кликаем по соответствующей радиокнопке
        Random random = new Random();
        if (random.nextBoolean()) {
            clickOnManRadio();
        } else {
            clickOnWomanRadio();
        }

    }

    public void clickOnManRadio() {
        if (driver == null) {
            log.error("WebDriver не проинициализирован");
            throw new IllegalStateException("WebDriver не проинициализирован");
        }
        try {
            WebElement element = driver.findElement(manradioLocator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            log.error("Не удалось нажать на manradioLocator: " + e.getMessage());
        }
    }

    public void clickOnWomanRadio() {
        if (driver == null) {
            log.error("WebDriver не проинициализирован");
            throw new IllegalStateException("WebDriver не проинициализирован");
        }
        try {
            WebElement element = driver.findElement(womanradioLocator);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            log.error("Не удалось нажать на womanradioLocator: " + e.getMessage());
        }
    }

    public void openAndClickToMultiPhoto() {
        WebElement photoTab = wait.until(ExpectedConditions.elementToBeClickable(photoTabLocator));
        photoTab.click();

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement multiPhoto = wait.until(ExpectedConditions.visibilityOfElementLocated(multiPhotoLocator));
        wait.until(ExpectedConditions.elementToBeClickable(multiPhotoLocator));
        jsExecutor.executeScript("arguments[0].click();", multiPhoto);

    }
}


