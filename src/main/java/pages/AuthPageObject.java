package pages;

import jdk.internal.instrumentation.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthPageObject extends BasePageObject {

    private WebDriver driver;
    private Logger log;

    private String pageUrl = "https://doc.star-smile.ru/#/auth";

    private By formAuthenticationLinkLocator = By.linkText("Form Authentication");

    public AuthPageObject(WebDriver driver, Logger log) {
        super(driver, log);
    }

    /** Open AuthPage with it's url */
    public void openPage() {
        log.info("Opening page: " + pageUrl);
        openUrl(pageUrl);
        log.info("Page opened!");
    }

    /** Open LoginPage by clicking on login-btn */
    public DoctorsAccountPage clickFormAuthenticationLink() {
        log.info("Clicking Form Authentication link on AuthPage");
        click(formAuthenticationLinkLocator);
        return new DoctorsAccountPage(driver, log);
    }

}
