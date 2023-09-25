package base;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestUtilities extends BaseTest {

    @DataProvider(name="files")
    protected static Object[][] files() {
        return new Object[][] {
                {1,"index.html"},
                {2,"logo.png"},
                {3,"text.txt"}
        };
    }

    /** Take screenshot */
    protected void takeScreenshot(String fileName) {
        File scrFile = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir")
                + File.separator + "test-output"
                + File.separator + "screenshots"
                + File.separator + getTodaysDate()
                + File.separator + testSuiteName
                + File.separator + testName
                + File.separator + testMethodName
                + File.separator + getSystemTime()
                + " " + fileName + ".png";
        try {
            FileUtils.copyFile(scrFile, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Todays date in yyyyMMdd format */
    private static String getTodaysDate() {
        return (new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    /** Current time in HHmmssSSS */
    private String getSystemTime() {
        return (new SimpleDateFormat("HHmmssSSS").format(new Date()));
    }

    /** Get logs from browser console */
    protected List<LogEntry> getBrowserLogs() {
        LogEntries log = BaseTest.getDriver().manage().logs().get("browser");
        List<LogEntry> logList = log.getAll();
        return logList;
    }
}
