package pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AuthPage extends BasePage {
        @FindBy(id = "username")
        private WebElement loginField;
        public AuthPage(){super();}
        public void fillTheLoginField(String keyword) {
            loginField.sendKeys(keyword); }
        public void pressEnter() {
            loginField.sendKeys(Keys.TAB);
    }
}
