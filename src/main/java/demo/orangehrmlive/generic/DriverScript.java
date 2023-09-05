package demo.orangehrmlive.generic;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import demo.orangehrmlive.config.Constants;
import demo.orangehrmlive.utility.ExcelUtility;
import demo.orangehrmlive.utility.ReadObjectRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DriverScript {
    public static Logger logger = LogManager.getLogger();
    public static String sResultsPath = "";
    public static String DATE_TIME_FORMAT = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS_a").format(new Date());

    public static WebDriver driver;
    public static GenericActionKeywords genericActionKeywords;
    public static ReadObjectRepository readObjectRepository;

    public static String sEnv;
    public static boolean bResult;
    public static String sAppUrl;

    public static String sTestCaseId;
    public static String sTestCaseName;
    public static String sRunMode;

    public static String sTestStepId;
    public static String sTestStepName;
    public static String sActionKeyword;
    public static String sObjectIdentifier;
    public static String sTestData;

    public static int iTestStep;
    public static int iTestLastStep;

    private static ExtentReports extentReports;
    public static ExtentTest extentTest;
    private static ExtentSparkReporter extentSparkReporter;

    public static Method method;

    public static String sScreenshotFilePath = "";
    public static String sExtentScreenshotFilePath = "";

    @BeforeSuite
    public static void setUp() {
        if (!Constants.SCREENSHOT_FOLDER_PATH.exists()) {
            Constants.SCREENSHOT_FOLDER_PATH.mkdir();
            logger.info("Created screenshot folder");
        }
        extentSparkReporter = new ExtentSparkReporter(Constants.EXTENT_REPORT_PATH);
        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setReportName("Orange HRM Report");
        extentSparkReporter.config().setDocumentTitle("Orange HRM");
        extentSparkReporter.config().setTheme(Theme.DARK);

        extentReports = new ExtentReports();
        extentReports.attachReporter(extentSparkReporter);
    }

    @AfterSuite
    public static void tearDown() {
        driver.quit();
        logger.info("Closed the driver instance");
        extentReports.flush();
    }


    @Test
    @Parameters({"product", "env"})
    public void driverScript(String productToTest, String testEnv) {
        sEnv = testEnv;
        logger.info("Product : " + productToTest);
        logger.info("Env : " + testEnv);

        Constants.TESTCASES_PATH = Constants.TESTCASES_PATH.replace("#ENV", testEnv).replace("#PRODUCT", productToTest);
        logger.info("TestCases Path : " + Constants.TESTCASES_PATH);

        sResultsPath = Constants.REPORTS_PATH + productToTest + "_" + DATE_TIME_FORMAT + ".xlsx";
        logger.info("Results Path : " + sResultsPath);

        genericActionKeywords = new GenericActionKeywords();
        readObjectRepository = new ReadObjectRepository(Constants.OBJECT_REPO_PATH);

        ExcelUtility.copyFile(Constants.TESTCASES_PATH, sResultsPath);
        ExcelUtility.setExcelFile(sResultsPath);

        executeTestCases();
    }

    public void executeTestCases() {
        int iTotalTestCases = ExcelUtility.getRowCount(Constants.SHEET_TESTCASES);
        logger.info("Total test cases : " + (iTotalTestCases-1));
        for (int iTestCase = 1; iTestCase < iTotalTestCases; iTestCase++) {
            sRunMode = ExcelUtility.getCellData(Constants.SHEET_TESTCASES, iTestCase, Constants.COL_TC_RUN_MODE);
            if (sRunMode.equalsIgnoreCase("Yes")) {
                sTestCaseId = ExcelUtility.getCellData(Constants.SHEET_TESTCASES, iTestCase, Constants.COL_TC_ID);
                sTestCaseName = ExcelUtility.getCellData(Constants.SHEET_TESTCASES, iTestCase, Constants.COL_TC_NAME);
                logger.info("Executing test case : " + sTestCaseId + " - " + sTestCaseName);

                iTestStep = ExcelUtility.getRowContains(Constants.SHEET_DEFAULT_TEST_STEPS, Constants.COL_TC_ID, sTestCaseId);
                iTestLastStep = ExcelUtility.getTestStepCount(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, sTestCaseId);
                logger.info("Test Step start : " + iTestStep);
                logger.info("Test Step end : " + iTestLastStep);
                for (;iTestStep < iTestLastStep; iTestStep++) {
                    sTestStepId = ExcelUtility.getCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_ID);
                    sTestStepName = ExcelUtility.getCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_NAME);
                    sActionKeyword = ExcelUtility.getCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_ACTION_KEYWORD);
                    sObjectIdentifier = ExcelUtility.getCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_OBJECT_IDENTIFIER);
                    sTestData = ExcelUtility.getCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_TEST_DATA);

                    logger.info("sTestStepId : " + sTestStepId);
                    logger.info("sTestStepName : " + sTestStepName);
                    logger.info("sActionKeyword : " + sActionKeyword);
                    logger.info("sObjectIdentifier : " + sObjectIdentifier);
                    logger.info("sTestData : " + sTestData);

                    if (sActionKeyword != null && !sActionKeyword.isEmpty()) {
                        executeActions();
                    }
                }
                if (DriverScript.bResult == true) {
                    ExcelUtility.setCellData(Constants.SHEET_TESTCASES, iTestCase, Constants.COL_TC_RESULT, Constants.RESULT_PASS);
                }
                else {
                    ExcelUtility.setCellData(Constants.SHEET_TESTCASES, iTestCase, Constants.COL_TC_RESULT, Constants.RESULT_FAIL);
                }
            }
        }
    }

    public void executeActions() {
        try {
            ArrayList<String> objectIdentifierList = new ArrayList<>();
            String[] objectIdentifierArray = null;
            ArrayList<String> testDataList = new ArrayList<>();
            String[] testDataArray = null;

            if (sObjectIdentifier.contains(",")) {
                objectIdentifierArray = sObjectIdentifier.split(",");
                for (int i = 0; i < objectIdentifierArray.length; i++) {
                    objectIdentifierList.add(objectIdentifierArray[i]);
                }
            }
            else if (sObjectIdentifier.equals("")) {}
            else {
                objectIdentifierList.add(sObjectIdentifier);
            }

            if (sTestData.contains(",")) {
                testDataArray = sTestData.split(",");
                for (int i = 0; i < testDataArray.length; i++) {
                    testDataList.add(testDataArray[i]);
                }
            }
            else if (sTestData.equals("")) {}
            else {
                testDataList.add(sTestData);
            }

            Class<?>[] paramTypeArray = null;
            Object[] paramDataArray = null;

            if (objectIdentifierList.size() > 0 && testDataList.size() > 0) {
                paramTypeArray = new Class[] {ArrayList.class, ArrayList.class};
                paramDataArray = new Object[] {objectIdentifierList, testDataList};
            }
            else if (objectIdentifierList.size() == 0 && testDataList.size() == 0) {
                paramTypeArray = new Class[] {};
                paramDataArray = new Object[] {};
            }
            else if (objectIdentifierList.size() > 0 && testDataList.size() == 0) {
                paramTypeArray = new Class[] {ArrayList.class};
                paramDataArray = new Object[] {objectIdentifierList};
            }
            else {
                paramTypeArray = new Class[] {ArrayList.class};
                paramDataArray = new Object[] {testDataList};
            }
            String testName = sTestCaseId + "_" + sTestStepId + "_" + sTestStepName;
            extentTest = extentReports.createTest(testName);
            method = genericActionKeywords.getClass().getMethod(sActionKeyword, paramTypeArray);
            method.invoke(genericActionKeywords, paramDataArray);
        }
        catch (Exception e) {
            logger.info("Unable to execute test actions : " + e.getMessage());
        }
        finally {
            if (DriverScript.bResult == true) {
                genericActionKeywords.captureScreenshot();
                ExcelUtility.setCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_RESULT, Constants.RESULT_PASS);
                extentTest.log(Status.PASS, "Test step passed");
                extentTest.log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromPath(sExtentScreenshotFilePath).build());
            }
            else {
                genericActionKeywords.captureScreenshot();
                ExcelUtility.setCellData(Constants.SHEET_DEFAULT_TEST_STEPS, iTestStep, Constants.COL_TS_RESULT, Constants.RESULT_FAIL);
                extentTest.log(Status.FAIL, "Test step failed");
                extentTest.log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromPath(sExtentScreenshotFilePath).build());
            }
        }
    }

}
