package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthPageObject extends BasePageObject {

    private String pageUrl = "https://doc.star-smile.ru/#/auth";

    private By usernameLocator = By.id("username");
    private By passwordLocator = By.id("password");
    private By logInButtonLocator = By.id("login-btn");

    public AuthPageObject(WebDriver driver, Logger log) {
        super(driver, log);
    }

    /** Open AuthPage with it's url */
    public void openPage() {
        log.info("Opening page: " + pageUrl);
        openUrl(pageUrl);
        log.info("Page opened!");
    }

    /** Execute log in */
    public DoctorsAccountPage logIn(String username, String password) {
        log.info("Executing LogIn with username [{}] and password [{}]", username, password);
        type(username, usernameLocator);
        type(password, passwordLocator);
        click(logInButtonLocator);
        return new DoctorsAccountPage(driver, log);
    }

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Максимальное ожидание в течение 10 секунд
    By xpathSelector = By.xpath("//div[@class='v-snack__wrapper v-sheet theme--dark' and contains(@style, 'display: none;')]//div[@class='v-snack__content' and text()='Неверный логин или пароль']");
    WebElement errorMessageElement = wait.until(ExpectedConditions.presenceOfElementLocated(xpathSelector));


    public String getErrorMessageText() {
    }
}

