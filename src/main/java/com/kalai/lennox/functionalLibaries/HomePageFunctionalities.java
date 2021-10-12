package com.kalai.lennox.functionalLibaries;

import com.kalai.lennox.objectRepositories.HomePage;
import com.kalai.lennox.utils.TestBase;
import com.kalai.lennox.utils.Utilities;

public class HomePageFunctionalities extends TestBase {

	Utilities utilities = new Utilities();
	
	public void navigateToSpecificPage(String linkName) throws Exception {
		
		utilities.waitUntilVisiblityOfElement("xpath", HomePage.BUTTON_MENU);
		utilities.waitUntilElementToBePresent("xpath", HomePage.BUTTON_MENU);
		
		utilities.clickElement("xpath", HomePage.BUTTON_MENU);
		
		clickOnLinkName(linkName);
		waitForPageLoad(linkName);
	
	}

	private void waitForPageLoad(String linkName) {
		utilities.waitUntilVisiblityOfElement("xpath", String.format(HomePage.SUBMENU_PAGE_HEADER,linkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(HomePage.SUBMENU_PAGE_HEADER,linkName));
	}

	private void clickOnLinkName(String linkName) throws Exception {
		
		utilities.waitUntilVisiblityOfElement("xpath", String.format(HomePage.SUBMENU_NAVIGATION,linkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(HomePage.SUBMENU_NAVIGATION,linkName));
		
		utilities.clickElement("xpath", String.format(HomePage.SUBMENU_NAVIGATION,linkName));
	}
	
	public void acceptAllCookies() throws Exception {
		
		utilities.waitUntilVisiblityOfElement("xpath", HomePage.ACCEPT_ALL_COOKIES);
		utilities.waitUntilElementToBePresent("xpath", HomePage.ACCEPT_ALL_COOKIES);
		
		Thread.sleep(2000);
		utilities.clickElement("xpath", HomePage.ACCEPT_ALL_COOKIES);
	}
}
