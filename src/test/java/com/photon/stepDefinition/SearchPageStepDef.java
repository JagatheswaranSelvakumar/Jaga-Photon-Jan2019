package com.photon.stepDefinition;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import com.photon.framework.CommonLibrary;
import com.photon.framework.SeleniumUtils;
import com.photon.stepLibrary.SearchPageLibrary;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SearchPageStepDef extends SeleniumUtils {

	public SearchPageStepDef() throws ConfigurationException, IOException {
		super();
	}

	@Given("^user launch the google application")
	public void user_launch_the_google_application() throws Exception {
		SearchPageLibrary.launchingApplication();
	}

	@When("^user search \"([^\"]*)\" text$")
	public void user_search_text(String searchText) throws Exception {
		SearchPageLibrary.searchText(searchText);
	}

	@Then("^user verifies the search result page is displayed$")
	public void user_verifies_the_search_result_page_is_displayed() throws Exception {
		SearchPageLibrary.verifySearchResultPage();
	}

	@After
	public static void closeBrowser() {
		try {
			CommonLibrary.closeBrowser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
