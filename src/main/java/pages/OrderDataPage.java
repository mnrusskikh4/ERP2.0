package pages;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;
import java.util.Random;

public class OrderDataPage extends BasePageObject {

    private final By sendButtonLocator = By.id("send-btn");
    private By surnameLocator = By.id("fe-0-0");
    private By nameLocator = By.id("fe-0-1");
    private By middlenameLocator = By.id("fe-0-2");
    private By birthdateLocator = By.id("input-790");
    private By ageLocator = By.id("input-791");
    private By phoneLocator = By.id("fe-0-140");
    private By emailLocator = By.id("fe-0-141");
    private By manradioLocator = By.id("fei-0-3-0");
    private By womanradioLocator = By.id("fei-0-3-1");
    public OrderDataPage(WebDriver driver, Logger log) {
        super(driver, log);
    }

    public void waitForOrderPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sendButtonLocator));
        System.out.println("Страница Оформление заказа загружена");
    }

    public void fillForm(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Создание экземпляра Faker с русской локализацией
        Faker faker = new Faker(new Locale("ru"));

        // Генерация данных имя, фамилия, e-mail, мобильный телефон, дата рождения
        String surnameGenerated = faker.name().lastName();
        WebElement surnameLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fe-0-0")));
        surnameLocator.sendKeys(surnameGenerated);

        String firstnameGenerated = faker.name().firstName();
        WebElement nameLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fe-0-1")));
        nameLocator.sendKeys(firstnameGenerated);

        String emailGenerated = faker.internet().emailAddress();
        WebElement emailLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fe-0-141")));
        emailLocator.sendKeys(emailGenerated);

        String phoneGenerated = faker.phoneNumber().cellPhone();
        WebElement phoneLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fe-0-140")));
        phoneLocator.sendKeys(phoneGenerated);

        String birthdateGenerated = faker.date().birthday().toString();
        WebElement birthdateLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("input-790")));
        birthdateLocator.sendKeys(birthdateGenerated);

        String middleName = "Автотест";
        WebElement middlenameLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fe-0-2")));
        middlenameLocator.sendKeys(middleName);

        WebElement manradioLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fei-0-3-0")));
        WebElement womanradioLocator = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fei-0-3-1")));

        // Рандомно выбираем одну из радиокнопок и кликаем по ней
        WebElement selectedGender = new Random().nextBoolean() ? manradioLocator : womanradioLocator;
        selectedGender.click();

    }

}


