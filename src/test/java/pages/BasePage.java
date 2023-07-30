package pages;

import org.openqa.selenium.WebDriver;
import static tests.BaseTest.getDriver;

public abstract class BasePage {
    protected static WebDriver driver;
    public BasePage() {
        this.driver = getDriver();
    }


}
