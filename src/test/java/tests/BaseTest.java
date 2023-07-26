package tests;

import org.openqa.selenium.WebDriver;

public abstract class BaseTest {
    private static WebDriver driver;
    public static WebDriver getDriver() {
        return driver;
    }

}
