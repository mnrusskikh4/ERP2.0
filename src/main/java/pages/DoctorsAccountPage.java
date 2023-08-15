package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DoctorsAccountPage extends BasePageObject {

    private String pageUrl = "https://doc.star-smile.ru/#/";

    private final By logInButtonLocator = By.id("login-btn");

    public DoctorsAccountPage (WebDriver driver, Logger log) {
        super(driver, log);
    }

    /** Open DoctorsAccountPage with it's url */
    public void openPage() {
        log.info("Opening page: " + pageUrl);
        openUrl(pageUrl);
        log.info("Page opened!");
    }

    /** Open DoctorsAccountPage by clicking on Login Button Locator */
    public AuthPageObject logInButtonLocator() {
        log.info("Clicking Login Button Locator on AuthPageObject");
        click(logInButtonLocator);
        return new AuthPageObject(driver, log);
    }

}
