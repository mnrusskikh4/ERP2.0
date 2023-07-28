package utils;

import org.openqa.selenium.WebDriver;

import java.io.File;

public class DriverFactory {
    private static WebDriver driver;
    private final static String DRIVER_PATH = "src/test/resources/";

    public static WebDriver getDriver(Browser browser) {
        File file;
        switch (browser) {
            case CHROME:
                file = new File(DRIVER_PATH + "choromedriver.exe");
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
                driver = new ChromeDriver();
                break;
            case EDGE:
                file = new File(DRIVER_PATH + "msedgedriver.exe");
                System.setProperty("webdriver.edge.driver", file.getAbsolutePath());
                driver = new EdgeDriver();
                break;

        }
        driver.manage().window().maximize();
        return driver;
    }
}
