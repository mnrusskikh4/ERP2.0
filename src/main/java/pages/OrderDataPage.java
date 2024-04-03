package pages;

import base.Kandinsky;
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

    private WebDriverWait wait;

    private final By sendButtonLocator = By.id("send-btn");

    private By photoTabLocator = By.xpath("(//div[contains(@class, 'v-list-item__content')])[3]");

    private By multiPhotoLocator = By.xpath("//input[@id='multiplePhoto' and @type='checkbox' and @aria-checked='false' and @role='switch']");

    private By surnameLocator = By.id("fe-0-0");
    private By nameLocator = By.id("fe-0-1");
    private By middlenameLocator = By.id("fe-0-2");
    private By birthdateLocator = By.xpath("(//div[contains(@class, 'v-text-field__slot')])[4]");
    private By phoneLocator = By.id("fe-0-140");
    private By emailLocator = By.id("fe-0-141");
    public By manradioLocator = By.id("fei-0-3-0");
    public By womanradioLocator = By.id("fei-0-3-1");
    public static final String URL = "https://api-key.fusionbrain.ai/";
    public static final String API_KEY = "B0ACDF1FD75E32D32D471EA21260FAFC";
    public static final String SECRET_KEY = "1A9B248B3EA51441B825F390840599D8";

    Kandinsky api = new Kandinsky(URL, API_KEY, SECRET_KEY);

    public OrderDataPage(WebDriver driver, Logger log) {
        super(driver, log);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
        WebElement element = driver.findElement(manradioLocator);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void clickOnWomanRadio() {
        WebElement element = driver.findElement(womanradioLocator);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void openAndClickToMultiPhoto() {
        WebElement photoTab = wait.until(ExpectedConditions.elementToBeClickable(photoTabLocator));
        photoTab.click();

        WebElement multiPhoto = wait.until(ExpectedConditions.elementToBeClickable(multiPhotoLocator));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", multiPhoto);
    }
}


