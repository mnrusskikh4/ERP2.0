package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthPageObject extends BasePageObject {

    private String pageUrl = "https://doc.star-smile.ru/#/auth";

    private By usernameLocator = By.id("username");
    private By passwordLocator = By.id("password");
    private By logInButtonLocator = By.id("login-btn");

    public AuthPageObject(WebDriver driver, Logger log) {
        super(driver, log);
    }

    public void openPage() {
        String url = "https://doc.star-smile.ru/#/auth";
        driver.get(url);
        Logger log = LogManager.getLogger(AuthPageObject.class);
        log.info("Main page is opened.");
    }

    /** Execute log in */
    public DoctorsAccountPage logIn(String username, String password) {
        Logger log = LogManager.getLogger(AuthPageObject.class);
        log.info("Executing LogIn with username [{}] and password [{}]", username, password);
        type(username, usernameLocator);
        type(password, passwordLocator);
        click(logInButtonLocator);
        return new DoctorsAccountPage(driver, log);
    }

}

