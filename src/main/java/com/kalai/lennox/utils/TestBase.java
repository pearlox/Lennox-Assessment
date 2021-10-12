package com.kalai.lennox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.kalai.lennox.Reporting.ExtentManager;
import com.kalai.lennox.exception.FrameworkException;

public class TestBase {

	public static WebDriver driver;
	public static Properties prop;
	public static ExtentReports extent;
	public static ThreadLocal<ExtentTest> tlTest = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<ExtentTest> tlNode = new ThreadLocal<ExtentTest>();

	/**
	 * Constructor to load properties from config.properties file
	 */
	public TestBase() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+ "/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeSuite
	public void beforeSuite() {

		File resultDir = new File(System.getProperty("user.dir") +File.separator+"Results");
		if(!resultDir.exists()){
			resultDir.mkdir();
		}

		File screenshotsDir = new File(System.getProperty("user.dir") +File.separator+"Screenshots");
		if(!screenshotsDir.exists()){
			screenshotsDir.mkdir();
		}

		extent = ExtentManager.createInstance(System.getProperty("user.dir") +File.separator+"Results"+File.separator+"testReport.html");
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +File.separator+"Results"+File.separator+"testReport.html");
		extent.attachReporter(htmlReporter);
	}

	@BeforeMethod
	public synchronized void beforeMethod(Method method) {
		ExtentTest parent = extent.createTest(getClass().getName());
		tlTest.set(parent);
		ExtentTest child = tlTest.get().createNode(method.getName());
		tlNode.set(child);
	}

	@AfterMethod
	public synchronized void afterMethod() {
		extent.flush();
	}

	/**
	 * Initialization of driver is done
	 */
	public static void initialization(){
		String browserName = prop.getProperty("browser");
		String driverPath = "";

		switch(browserName) {
			case "chrome":
				driverPath = System.getProperty("user.dir")+File.separator+"Webdrivers"+File.separator+"chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", driverPath);
				driver = new ChromeDriver();
				break;
				
			case "firefox":
				driverPath = System.getProperty("user.dir")+File.separator+"Webdrivers"+File.separator+"geckodriver.exe";
				System.setProperty("webdriver.gecko.driver", driverPath);	
				driver = new FirefoxDriver(); 
				break;
				
			case "ie":
				driverPath = System.getProperty("user.dir")+File.separator+"Webdrivers"+File.separator+"IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", driverPath);
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				driver = new InternetExplorerDriver(ieCapabilities); 
				break;
	
			default : throw new FrameworkException("Incorrect browser name provided!!");
		} 

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();

		driver.manage().timeouts().pageLoadTimeout(Utilities.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Utilities.IMPLICIT_WAIT, TimeUnit.SECONDS);

		driver.get(prop.getProperty("url"));

	}

	public ExtentTest createNode(String node) {
		ExtentTest extentTest = tlNode.get().createNode(node);
		return extentTest;
	}

	public static void reportLog(String desc, Status status) {
		if(status.equals(Status.PASS))
			tlNode.get().pass(desc);
		else if(status.equals(Status.FAIL))
			tlNode.get().fail(desc);
	}

}
