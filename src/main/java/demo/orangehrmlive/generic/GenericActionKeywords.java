package demo.orangehrmlive.generic;

import demo.orangehrmlive.config.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
        }
    }

    public void launchAndNavigate(ArrayList<String> testDataList) {
        try {
            logger.info("launchAndNavigate called");
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
                driver.manage().window().maximize();
                driver.get(sAppUrl);
                wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                DriverScript.bResult = true;
            }
        }
        catch (Exception e) {
            DriverScript.bResult = false;
            logger.info("Error in launchAndNavigate : " + e.getMessage());
        }
    }

    public void verifyAllElementsExist(ArrayList<String> objectIdentifierList) {
        try {
            logger.info("verifyAllElementsExist called");
            if (objectIdentifierList != null && !objectIdentifierList.equals("")) {

                for (String eachLocator : objectIdentifierList) {
                    By locateBy = readObjectRepository.getObjectLocator(eachLocator);
                    WebElement element = waitUntilElementIsVisible(locateBy);
                    if (element != null) {
                        DriverScript.bResult = true;
                        logger.info("Successfully located the web-element : " + element.toString());
                    }
                    else {
                        Assert.fail("Unable to locate element : " + eachLocator);
                        DriverScript.bResult = false;
                    }
                }
            }
        }
        catch (Exception e) {
            DriverScript.bResult = false;
            logger.info("Unable to verifyAllElementsExist : " + e.getMessage());
        }
    }

    public void verifyTitle(ArrayList<String> testDataList) {
        logger.info("verifyTitle called");
    }

    public void sendText(ArrayList<String> objectIdentifierList, ArrayList<String> testDataList) {
        logger.info("sendText called");
    }

    public void click(ArrayList<String> objectIdentifierList) {
        logger.info("click called");
    }

    public void verifyTextMatches(ArrayList<String> objectIdentifierList, ArrayList<String> testDataList) {
        logger.info("verifyTextMatches called");
    }

    public WebElement waitUntilElementIsVisible(By locateBy) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locateBy));
            return element;
        }
        catch (Exception e) {
            logger.error("Unable to locate the web-element : " + e.getMessage());
            return null;
        }
    }
}
