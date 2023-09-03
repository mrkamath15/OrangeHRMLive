package demo.orangehrmlive.generic;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class DriverScript {

    @Test
    @Parameters({"product", "env"})
    public void driverScript(String productToTest, String testEnv) {
        System.out.println(productToTest);
        System.out.println(testEnv);
    }

}
