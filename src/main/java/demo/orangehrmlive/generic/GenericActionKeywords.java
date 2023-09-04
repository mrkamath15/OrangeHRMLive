package demo.orangehrmlive.generic;

import com.aventstack.extentreports.Status;
import demo.orangehrmlive.config.Constants;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

public class GenericActionKeywords extends  DriverScript {
    private Properties propertyFile;
    private WebDriverWait wait;

    public GenericActionKeywords() {
        FileInputStream fileInputStream = null;
        try {
            if (sEnv.equalsIgnoreCase("UAT")) {
                fileInputStream = new FileInputStream(Constants.UAT_GLOBAL_PROPERTIES);
            }
            else if (sEnv.equalsIgnoreCase("PROD")) {
                fileInputStream = new FileInputStream(Constants.PROD_GLOBAL_PROPERTIES);
            }
            propertyFile = new Properties();
            propertyFile.load(fileInputStream);
        }
        catch (Exception e) {
            logger.error("Unable to configure Global properties config file : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to configure Global properties config file : " + e.getMessage());
        }
    }

    public void launchAndNavigate(ArrayList<String> testDataList) {
        try {
            logger.info("launchAndNavigate called");
            extentTest.log(Status.INFO, "launchAndNavigate called");
            if (testDataList != null && !testDataList.equals("")) {
                String browser = testDataList.get(0);
                sAppUrl = propertyFile.getProperty("AppURL");
                logger.info("Browser selected is : " + browser);
                logger.info("Application URL : " + sAppUrl);
                switch (browser.toUpperCase()) {
                    case "CHROME":
                        System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_PATH);
                        driver = new ChromeDriver();
                        break;
                    case "FIREFOX":
                        System.setProperty("webdriver.gecko.driver", Constants.FIREFOX_DRIVER_PATH);
                        driver = new FirefoxDriver();
                        break;
                    case "IE":
                        System.setProperty("webdriver.ie.driver", Constants.IE_DRIVER_PATH);
                        driver = new InternetExplorerDriver();
                        break;
                }
                logger.info("Browser initiated successfully");
                extentTest.log(Status.PASS, "Browser initiated successfully");
                driver.manage().window().maximize();
                driver.get(sAppUrl);
                wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                DriverScript.bResult = true;
            }
        }
        catch (Exception e) {
            DriverScript.bResult = false;
            logger.error("Error in launchAndNavigate : " + e.getMessage());
            extentTest.log(Status.FAIL, "Error in launchAndNavigate : " + e.getMessage());
        }
    }

    public void verifyAllElementsExist(ArrayList<String> objectIdentifierList) {
        try {
            logger.info("verifyAllElementsExist called");
            extentTest.log(Status.INFO, "verifyAllElementsExist called");
            if (objectIdentifierList != null && !objectIdentifierList.equals("")) {

                for (String eachLocator : objectIdentifierList) {
                    By locateBy = readObjectRepository.getObjectLocator(eachLocator);
                    WebElement element = waitUntilElementIsVisible(locateBy);
                    if (element != null) {
                        DriverScript.bResult = true;
                        logger.info("Successfully located the web-element : " + element.toString());
                        extentTest.log(Status.PASS, "Successfully located the web-element : " + element.toString());
                    }
                    else {
                        Assert.fail("Unable to locate element : " + eachLocator);
                        extentTest.log(Status.FAIL, "Unable to locate element : " + eachLocator);
                        DriverScript.bResult = false;
                    }
                }
            }
        }
        catch (Exception e) {
            DriverScript.bResult = false;
            logger.error("Unable to verifyAllElementsExist : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to verifyAllElementsExist : " + e.getMessage());
        }
    }

    public boolean verifyTitle(ArrayList<String> testDataList) {
        boolean isTitleMatches;
        try {
            logger.info("verifyTitle called");
            extentTest.log(Status.INFO, "verifyTitle called");
            isTitleMatches = wait.until(ExpectedConditions.titleIs(testDataList.get(0)));
            DriverScript.bResult = true;
        }
        catch (Exception e) {
            logger.error("Unable to verifyTitle : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to verifyTitle : " + e.getMessage());
            isTitleMatches = false;
            DriverScript.bResult = false;
        }
        return isTitleMatches;
    }

    public void sendText(ArrayList<String> objectIdentifierList, ArrayList<String> testDataList) {
        try {
            logger.info("sendText called");
            extentTest.log(Status.INFO, "sendText called");
            By locateBy = readObjectRepository.getObjectLocator(objectIdentifierList.get(0));
            WebElement element = waitUntilElementIsVisible(locateBy);
            element.clear();
            if (testDataList.get(0).equalsIgnoreCase("BLANK")) {
                element.sendKeys("");
            }
            else {
                element.sendKeys(testDataList.get(0));
            }
            logger.info("Successfully sent text : " + testDataList.get(0) + " to web-element : " + element.toString());
            extentTest.log(Status.PASS, "Successfully sent text : " + testDataList.get(0) + " to web-element : " + element.toString());
            DriverScript.bResult = true;
        }
        catch (Exception e) {
            logger.error("Unable to send text to web-element : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to send text to web-element : " + e.getMessage());
            DriverScript.bResult = false;
        }
    }

    public void click(ArrayList<String> objectIdentifierList) {
        try {
            logger.info("click called");
            extentTest.log(Status.INFO, "click called");
            By locateBy = readObjectRepository.getObjectLocator(objectIdentifierList.get(0));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locateBy));
            element.click();
            DriverScript.bResult = true;
            logger.info("Successfully clicked the web-element : " + element.toString());
            extentTest.log(Status.PASS, "Successfully clicked the web-element : " + element.toString());
        }
        catch (Exception e) {
            logger.error("Unable to click web-element : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to click web-element : " + e.getMessage());
            DriverScript.bResult = false;
        }
    }

    public void verifyTextMatches(ArrayList<String> objectIdentifierList, ArrayList<String> testDataList) {
        try {
            logger.info("verifyTextMatches called");
            extentTest.log(Status.INFO, "verifyTextMatches called");
            By locateBy = readObjectRepository.getObjectLocator(objectIdentifierList.get(0));
            WebElement element = waitUntilElementIsVisible(locateBy);
            String expectedText = testDataList.get(0);
            String actualText = element.getText();
            if (expectedText.equals(actualText)) {
                DriverScript.bResult = true;
                logger.info("Element text : " + actualText + ", matches with the expected text : " + expectedText);
                extentTest.log(Status.PASS, "Element text : " + actualText + ", matches with the expected text : " + expectedText);
            }
            else {
                DriverScript.bResult = false;
                logger.info("Element text : " + actualText + ", DOES NOT match with the expected text : " + expectedText);
                extentTest.log(Status.FAIL, "Element text : " + actualText + ", DOES NOT match with the expected text : " + expectedText);
            }

        }
        catch (Exception e) {
            logger.info("Unable to retrieve text from web-element : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to retrieve text from web-element : " + e.getMessage());
            DriverScript.bResult = false;
        }
    }

    public WebElement waitUntilElementIsVisible(By locateBy) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locateBy));
            return element;
        }
        catch (Exception e) {
            logger.error("Unable to locate the web-element : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to locate the web-element : " + e.getMessage());
            return null;
        }
    }

    public void captureScreenshot(String... name) {
        try {
            sScreenshotFilePath = Constants.SCREENSHOT_FOLDER_PATH + "/" + DATE_TIME_FORMAT + "/" + sTestCaseId + "_" + sTestStepId + "_" + sTestStepName.replaceAll(" ", "_")
                    + "_" + DATE_TIME_FORMAT + ".png";
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(srcFile, new File(sScreenshotFilePath));
        }
        catch (Exception e) {
            logger.error("Unable to capture screenshot : " + e.getMessage());
            extentTest.log(Status.FAIL, "Unable to capture screenshot" + e.getMessage());
        }
    }
}
