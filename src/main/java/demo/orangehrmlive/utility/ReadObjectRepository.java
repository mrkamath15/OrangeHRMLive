package demo.orangehrmlive.utility;

import demo.orangehrmlive.generic.DriverScript;

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

    public Properties getPropertyFile() {
        return properties;
    }
}
