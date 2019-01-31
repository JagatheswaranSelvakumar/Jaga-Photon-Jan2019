package com.photon.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.DataTable;

public class SeleniumUtils extends CommonLibrary {

	public SeleniumUtils() throws ConfigurationException, IOException {
	}

	public static Log log = LogFactory.getLog(SeleniumUtils.class);
	public static WebDriverWait browserWithElementWait = null;
	public static String destDir;
	public static DateFormat dateFormat;
	public static Integer Ycoordinate = null;
	public static Integer Xcoordinate = null;
	public static Integer img_Width = null;
	public static Integer img_Height = null;
	public static String imgLocation;
	public static String imgSize;

	public static WebElement getElementByProperty(String objectProperty, WebDriver webDriver) throws Exception {
		String propertyType = null;
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			if (browserWithElementWait == null) {
				browserWithElementWait = new WebDriverWait(webDriver, config.getInt("elementWaitInSecond"));
			}
			propertyType = StringUtils.substringAfter(objectProperty, "~");
			objectProperty = StringUtils.substringBefore(objectProperty, "~");
			if (propertyType.equalsIgnoreCase("CSS")) {
				element = browserWithElementWait
						.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(objectProperty)));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("XPATH")) {
				element = browserWithElementWait
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objectProperty)));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("ID")) {
				element = browserWithElementWait
						.until(ExpectedConditions.presenceOfElementLocated(By.id(objectProperty)));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("NAME")) {
				element = browserWithElementWait
						.until(ExpectedConditions.presenceOfElementLocated(By.name(objectProperty)));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("LINKTEXT")) {
				element = browserWithElementWait
						.until(ExpectedConditions.presenceOfElementLocated(By.linkText(objectProperty)));
				highlightElement(element, webDriver);
			} else {
				element = browserWithElementWait
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath(objectProperty)));
				highlightElement(element, webDriver);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return element;
	}

	public static void highlightElement(WebElement element, WebDriver webDriver) {
		for (int i = 0; i < 1; i++) {
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: red; border: 3px solid red;");
		}
	}

	public static boolean isElementPresentVerifyClick(String objectProperty, WebDriver driver) {
		boolean isVerifiedAndClicked = false;
		browserWithElementWait = new WebDriverWait(driver, watingForElement);
		try {
			Thread.sleep(2000);
			element = getElementByProperty(objectProperty, driver);
			if (element != null) {
				element.click();
				isVerifiedAndClicked = true;
			} else {
				throw new Exception("Object Couldn't be retrieved and clicked");
			}
		} catch (Exception e) {
			element = null;
		}
		return isVerifiedAndClicked;
	}

	/*
	 * 
	 */

	public static boolean clearAndEnterText(String objectProperty, String Text, WebDriver driver) {
		boolean isTextEnteredResult = false;
		try {
			if ("-".equals(Text)) {
				isTextEnteredResult = true;
			} else {
				WebElement textBox = getElementByProperty(objectProperty, driver);
				textBox.clear();
				Thread.sleep(2000);
				textBox.sendKeys(Text);
				isTextEnteredResult = true;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return isTextEnteredResult;
	}


	public static boolean isElementPresentVerification(String objectProperty, WebDriver driver) throws Exception {
		boolean isElementPresent = false;
		browserWithElementWait = new WebDriverWait(driver, watingForElement);
		try {
			element = getElementByProperty(objectProperty, driver);
			if (element != null) {
				isElementPresent = true;
				System.out.println("element is" + element);
			} else {
				// throw new
				// Exception("Object Couldn't be retrieved and verified");
				System.out.println("not verified");
			}
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isElementPresent;
	}

	public static String getXLSTestData(String FileName, String SheetName, String RowId, String column)
			throws IOException {
		FileInputStream file = null;
		String col1 = null;
		DataFormatter df = new DataFormatter();
		String environment = config.getString("environment");
		String xlsName = FileName + "_" + environment;
		file = new FileInputStream(
				new File(System.getProperty("user.dir") + "/src/test/resource" + File.separator + xlsName + ".xls"));
		log.info(file);
		@SuppressWarnings("resource")
		HSSFWorkbook book = new HSSFWorkbook(file);
		HSSFSheet sheet = book.getSheet(SheetName);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int rowIterator = 1; rowIterator <= rowCount; rowIterator++) {
			String row = sheet.getRow(rowIterator).getCell(0).getStringCellValue();
			if (RowId.equalsIgnoreCase(row)) {
				for (int colIterator = 1; colIterator < sheet.getRow(rowIterator).getLastCellNum(); colIterator++) {
					String col = sheet.getRow(0).getCell(colIterator).getStringCellValue();
					if (col.equalsIgnoreCase(column)) {
						Cell cellvalue = sheet.getRow(rowIterator).getCell(colIterator);
						col1 = df.formatCellValue(cellvalue);
						break;

					}
				}
			}
		}
		return col1;

	}

	public static Map<String, List<String>> getHorizontalData(DataTable dataTable) throws Exception {
		Map<String, List<String>> dataMap = null;
		try {
			dataMap = new HashMap<String, List<String>>();
			List<String> headingRow = dataTable.raw().get(0);
			int dataTableRowsCount = dataTable.getGherkinRows().size() - 1;
			ArrayList<String> totalRowCount = new ArrayList<String>();
			totalRowCount.add(Integer.toString(dataTableRowsCount));
			dataMap.put("totalRowCount", totalRowCount);
			for (int i = 0; i < headingRow.size(); i++) {
				List<String> dataList = new ArrayList<String>();
				dataMap.put(headingRow.get(i), dataList);
				for (int j = 1; j <= dataTableRowsCount; j++) {
					List<String> dataRow = dataTable.raw().get(j);
					dataList.add(dataRow.get(i));
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return dataMap;
	}

	public static void takeScreenShot(String screenshotName, WebDriver driver) {
		try {
			destDir = "target/screenshots";
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			new File(destDir).mkdirs();
			dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
			String destFile = dateFormat.format(new Date()) + screenshotName + ".png";
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void validateImagePosition(String objectProperty, String imgLocation, String imgName,
			WebDriver driver) throws Exception {
		try {
			element = getElementByProperty(objectProperty, driver);
			Point point = element.getLocation();
			String[] imgsp = imgLocation.split("X");
			Xcoordinate = Integer.parseInt(imgsp[0]);
			Ycoordinate = Integer.parseInt(imgsp[1]);
			if (Xcoordinate.equals(point.x) && Ycoordinate.equals(point.y)) {
				log.info("------------------------------------------------------------------------------");
				log.info(imgName + " Image Position :: X & Y coordinate value are Same");
			} else {
				log.info("------------------------------------------------------------------------------");
				log.info("Mismatch of " + imgName + " Image Position X & Y coordinate");
				log.info("Actual X and Y Coordinate of Image " + point.x + "," + point.y);
				log.info("Expected X and Y Coordinate of Image " + Xcoordinate + "," + Ycoordinate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	public static void validateImageSize(String objectProperty, String imgSize, String imgName, WebDriver driver)
			throws Exception {
		try {
			element = getElementByProperty(objectProperty, driver);
			Dimension dimensions = element.getSize();
			String[] imgsize = imgSize.split("X");
			img_Width = Integer.parseInt(imgsize[0]);
			img_Height = Integer.parseInt(imgsize[1]);
			if (img_Width.equals(dimensions.width) && img_Height.equals(dimensions.height)) {
				log.info(imgName + " Image Width & Height value are Same");
			} else {
				log.info("Mismatch of " + imgName + " Image Width & Height");
				log.info("Actual Width and Height of Image " + dimensions.width + "," + dimensions.height);
				log.info("Expected Width and Height of Image " + img_Width + "," + img_Height);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

	public static String getElementText(String objectProperty, WebDriver driver) throws Exception {
		try {
			element = getElementByProperty(objectProperty, driver);
			if (element.getText() != null && !element.getText().isEmpty()) {
				return element.getText();
			} else if (element.getAttribute("value") != null && !element.getAttribute("value").isEmpty()) {
				return element.getAttribute("value");
			} else {
				return element.getAttribute("innerText");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static boolean isEnabled(String objectProperty, WebDriver driver) {
		try {
			element = getElementByProperty(objectProperty, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (element.isEnabled());
	}

	public static boolean isDisabled(String objectProperty, WebDriver driver) {
		try {
			element = getElementByProperty(objectProperty, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (!element.isEnabled());
	}

	public static boolean isVisible(String objectProperty, WebDriver driver) {
		try {
			element = getElementByProperty(objectProperty, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (element.isDisplayed());
	}

	public static boolean isInvisible(String objectProperty, WebDriver driver) {
		try {
			element = getElementByProperty(objectProperty, driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (!element.isDisplayed());
	}

	public static void page_Refresh(WebDriver driver) {
		try {
			driver.navigate().refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean selectValueFromDropDown(String objectProperty, String textValue, WebDriver driver) {
		boolean isTextEnteredResult = false;
		browserWithElementWait = new WebDriverWait(driver, watingForElement);
		try {
			Thread.sleep(1000);
			element = getElementByProperty(objectProperty, driver);
			element.click();
			Thread.sleep(1000);
			Select selectValue = new Select(element);
			selectValue.selectByVisibleText(textValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isTextEnteredResult;
	}

}
