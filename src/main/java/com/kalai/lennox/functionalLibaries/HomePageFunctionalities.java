package com.kalai.lennox.functionalLibaries;

import com.kalai.lennox.objectRepositories.HomePage;
import com.kalai.lennox.utils.TestBase;
import com.kalai.lennox.utils.Utilities;

public class HomePageFunctionalities extends TestBase {

	private Utilities utilities = new Utilities();

	/**
	 *
	 * @param linkName specifies the link name to be navigated
	 * @throws Exception when web element is not found
	 */
	public void navigateToSpecificPage(String linkName) throws Exception {
		
		utilities.waitUntilVisiblityOfElement("xpath", HomePage.BUTTON_MENU);
		utilities.waitUntilElementToBePresent("xpath", HomePage.BUTTON_MENU);
		
		utilities.clickElement("xpath", HomePage.BUTTON_MENU);
		
		clickOnLinkName(linkName);
		waitForPageLoad(linkName);
	
	}

	/**
	 *
	 * @param linkName specifies the Page name
	 */
	private void waitForPageLoad(String linkName) {
		utilities.waitUntilVisiblityOfElement("xpath", String.format(HomePage.SUBMENU_PAGE_HEADER,linkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(HomePage.SUBMENU_PAGE_HEADER,linkName));
	}

	/**
	 *
	 * @param linkName specifies the page name
	 * @throws Exception when web element is not found
	 */
	private void clickOnLinkName(String linkName) throws Exception {
		
		utilities.waitUntilVisiblityOfElement("xpath", String.format(HomePage.SUBMENU_NAVIGATION,linkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(HomePage.SUBMENU_NAVIGATION,linkName));
		
		utilities.clickElement("xpath", String.format(HomePage.SUBMENU_NAVIGATION,linkName));
	}

	/**
	 * acceptAllCookies method will click on the Accept All cookies button
	 * @throws Exception when web element is not found
	 */
	public void acceptAllCookies() throws Exception {
		
		utilities.waitUntilVisiblityOfElement("xpath", HomePage.ACCEPT_ALL_COOKIES);
		utilities.waitUntilElementToBePresent("xpath", HomePage.ACCEPT_ALL_COOKIES);
		
		Thread.sleep(2000);
		utilities.clickElement("xpath", HomePage.ACCEPT_ALL_COOKIES);
	}
}
