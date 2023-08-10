package base;

import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
public class BrowserDriverFactory {
    private ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    private String browser;
    private Logger log;

    public BrowserDriverFactory(String browser, Logger log) {
        this.browser = browser.toLowerCase();
        this.log = log;
    }

    public WebDriver createDriver() {
        // Create driver
        log.info("Create driver: " + browser);

        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
                driver.set(new ChromeDriver());
                break;

            case "edge":
                System.setProperty("webdriver.msedge.driver", "src/test/resources/msedgedriver.exe");
                driver = new EdgeDriver();
                break;

            default:
                System.out.println("Do not know how to start: " + browser + ", starting chrome.");
                System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
                driver.set(new ChromeDriver());
                break;
        }

        return driver.get();
    }
}
