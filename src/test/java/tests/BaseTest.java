package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import utils.DriverFactory;
import utils.PropertyReader;

public abstract class BaseTest {
    protected static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.getDriver(PropertyReader.getBrowser());
        driver.get(PropertyReader.getUrl());
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    //@DataProvider (name = "dataProvider")
    //public Object[][] dataProviderMethod() {
    //return new Object[][]{{"awsavichev@gmail.com"}, {"mikhailrusskikh4@yandex.ru"}};
    //}
}
