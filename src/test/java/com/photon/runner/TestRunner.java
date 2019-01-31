package com.photon.runner;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.vimalselvam.cucumber.listener.ExtentProperties;
import com.vimalselvam.cucumber.listener.Reporter;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(features = "src/test/java/com/photon/feature", plugin = { "html:target/cucumber-htmlreport",
		"json:target/cucumber-report.json",
		"com.vimalselvam.cucumber.listener.ExtentCucumberFormatter::output/report.html" }, glue = "com/photon/stepDefinition", tags = {
				"@SearchText" }, monochrome = true)
public class TestRunner extends AbstractTestNGCucumberTests {

	@BeforeClass
	public static void setup() {
	}
	
	@AfterClass
    public static void teardown() {
        Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
        Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Mac OSX");
        Reporter.setTestRunnerOutput("Sample test runner output message");
    }

}
