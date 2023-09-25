package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Listeners({base.TestListener.class})
public class BaseTest {

    // Используем ThreadLocal для WebDriver
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected Logger log;

    protected String testSuiteName;
    protected String testName;
    protected String testMethodName;

    public static WebDriver getDriver() {
        return driver.get();
    }

    @Parameters({"browser", "chromeProfile", "deviceName"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, @Optional("chrome") String browser, @Optional String profile,
                      @Optional String deviceName, ITestContext ctx) {
        String testName = ctx.getCurrentXmlTest().getName();
        log = LogManager.getLogger(testName);

        BrowserDriverFactory factory = new BrowserDriverFactory(browser, log);
        driver.set(factory.createDriver());

        getDriver().manage().window().maximize();

        this.testSuiteName = ctx.getSuite().getName();
        this.testName = testName;
        this.testMethodName = method.getName();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (getDriver() != null) {
            log.info("Close driver");
            getDriver().quit();
        }
    }
}
