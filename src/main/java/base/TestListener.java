package base;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    Logger log = LogManager.getLogger(TestListener.class);
    String testName;
    String testMethodName;

    @Override
    public void onTestStart(ITestResult result) {
        this.testMethodName = result.getMethod().getMethodName();
        log.info("[Starting " + testMethodName + "]");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("[Test " + testMethodName + " passed]");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        captureScreenshotAndLogs();
        log.info("[Test " + testMethodName + " failed]");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        captureScreenshotAndLogs();
        log.info("[Test " + testMethodName + " skipped]");
    }

    @Override
    public void onStart(ITestContext context) {
        this.testName = context.getCurrentXmlTest().getName();
        this.log = LogManager.getLogger(testName);
        log.info("[TEST " + testName + " STARTED]");
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("[ALL " + testName + " FINISHED]");
    }
    @Attachment
    public static void captureScreenshotAndLogs() {
        // Захват скриншота и прикрепление к Allure-отчёту
        byte[] screenshotBytes = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.BYTES);
        Allure.getLifecycle().addAttachment("Failure screenshot", "image/png", ".png", screenshotBytes);

        // Получение логов браузера и прикрепление к Allure-отчёту
        String browserLogs = BaseTest.getDriver().manage().logs().get(LogType.BROWSER).getAll().toString();
        Allure.addAttachment("Browser logs", browserLogs);
    }
}
