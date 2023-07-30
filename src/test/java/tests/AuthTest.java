package tests;

import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.AuthPage;

public class AuthTest extends AuthPage {

    public AuthTest() {
        super();
    }

    @BeforeClass
    public void setUp() {
    }

    @Test
    public void fillTheLoginField() {
        String text = "awsavichev@gmail.com";
        loginField.sendKeys(text);
    }

    public void pressTab() {
        loginField.sendKeys(Keys.TAB);
    }
}
