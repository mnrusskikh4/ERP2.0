package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthPageObject extends BasePageObject {

    private String pageUrl = "https://doc.star-smile.ru/#/auth";

    private By usernameLocator = By.id("username");
    private By passwordLocator = By.id("password");
    private By logInButtonLocator = By.xpath("//*[@id='app']/div[3]/div/div[1]");

    public AuthPageObject(WebDriver driver, Logger log) {
        super(driver, log);
    }

    public void openPage() {
        log.info("Opening page: " + pageUrl);
        openUrl(pageUrl);
        log.info("Page opened!");
    }

    public DoctorsAccountPage logIn(String username, String password) {
        log.info("Executing LogIn with username [{}] and password [{}]", username, password);
        type(username, usernameLocator);
        type(password, passwordLocator);
        click(logInButtonLocator);
        return new DoctorsAccountPage(driver, log);
    }

    public String getErrorMessageText() {
        By errorMessageLocator = By.xpath("//div[@role='status'][contains(@class, 'v-snack__content')][contains(text(), 'Неверный логин или пароль')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
        return driver.findElement(errorMessageLocator).getText();
    }

}

