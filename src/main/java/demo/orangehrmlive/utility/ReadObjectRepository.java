package demo.orangehrmlive.utility;

import demo.orangehrmlive.generic.DriverScript;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.util.Properties;

public class ReadObjectRepository extends DriverScript {
    private Properties properties;

    public ReadObjectRepository(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            properties = new Properties();
            properties.load(fileInputStream);
        }
        catch (Exception e) {
            logger.error("Unable to configure object repository :" + e.getMessage());
        }
    }

    public By getObjectLocator(String propertyName) {
        String propertyValue = getProperty(propertyName);
        return getLocator(propertyValue);
    }

    private String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

    private By getLocator(String propertyValue) {
        String locateBy = propertyValue.split("~")[0];
        String locatorValue = propertyValue.split("~")[1];

        By locator = null;
        switch (locateBy.toUpperCase()) {
            case "ID":
                locator = By.id(locatorValue);
                break;
            case "NAME":
                locator = By.name(locatorValue);
                break;
            case "CLASSNAME":
                locator = By.className(locatorValue);
                break;
            case "TAGNAME":
                locator = By.tagName(locatorValue);
                break;
            case "LINKTEXT":
                locator = By.linkText(locatorValue);
                break;
            case "PARTIALLINKTEXT":
                locator = By.partialLinkText(locatorValue);
                break;
            case "XPATH":
                locator = By.xpath(locatorValue);
                break;
            case "CSS":
                locator = By.cssSelector(locatorValue);
                break;
        }
        return locator;
    }


}
