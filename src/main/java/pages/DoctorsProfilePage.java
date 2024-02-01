package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DoctorsProfilePage extends BasePageObject {
    private WebDriver driver;

    private By checkboxes = By.xpath("//input[@id='input-14322' and @role='checkbox' and @aria-checked='true']");

    public DoctorsProfilePage(WebDriver driver, Logger log) {
        super(driver, log);
        this.driver = driver;
    }

    public boolean areAllCheckboxesSelected() {
        List<WebElement> allCheckboxes = findAll(checkboxes);
        for (WebElement checkbox : allCheckboxes) {
            if (!checkbox.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public WebElement getFirstCheckboxElement() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-input--selection-controls__ripple' and @style='color: rgb(133, 120, 215); caret-color: rgb(133, 120, 215);']")));

        return element;
    }
}
