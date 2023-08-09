package pages;

import org.openqa.selenium.WebDriver;
import static tests.BaseTest.getDriver;

public abstract class BasePage {
    WebDriver driver;
    public BasePage() {
        this.driver = getDriver();
    }


}
