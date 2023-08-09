package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
public class BrowserDriverFactory {
    private ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
    private String browser;

    public BrowserDriverFactory(String browser) {
        this.browser = browser.toLowerCase();
    }

    public WebDriver createDriver() {
        // Create driver
        System.out.println("Create driver: " + browser);

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
