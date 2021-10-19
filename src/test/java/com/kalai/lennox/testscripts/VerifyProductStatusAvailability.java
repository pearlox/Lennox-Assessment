package com.kalai.lennox.testscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.kalai.lennox.exception.FrameworkException;
import com.kalai.lennox.functionalLibaries.HomePageFunctionalities;
import com.kalai.lennox.functionalLibaries.ProductCatalogPageFunctionalities;
import com.kalai.lennox.functionalLibaries.SignInPageFunctionalities;
import com.kalai.lennox.utils.TestBase;
import com.kalai.lennox.utils.Utilities;

public class VerifyProductStatusAvailability extends TestBase {

	private Utilities utilities;
    private SignInPageFunctionalities signInPage;
    private HomePageFunctionalities homePageFunctionalities;
    private ProductCatalogPageFunctionalities productCatalogPageFunctionalities;

	/**
	 * Initialize and get the values from properties file which will be required for execution
	 */
	public VerifyProductStatusAvailability(){
		super();
	}

	@BeforeMethod
	public void setUp() throws Exception {
		initialization();
		utilities = new Utilities();
		signInPage = new SignInPageFunctionalities();
		homePageFunctionalities = new HomePageFunctionalities();
		productCatalogPageFunctionalities = new ProductCatalogPageFunctionalities();
		homePageFunctionalities.acceptAllCookies();
	}

	@Test
	public void verifyProductStatus() throws Exception {
		Map<Integer, Map<String,String>> runnerMap = utilities.getTestDataFromInputSheet();
		if(runnerMap.isEmpty()){
			tlNode.get().info("No test data is identified to run from excel sheet");
		} else {
			for(Entry<Integer, Map<String,String>> entrySet:runnerMap.entrySet()) {
				Map<String, String> testDataMap = entrySet.getValue();

				String testCaseID = testDataMap.get("TestCaseID");
				tlNode.set(tlTest.get().createNode(testCaseID));

				String userName = testDataMap.get("EMAILID");
				String password = testDataMap.get("Password");
				signInPage.signIntoApplication(userName, password);

				String linkName = testDataMap.get("LinkName");
				homePageFunctionalities.navigateToSpecificPage(linkName);

				String pageNavigation = testDataMap.get("PageNavigation");
				String pageTitle = testDataMap.get("PageTitle").trim();

				String previousLinkName = "";
				if(pageNavigation.contains(":::")) {
					String[] pageNavigationLinkNames = pageNavigation.split(":::");
					for(int i = 0;i<pageNavigationLinkNames.length;i++) {
						if(i==0) {
							productCatalogPageFunctionalities.navigateToCategoryLink(pageNavigationLinkNames[i].trim());
						} else {
							productCatalogPageFunctionalities.navigateToSubCategoryLink(pageNavigationLinkNames[i].trim());
							if(i>1) {
								productCatalogPageFunctionalities.waitForSubCategoryPageToLoad(previousLinkName);
							}
						}
						previousLinkName = pageNavigationLinkNames[i].trim();
					}
				} 

				utilities.verifyPageTitle(pageTitle);

				String pageDescription = testDataMap.get("PageDescription");
				productCatalogPageFunctionalities.verifyPageDescription(pageDescription);

				String productCatalog =  testDataMap.get("Catalog#");
				productCatalogPageFunctionalities.locateProduct(productCatalog);
				
				Map<String, String> productDetailsMap = productCatalogPageFunctionalities.verifyProduct(productCatalog);
				String expectedPrice = testDataMap.get("Price"); 
				String expectedModelNo = testDataMap.get("Model/Part#");
				
				String actualPrice = productDetailsMap.get("Price");
				String actualModelNo = productDetailsMap.get("Model");
				
				utilities.compareTwoValues(actualPrice, expectedPrice, "Product Price");
				utilities.compareTwoValues(actualModelNo, expectedModelNo, "Product Model/Part Number");
				
				String catalog = productDetailsMap.get("Catalog");
				String description = productDetailsMap.get("Description");
				String shippingAvailability = productDetailsMap.get("Shipping Availability");
				String storeAvailability = productDetailsMap.get("Store Availability");
				String storeName = productDetailsMap.get("Store Name");
				String addToCart = productDetailsMap.get("Add To Cart");
				
				tlNode.get().info("Product Catalog Number : "+catalog);
				tlNode.get().info("Product Description : "+description);
				tlNode.get().info("Product Shipping Availability : "+shippingAvailability);
				tlNode.get().info("Product Store Availability : "+storeAvailability);
				tlNode.get().info("Product Store Name : "+storeName);
				tlNode.get().info("Add to Cart Button is <b>"+addToCart+"</b>");
				
				signInPage.signOut();
			}

		}
	}

	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
}
