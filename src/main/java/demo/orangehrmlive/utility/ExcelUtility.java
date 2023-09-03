package demo.orangehrmlive.utility;

import demo.orangehrmlive.config.Constants;
import demo.orangehrmlive.generic.DriverScript;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtility extends DriverScript {
    private static XSSFWorkbook excelWorkbook;
    private static XSSFSheet excelSheet;
    private static XSSFRow excelRow;
    private static XSSFCell excelCell;

    public static void setExcelFile(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            excelWorkbook = new XSSFWorkbook(fileInputStream);
            logger.info("Excel set successfully : " + filePath);
        }
        catch (Exception e) {
            logger.error("Error setting excel file : " + e.getMessage());
        }
    }

    public static int getRowCount(String sheetName) {
        int rowCount = 0;
        try {
            excelSheet = excelWorkbook.getSheet(sheetName);
            rowCount = excelSheet.getPhysicalNumberOfRows();
            logger.info("Total row Count : " + rowCount);
        }
        catch (Exception e) {
            logger.error("Error getting total excel rows : "  + e.getMessage());
        }
        return rowCount;
    }

    public static String getCellData(String sheetName, int rowNum, int colNum) {
        String cellData = "";
        try {
            excelSheet = excelWorkbook.getSheet(sheetName);
            excelRow = excelSheet.getRow(rowNum);
            excelCell = excelRow.getCell(colNum);
            cellData = excelCell.getStringCellValue();
            logger.info("Successfully read the excel cell data : " + cellData);
        }
        catch (Exception e) {
            logger.error("Error getting text from excel : " + e.getMessage());
        }
        return cellData;
    }

    public static void setCellData(String sheetName, int rowNum, int colNum, String cellText) {
        try {
            excelSheet = excelWorkbook.getSheet(sheetName);
            excelRow = excelSheet.getRow(rowNum);
            excelCell = excelRow.getCell(colNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (excelCell == null) {
                excelCell = excelRow.createCell(colNum);
            }
            excelCell.setCellValue(cellText);

            FileOutputStream fileOutputStream = new FileOutputStream(sResultsPath);
            excelWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            logger.info("Successfully written data to excel : sheetName = " + sheetName + ", rowNum = " + rowNum + ", colNum = " + colNum + ", cellText = " + cellText);
        }
        catch (Exception e) {
            logger.error("Unable to set excel cell data : sheetName = " + sheetName + ", rowNum = " + rowNum + ", colNum = " + colNum + ", cellText = " + cellText + " : " + e.getMessage());
        }
    }

    public static int getRowContains(String sheetName, int colNum, String testCaseId) {
        int rowNum = 0;
        try {
            excelSheet = excelWorkbook.getSheet(sheetName);
            int totalRows = getRowCount(sheetName);

            for (int i = 1; i < totalRows; i++) {
                if (ExcelUtility.getCellData(sheetName, i, colNum).equals(testCaseId)) {
                    rowNum = i;
                    break;
                }
            }
        }
        catch (Exception e) {
            logger.error("Unable to get test case start : " + e.getMessage());
        }
        logger.info("Test case start : " + rowNum);
        return rowNum;
    }

    public static int getTestStepCount(String sheetName, int start, String testCaseId) {
        int lastStep = 0;
        try {
            excelSheet = excelWorkbook.getSheet(sheetName);
            int rowCount = ExcelUtility.getRowCount(sheetName);
            for (int i = start; i < rowCount; i++) {
                if (!ExcelUtility.getCellData(sheetName, i, Constants.COL_TC_ID).equalsIgnoreCase(testCaseId)) {
                    lastStep = i;
                    logger.info("Test case end : " + lastStep);
                    return lastStep;
                }
            }
            logger.info("Test case end : " + lastStep);
            return ExcelUtility.getRowCount(sheetName);
        }
        catch (Exception e) {
            logger.error("Unable to get last test step : " + e.getMessage());
        }
        logger.info("Test case end : " + lastStep);
        return lastStep;
    }

    public static void copyFile(String srcPath, String destPath) {
        try {
            FileUtils.copyFile(new File(srcPath), new File(destPath));
            logger.info("Successfully copied file : " + srcPath + " to " + destPath);
        }
        catch (Exception e) {
            logger.error("Unable to copy the file : " + e.getMessage());
        }
    }

}
