package pages;

public class DoctorsAccountPage extends BasePageObject {

    private String pageUrl = "http://the-internet.herokuapp.com/";

    private By formAuthenticationLinkLocator = By.linkText("Form Authentication");

    public WelcomePageObject(WebDriver driver, Logger log) {
        super(driver, log);
    }

    /** Open WelcomePage with it's url */
    public void openPage() {
        log.info("Opening page: " + pageUrl);
        openUrl(pageUrl);
        log.info("Page opened!");
    }

    /** Open LoginPage by clicking on Form Authentication Link */
    public LoginPage clickFormAuthenticationLink() {
        log.info("Clicking Form Authentication link on Welcome Page");
        click(formAuthenticationLinkLocator);
        return new LoginPage(driver, log);
    }
    {

}
