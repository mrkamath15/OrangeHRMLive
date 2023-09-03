package demo.orangehrmlive.config;

public class Constants {
    //Paths Test Cases and Report
    public static String TESTCASES_PATH = System.getProperty("user.dir") + "/TestCases/#ENV/#PRODUCT.xlsx";
    public static final String REPORTS_PATH = System.getProperty("user.dir") + "/Reports/";

    //Global properties
    public static final String UAT_GLOBAL_PROPERTIES = System.getProperty("user.dir") + "/src/main/resources/GlobalProperties/OrangeHRM_UAT.properties";
    public static final String PROD_GLOBAL_PROPERTIES = System.getProperty("user.dir") + "/src/main/resources/GlobalProperties/OrangeHRM_PROD.properties";

    //OR Path
    public static final String OBJECT_REPO_PATH = System.getProperty("user.dir") + "/src/main/resources/ObjectRepository/ObjectRepository.properties";

    //Drivers Path
    public static final String CHROME_DRIVER_PATH = System.getProperty("user.dir") + "/src/main/resources/Libs/chromedriver.exe";
    public static final String FIREFOX_DRIVER_PATH = System.getProperty("user.dir") + "/src/main/resources/Libs/firefoxdriver.exe";
    public static final String IE_DRIVER_PATH = System.getProperty("user.dir") + "/src/main/resources/Libs/iedriver.exe";

    //Sheets
    public static final String SHEET_TESTCASES = "TestCases";
    public static final String SHEET_DEFAULT_TEST_STEPS = "TestSteps";

    //Test cases sheet
    public static final int COL_TC_ID = 0;
    public static final int COL_TC_NAME = 1;
    public static final int COL_TC_RUN_MODE = 2;
    public static final int COL_TC_RESULT = 3;
    public static final int COL_TC_START_TIME = 4;
    public static final int COL_TC_END_TIME = 5;

    //Test steps sheet
    public static final int COL_TS_ID = 1;
    public static final int COL_TS_NAME = 2;
    public static final int COL_TS_ACTION_KEYWORD = 3;
    public static final int COL_TS_OBJECT_IDENTIFIER = 4;
    public static final int COL_TS_TEST_DATA = 5;
    public static final int COL_TS_RESULT = 6;
    public static final int COL_TS_SCREENSHOT_PATH = 7;
}
