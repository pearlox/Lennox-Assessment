package com.kalai.lennox.functionalLibaries;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.kalai.lennox.exception.FrameworkException;
import com.kalai.lennox.objectRepositories.HomePage;
import com.kalai.lennox.objectRepositories.SignInPage;
import com.kalai.lennox.utils.TestBase;
import com.kalai.lennox.utils.Utilities;

public class SignInPageFunctionalities extends TestBase {
	
	private Utilities utilities = new Utilities();

	/**
	 * signIntoApplication method will sign into application using userName and password provided
	 * @param userName indicates the email address
	 * @param password indicates the password
	 * @throws Exception when web element is not found
	 */
	public void signIntoApplication(String userName,String password) throws Exception {
		try {
			utilities.clickElement("xpath", HomePage.BUTTON_SIGN_IN);
			
			utilities.waitUntilVisiblityOfElement("xpath", SignInPage.LABEL_SIGN_IN);
			utilities.waitUntilElementToBePresent("xpath", SignInPage.LABEL_SIGN_IN);
			
			utilities.inputText("xpath", SignInPage.TEXT_BOX_EMAIL_ADDRESS, userName);
			utilities.inputText("xpath", SignInPage.TEXT_BOX_PASSWORD, password);
			
			utilities.clickElement("xpath", SignInPage.BUTTON_SIGN_IN);
			
			utilities.waitUntilVisiblityOfElement("xpath", HomePage.BUTTON_MENU);
			utilities.waitUntilElementToBePresent("xpath", HomePage.BUTTON_MENU);
			
			tlNode.get().pass("Signed into application successfully");
		} catch (Exception e) {
			tlNode.get().fail("Unable to sign into application",
					MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
			throw new FrameworkException(e.getMessage());
		}
	}

	/**
	 * signOut will make the user to sign out of the application
	 * @throws Exception when web element is not found
	 */
	public void signOut() throws Exception {
		utilities.clickElement("xpath", HomePage.BUTTON_PROFILE);
		
		utilities.waitUntilVisiblityOfElement("xpath", HomePage.BUTTON_LOGOUT);
		utilities.waitUntilElementToBePresent("xpath", HomePage.BUTTON_LOGOUT);
		
		utilities.clickElement("xpath", HomePage.BUTTON_LOGOUT);
		
		utilities.waitUntilVisiblityOfElement("xpath", HomePage.BUTTON_SIGN_IN);
		utilities.waitUntilElementToBePresent("xpath", HomePage.BUTTON_SIGN_IN);
		tlNode.get().pass("Signed out of the application successfully");
	}
}
