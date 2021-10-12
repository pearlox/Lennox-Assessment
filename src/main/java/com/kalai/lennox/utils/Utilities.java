package com.kalai.lennox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.google.common.io.Files;
import com.kalai.lennox.exception.FrameworkException;
import com.kalai.lennox.objectRepositories.ProductCatalogPage;

public class Utilities extends TestBase {
	protected static long PAGE_LOAD_TIMEOUT = 60;
	protected static long IMPLICIT_WAIT = 20;

	private WebDriverWait wait = new WebDriverWait(driver,Long.valueOf(prop.getProperty("longwait")));
	private JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;

	/**
	 * randomNumber method used to return random value between 0 - 1000
	 * @return integer
	 */
	public static int randomNumber() {
		Random random = new Random();
		return random.nextInt(1000);
	}

	/**
	 * getDateFormated returns us String with current date and time in dd-MM-yyyy HHmmss AM/PM format
	 * @return string
	 */
	private String getDateFormated() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
		Date date = new Date();
		return sdf.format(date).replace(":", "");
	}

	/**
	 * generateRandomLetters is used to generate random letters of specified length
	 * @param length indicates the length of the word to be generated
	 * @return string
	 */
	public String generateRandomLetters(int length) {
		StringBuilder sb = new StringBuilder();
		String alphaNumberic = "ABCDEFGHIJKLMNOPQRSTUVWZY1234567890abcdefghijklmnopqrstuvwxyz";
		for(int i=0;i<length;i++) {
			int index = (int) (alphaNumberic.length()*Math.random());
			sb.append(alphaNumberic.charAt(index));
		}

		return sb.toString().trim();
	}

	/**
	 * takeScreenshot method is used to take screenshot and return the path of the screenshot stored location
	 * @return file path
	 */
	public String takeScreenshot() {
		String encodedBase64;
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
		String pathName = System.getProperty("user.dir")+File.separator+"Screenshots"+File.separator+"SS_"+getDateFormated()+".png";
		File destFile = new File(pathName);
		copyFile(srcFile, destFile);
		try (FileInputStream fileInputStream =new FileInputStream(destFile)){
			Files.copy(srcFile, destFile);
	        byte[] bytes =new byte[(int)destFile.length()];
	        fileInputStream.read(bytes);
	        encodedBase64 = new String(Base64.encodeBase64(bytes));
		} catch (Exception e) {
			throw new FrameworkException(e.getMessage());
		}
	    return "data:image/png;base64,"+encodedBase64;
	}
	
	/**
	 * copyFile method is used to copy the file from source location to destination location
	 * @param srcFile indicates path of source file to be copied
	 * @param destFile indicates destination file path
	 */
	private void copyFile(File srcFile,File destFile) {
		try {
			Files.copy(srcFile, destFile);
		} catch (Exception e) {
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * clickElement is used to perform selenium click action with specified xpath
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @throws Exception when web element is not found
	 */
	public void clickElement(String by, String xpath) throws Exception {
		WebElement element = null;
		try {
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				tlNode.get().fail("Unable to click on element with xpath as "+xpath, MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);

			} else {
				element.click();
				tlNode.get().pass("Clicked on element with xpath as "+xpath, MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * inputText method is used to perform entering specified value into Textbox/Textareabox 
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @param valueToBeEntered indicates the value to be entered
	 * @throws Exception when web element is not found
	 */
	public void inputText(String by, String xpath,String valueToBeEntered) throws Exception {
		try {
			WebElement element = null;
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);
			} else {
				element.sendKeys(valueToBeEntered);
				tlNode.get().pass("Entered value as "+valueToBeEntered+" on element with xpath as "+xpath, 
						MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * clearTextBox is used to clear values from specified Textbox / Textareabox
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @throws Exception when web element is not found
	 */
	public void clearTextBox(String by, String xpath) throws Exception {
		try {
			WebElement element = null;
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);
			} else {
				element.clear();
				tlNode.get().pass("Cleared value present on element with xpath as "+xpath, 
						MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}
	
	/**
	 * verifyPageTitle method is used to verify the page title of the current page
	 * @param expectedPageTitle indicates the expected page title
	 * @throws Exception when web element is not found
	 */
	public void verifyPageTitle(String expectedPageTitle) throws Exception {
		String actualPageTitle = driver.getTitle().trim();
		if(actualPageTitle.equals(expectedPageTitle)) {
			tlNode.get().pass("Verified Page Title! <br>Expected Page Title : "+expectedPageTitle+"<br> Actual Page Title : "+actualPageTitle,
					MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
		} else {
			tlNode.get().fail("Page Title verification failed! <br>Expected Page Title : "+expectedPageTitle+"<br> Actual Page Title : "+actualPageTitle,
					MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
		}
	}
	
	/**
	 * getText is used to get the text of the specified web element
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @return string
	 * @throws Exception when web element is not found
	 */
	public String getText(String by, String xpath) throws Exception {
		try {
			WebElement element = null;
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);
			} else {
				return element.getText();
			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * verifyElementDisplayed is used to verify whether element is displayed or not
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @throws Exception when web element is not found
	 */
	public void verifyElementDisplayed(String by, String xpath) throws Exception {
		try {
			WebElement element = null;
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);
			} else {
				if(element.isDisplayed()) {
					tlNode.get().pass("Element is displayed with xpath as "+xpath, 
							MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
				} else {
					tlNode.get().fail("Element is not displayed with xpath as "+xpath, 
							MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
				}
			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * scrollInto method scrolls the browser window to the specified xpath
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @throws Exception when web element is not found
	 */
	public void scrollInto(String by,String xpath) throws Exception {
		try {
			WebElement element = null;
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);
			} else {
				javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}

	}

	/**
	 * scrollToTop method scrolls the browser window to the top of the web page
	 */
	public void scrollToTop() {
		javascriptExecutor.executeScript("window.scrollTo(0,0);");
	}

	/**
	 * checkAttributeValue checks the value of the specified attribute name and compares with expected value
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 * @param attributeName indicates the attribute name
	 * @param expectedValue indicates teh expected value for the attribute name specified
	 * @throws Exception when web element is not found
	 */
	public void checkAttributeValue(String by, String xpath,String attributeName,String expectedValue) throws Exception {
		WebElement element = null;
		try {
			switch(by) {
			case "xpath" : element = driver.findElement(By.xpath(xpath)); break;
			case "id" : element = driver.findElement(By.id(xpath)); break;
			case "name" : element = driver.findElement(By.name(xpath)); break;
			}
			if(null==element) {
				tlNode.get().fail("Unable to click on element with xpath as "+xpath, MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
				throw new FrameworkException("Unable to locate the element for given xpath as "+xpath);
			} else {
				String actualValue = element.getAttribute(attributeName);
				if(actualValue.equals(expectedValue)) {
					tlNode.get().pass("Verified Attribute value as "+expectedValue+" for "+attributeName);
				} else {
					tlNode.get().fail("Mismatch with attribute values!!<br>Expected value : "+expectedValue+"<br> Actual value : "+actualValue);
				}

			}
		} catch (Exception e) {
			tlNode.get().fail(e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * waitUntilElementToBePresent will make webdriver to wait until the element is present
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 */
	public void waitUntilElementToBePresent(String by,String xpath) {
		try {
			tlNode.get().info("Waiting for element with locator '"+by+"' and value as '"+xpath+"'");
			switch(by) {
			case "xpath" : 
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath))); 
				break;
			case "id" : 
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(xpath))); 
				break;
			case "name" : 
				wait.until(ExpectedConditions.presenceOfElementLocated(By.name(xpath))); 
				break;
			}
		} catch (Exception e) {
			tlNode.get().fail("Element is not found with "+xpath);
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * waitUntilVisiblityOfElement will make webdriver to wait until the visibility of the element
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 */
	public void waitUntilVisiblityOfElement(String by,String xpath) {
		tlNode.get().info("Waiting for visibility of element with locator '"+by+"' and value as '"+xpath+"'");
		try {
			switch(by) {
			case "xpath" : 
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))); 
				break;
			case "id" : 
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(xpath))); 
				break;
			case "name" : 
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(xpath))); 
				break;
			}
		} catch (Exception e) {
			tlNode.get().fail("Element is not found with "+xpath);
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * waitUntilInvisiblityOfElement will make webdriver to wait until the element disappears
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 */
	public void waitUntilInvisiblityOfElement(String by,String xpath) {
		tlNode.get().info("Waiting for invisibility of element with locator '"+by+"' and value as '"+xpath+"'");
		try {
			switch(by) {
			case "xpath" : 
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath))); 
				break;
			case "id" : 
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(xpath))); 
				break;
			case "name" : 
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name(xpath))); 
				break;
			}
		} catch (Exception e) {
			tlNode.get().fail("Element is not found with "+xpath);
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * waitUntilElementToBeClickable will make webdriver to wait until the element is clickable
	 * @param by indicates the element locator
	 * @param xpath indicates the web element value
	 */
	public void waitUntilElementToBeClickable(String by,String xpath) {
		tlNode.get().info("Waiting for element to be clickable with locator '"+by+"' and value as '"+xpath+"'");
		try {
			switch(by) {
			case "xpath" : 
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))); 
				break;
			case "id" : 
				wait.until(ExpectedConditions.elementToBeClickable(By.id(xpath))); 
				break;
			case "name" : 
				wait.until(ExpectedConditions.elementToBeClickable(By.name(xpath))); 
				break;
			}
		} catch (Exception e) {
			tlNode.get().fail("Element is not found with "+xpath);
			throw new FrameworkException(e.getMessage());
		}
	}
	
	/**
	 * compareTwoValues method compares two string and print the result in Extent reports
	 * @param actualValue indicates the actual value to be compared
	 * @param expectedValue indicates the expected value to be compared
	 * @param fieldLabel indicates the field label name for which the comparision is made
	 */
	public void compareTwoValues(String actualValue,String expectedValue,String fieldLabel) {
		if(actualValue.equals(expectedValue)){
			tlNode.get().pass("Verified values for <b>"+fieldLabel+"</b> successfully <br>Actual value : "+actualValue+"<br>Expected Value : "+expectedValue);
		} else {
			tlNode.get().fail("Verification failed for <b>"+fieldLabel+"</b><br>Actual value : "+actualValue+"<br>Expected Value : "+expectedValue);
		}
	}

	/**
	 * getTestDataFromInputSheet method is used to fetch and return the test data from the test data sheet
	 * @return Map with Integer and Nested Map (String and String)
	 */
	public Map<Integer, Map<String, String>> getTestDataFromInputSheet() {
		String filePath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"
				+File.separator+"TestDataSheet.xlsx";

		Map<String, Integer> columnIndexMap = getAllColumnIndexFromSheet(filePath);
		Map<Integer, Map<String, String>> runMap = new HashMap<>();
		
		try (FileInputStream inputStream = new FileInputStream(new File(filePath));
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
			
			XSSFSheet sheet = workbook.getSheet("Test Data");
			
			for(int rowNo = 1;rowNo<sheet.getPhysicalNumberOfRows();rowNo++) {
				int executionColumnNo = columnIndexMap.get("Execution");
				Row row = sheet.getRow(rowNo);
				Cell cell = row.getCell(executionColumnNo);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String executionValue = cell.getStringCellValue();
				
				if(executionValue.equalsIgnoreCase("y")) {
					Map<String, String> tempMap = new HashMap<>();
					for(int columnNo = 0;columnNo<row.getPhysicalNumberOfCells();columnNo++){
						cell = row.getCell(columnNo);
						
						if(null== cell || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
							String columnName = sheet.getRow(0).getCell(columnNo).getStringCellValue().trim();
							tempMap.put(columnName, "");
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							String cellValue = cell.getStringCellValue().trim();
							String columnName = sheet.getRow(0).getCell(columnNo).getStringCellValue().trim();
							tempMap.put(columnName, cellValue);
						}
					}
					runMap.put(rowNo, tempMap);
				}
			}
			return runMap;
		} catch (Exception e) {
			tlNode.get().fail("Error in reading the test data from input sheet. Error Message : "+e.getMessage());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * getAllColumnIndexFromSheet method is used to get all column index from the specified excel sheet
	 * @param filePath indicates path of the excel sheet
	 * @return Map with String and Integer where Column name is string and column no is integer
	 */
	private Map<String, Integer> getAllColumnIndexFromSheet(String filePath) {
		Map<String, Integer> map = new HashMap<>();
		try (FileInputStream inputStream = new FileInputStream(new File(filePath));
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream)){
			
			XSSFSheet sheet = workbook.getSheet("Test Data");
			Row row = sheet.getRow(0);
			for(int i=0;i<row.getPhysicalNumberOfCells();i++) {
				Cell cell = row.getCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String columnName = cell.getStringCellValue().trim();
				map.put(columnName, i);
			}
		} catch (Exception e) {
			tlNode.get().fail("Error in reading the column index data from input sheet. Error Message : "+e.getMessage());
			throw new FrameworkException(e.getMessage());
		}
		return map;
	}


}
