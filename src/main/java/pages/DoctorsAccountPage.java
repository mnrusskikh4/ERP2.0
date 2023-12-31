package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DoctorsAccountPage extends BasePageObject {

    private By expandMoreIconLocator = By.xpath("//*[contains(@class, 'ml-5')]\n");

    public DoctorsAccountPage (WebDriver driver, Logger log) {
            super(driver, log);
        }


    public boolean isExpandMoreIconPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(expandMoreIconLocator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(expandMoreIconLocator));
    }

    public DoctorsProfilePage clickExpandMoreAndSelectProfile() {
        // Нажимаем на кнопку "expand_more"
        WebElement expandMoreButton = driver.findElement(By.xpath("//*[contains(@class, 'ml-5')]\n"));
        expandMoreButton.click();

        // Ожидаем, пока не станет видимым элемент "Профиль" и кликаем по нему
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement profileOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item__title' and text()='Профиль']")));
        profileOption.click();

        return new DoctorsProfilePage(driver, log);
    }


}

