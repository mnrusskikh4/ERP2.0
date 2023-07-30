package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AuthPage extends BasePage {

    @FindBy(id = "username")
    protected WebElement loginField;
}
