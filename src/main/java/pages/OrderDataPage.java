package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderDataPage extends BasePageObject {

    private final By sendButtonLocator = By.id("send-btn");

    public OrderDataPage(WebDriver driver, Logger log) {
        super(driver, log);
    }

    public void waitForOrderPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(sendButtonLocator));
        System.out.println("Страница Оформление заказа загружена");
    }
}
