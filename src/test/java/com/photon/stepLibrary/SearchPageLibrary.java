package com.photon.stepLibrary;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;

import com.photon.framework.SeleniumUtils;
import com.photon.pageObject.SearchPage;

public class SearchPageLibrary extends SeleniumUtils {

	public SearchPageLibrary() throws ConfigurationException, IOException {
		super();
	}

	public static void launchingApplication() throws Exception {
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			initBrowser();
		} catch (Exception e) {
			takeScreenShot(methodName, webDriver);
			throw new Exception(e.getMessage());
		}
	}

	public static void searchText(String searchText) throws Exception {
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			clearAndEnterText(SearchPage.search_TXT, searchText, webDriver);
		} catch (Exception e) {
			takeScreenShot(methodName, webDriver);
			throw new Exception(e.getMessage());
		}
	}

	public static void verifySearchResultPage() throws Exception {
		methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		try {
			if (!isElementPresentVerification(SearchPage.search_TXT, webDriver)) {
				throw new Exception("Not able to find the Search Result Page");
			}
		} catch (Exception e) {
			takeScreenShot(methodName, webDriver);
			throw new Exception(e.getMessage());
		}
	}
}
