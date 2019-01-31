package com.photon.framework;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class CommonLibrary {

	public static Configuration config = null;
	public static WebDriver webDriver = null;
	public static AndroidDriver<WebElement> androidDriver = null;
	public static IOSDriver<WebElement> iOSDriver = null;
	public static String browserName;
	public static String platformName;
	public static int watingForElement;
	public static String deviceID;
	public static String deviceName;
	public static String platformVersion;
	public static String appiumServerUrl;
	public static WebElement element = null;
	public static String methodName;
	public static BrowserMobProxy proxy;
	public static DesiredCapabilities caps;
	public static File chromeDriverPath = null;
	String sFileName = "F:/SeleniumEasy.har";

	public static Log log = LogFactory.getLog(CommonLibrary.class);

	public CommonLibrary() throws ConfigurationException, IOException {
		try {
			DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder(
					"src/test/resources/config/config.xml");
			config = builder.getConfiguration();
			browserName = config.getString("browserName");
			platformName = config.getString("platform");
			deviceID = config.getString("deviceID");
			deviceName = config.getString("deviceName");
			platformVersion = config.getString("platformVersion");
			appiumServerUrl = config.getString("appiumServerUrl");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static void initBrowser() {
		try {
			if (config.getString("breakPoint").equalsIgnoreCase("Desktop")) {
				switch (browserName) {
				case "FIREFOX":
					initiating_Firefox();
					break;
				case "CHROME":
					start_Har_Server("joann");
					initiating_Chrome();
					break;
				case "IE":
					initiating_IE();
					break;
				case "EDGE":
					initiating_Edge();
					break;
				default:
					log.error("Given browser option is Invalid ");
					break;
				}
				webDriver.get(config.getString("applicationURL"));
				webDriver.manage().timeouts().implicitlyWait(watingForElement, TimeUnit.SECONDS);
				webDriver.manage().window().maximize();
			} else if (config.getString("breakPoint").equalsIgnoreCase("Mobile")
					|| config.getString("breakPoint").equalsIgnoreCase("Tablet")) {
				switch (browserName) {
				case "CHROME":
					initializeAndroidMobileBrowserCapabilities();
					webDriver = new AndroidDriver<AndroidElement>(new URL(appiumServerUrl), caps);
					break;
				case "SAFARI":
					initializeiOSMobileBrowserCapabilities();
					webDriver = new IOSDriver<IOSElement>(new URL(appiumServerUrl), caps);
					break;
				default:
					log.error("Given browser option is Invalid ");
					break;

				}
				webDriver.get(config.getString("applicationURL"));
				webDriver.manage().timeouts().implicitlyWait(watingForElement, TimeUnit.SECONDS);
			} else {
				log.error("browser name is Invalid ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void initiating_Firefox() {
		try {
			if (platformName.equalsIgnoreCase("Windows")) {
				File firefoxDriverPath = new File("drivers/geckodriver.exe");
				System.setProperty("webdriver.gecko.driver", firefoxDriverPath.getAbsolutePath());
			} else if (platformName.equalsIgnoreCase("Mac")) {
			}
			webDriver = new FirefoxDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void initiating_Chrome() {
		File chromeDriverPath = null;
		try {
			if (platformName.equalsIgnoreCase("Windows")) {
				chromeDriverPath = new File("drivers/windows/chromedriver.exe");
			} else if (platformName.equalsIgnoreCase("Mac")) {
				chromeDriverPath = new File("drivers/mac/chromedriver");
			}
			System.setProperty("webdriver.chrome.driver", chromeDriverPath.getAbsolutePath());
			webDriver = new ChromeDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public static void initiating_IE() {
		try {
			File IEDriverPath = new File("drivers/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", IEDriverPath.getAbsolutePath());
			DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			webDriver = new InternetExplorerDriver(cap);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void initiating_Edge() {
		try {
			File EdgeDriverPath = new File("drivers/MicrosoftWebDriver.exe");
			System.setProperty("webdriver.edge.driver", EdgeDriverPath.getAbsolutePath());
			webDriver = new EdgeDriver();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void closeBrowser() {
		try {
			if (webDriver != null) {
				webDriver.quit();
			} else if (androidDriver != null) {
				androidDriver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void start_Har_Server(String Harname) {
		try {
			proxy = new BrowserMobProxyServer();
			proxy.start(0);
			Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
			proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
			proxy.newHar(Harname);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write_Har() {
		try {
			String chromeDriverPath = System.getProperty("user.dir") + "/har/joann_web.har";
			Har har = proxy.getHar();
			File harFile = new File(chromeDriverPath);
			try {
				har.writeTo(harFile);
			} catch (IOException ex) {
				System.out.println(ex.toString());
				System.out.println("Could not find file " + chromeDriverPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static DesiredCapabilities initializeAndroidMobileBrowserCapabilities() {
		caps = new DesiredCapabilities();
		caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		caps.setCapability(MobileCapabilityType.PLATFORM_NAME, config.getString("platformName"));
		caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		caps.setCapability(MobileCapabilityType.UDID, deviceID);
		caps.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
		caps.setCapability(MobileCapabilityType.NO_RESET, false);
		caps.setCapability("--session-override", true);
		caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
		return caps;
	}

	public static DesiredCapabilities initializeiOSMobileBrowserCapabilities() {
		caps = new DesiredCapabilities();
		caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
		caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		caps.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
		caps.setCapability(MobileCapabilityType.UDID, deviceID);
		caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		caps.setCapability("xcodeOrgId", "93BZQ9DWV3");
		caps.setCapability("xcodeSigningId", "iPhone Developer");
		caps.setCapability("--session-override", true);
		caps.setCapability(MobileCapabilityType.NO_RESET, true);
		caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
		caps.setCapability("startIWDP", true);
		caps.setCapability("usePrebuiltWDA", true);
		caps.setCapability("showXcodeLog", true);
		caps.setCapability("clearSystemFiles", true);
		caps.setCapability("safariInitialUrl", "https://www.google.com");
		caps.setCapability("webkitResponseTimeout", 10000);
		return caps;
	}

}
