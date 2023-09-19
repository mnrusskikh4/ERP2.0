package pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class DoctorsAccountPage extends BasePageObject {

    private String pageUrl = "https://doc.star-smile.ru/#/";

    public DoctorsAccountPage (WebDriver driver, Logger log) {
        super(driver, log);
    }

    /** Open DoctorsAccountPage with it's url */
    public void openPage() {
        log.info("Opening page: " + pageUrl);
        openUrl(pageUrl);
        log.info("Page opened!");
    }


}
