package com.kalai.lennox.functionalLibaries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.kalai.lennox.objectRepositories.ProductCatalogPage;
import com.kalai.lennox.utils.TestBase;
import com.kalai.lennox.utils.Utilities;

public class ProductCatalogPageFunctionalities extends TestBase{

	private Utilities utilities = new Utilities();

	/**
	 * navigateToCategoryLink method navigates to the specified link under Main Menu
	 * @param pageNavigationLinkName indicates the link name to be navigated
	 * @throws Exception when web element is not found
	 */
	public void navigateToCategoryLink(String pageNavigationLinkName) throws Exception {
		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));

		utilities.scrollInto("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));
		utilities.clickElement("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));

		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));
	}

	/**
	 * navigateToSubCategoryLink method navigates to the specified link after navigating to the Category page
	 * @param pageNavigationLinkName indicates the link name to be navigated
	 * @throws Exception when web element is not found
	 */
	public void navigateToSubCategoryLink(String pageNavigationLinkName) throws Exception {

		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));

		utilities.scrollInto("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));
		utilities.clickElement("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));

		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));

	}

	/**
	 * waitForSubCategoryPageToLoad method will wait for the page to get load
	 * @param pageNavigationLink indicates the link name where the page got navigated
	 */
	public void waitForSubCategoryPageToLoad(String pageNavigationLink) {
		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.SUB_CATERGORY_BREADCRUMB,pageNavigationLink));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.SUB_CATERGORY_BREADCRUMB,pageNavigationLink));
	}

	/**
	 * verifyPageDescription method will verify if the Product description displayed matches with test data
	 * @param pageDescription indicates the description for the Product to be found
	 * @throws Exception when web element is not found
	 */
	public void verifyPageDescription(String pageDescription) throws Exception {
		String actualPageDescription = utilities.getText("xpath", ProductCatalogPage.DESCRIPTION_LABEL);
		if(actualPageDescription.equals(pageDescription)) {
			tlNode.get().pass("Verified Page description! <br>Expected Page Description : "+pageDescription+
							"<br> Actual Page Description : "+actualPageDescription,
					MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
		} else {
			tlNode.get().fail("Page description verification failed! <br>Expected Page Description : "+pageDescription+
							"<br> Actual Page Description : "+actualPageDescription,
					MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
		}
	}

	/**
	 * locateProduct method will search for the products and navigates to the next page until the product is found
	 * @param productCatalog indicates the product catalog which needs to be located
	 * @throws Exception when web element is not found
	 */
	public void locateProduct(String productCatalog) throws Exception {

		String currentPageNumber = utilities.getText("xpath", ProductCatalogPage.CURRENT_PAGE_NUMBER);
		List<WebElement> productCategoryWebElementList = driver.findElements(By.xpath(String.format(ProductCatalogPage.PRODUCT_CATALOG, productCatalog)));

		while(productCategoryWebElementList.isEmpty()) {

			utilities.scrollInto("xpath", ProductCatalogPage.NEXT_PAGE_BUTTON);
			utilities.clickElement("xpath", ProductCatalogPage.NEXT_PAGE_BUTTON);

			utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.PAGE_NUMBER,Integer.valueOf(currentPageNumber)+1));
			utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.PAGE_NUMBER,Integer.valueOf(currentPageNumber)+1));

			currentPageNumber = utilities.getText("xpath", ProductCatalogPage.CURRENT_PAGE_NUMBER);
			productCategoryWebElementList = driver.findElements(By.xpath(String.format(ProductCatalogPage.PRODUCT_CATALOG, productCatalog)));
		}

		utilities.scrollInto("xpath", String.format(ProductCatalogPage.PRODUCT_CATALOG, productCatalog));
		tlNode.get().pass("Product is located at Page No "+currentPageNumber,
				MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
	}

	/**
	 * verifyProduct method will fetch the value for the product deatils and return the values in map
	 * @param productCatalog indicates the product catalog for which details to be verified
	 * @return Map with Product details
	 * @throws Exception when web element is not found
	 */
	public Map<String, String> verifyProduct(String productCatalog) throws Exception {
		
		String productDetails = utilities.getText("xpath", String.format(ProductCatalogPage.PRODUCT_DETAILS, productCatalog));
		String productCatalogName = productDetails.substring(productDetails.indexOf("Cat #")+6,productDetails.indexOf("Model")).trim();
		String productModelName = productDetails.substring(productDetails.lastIndexOf(":")+1).trim();
		
		String productDescription = utilities.getText("xpath", String.format(ProductCatalogPage.PRODUCT_DESCRIPTION, productCatalog));
		String price = utilities.getText("xpath", String.format(ProductCatalogPage.PRODUCT_PRICE, productCatalog));
		String shippingAvailability = utilities.getText("xpath", String.format(ProductCatalogPage.PRODUCT_SHIPPING_AVAILABILITY, productCatalog));
		String storeAvailability = utilities.getText("xpath", String.format(ProductCatalogPage.PRODUCT_LOCAL_AVAILABILITY, productCatalog));

		String storeDetails = utilities.getText("xpath", String.format(ProductCatalogPage.PRODUCT_PICK_UP_STORE, productCatalog));
		String storeName = storeDetails.substring(storeDetails.indexOf(":")+1,storeDetails.indexOf("Check")).trim();

		boolean status = driver.findElement(By.xpath(String.format(ProductCatalogPage.PRODUCT_ADD_TO_CART_BUTTON, productCatalog))).isEnabled();
		String addToCart = "";
		if(status) 
			addToCart = "Enabled";
		else 
			addToCart = "Disabled";
		
		Map<String, String> productMap = new HashMap<String, String>();
		productMap.put("Catalog", productCatalogName);
		productMap.put("Model", productModelName);
		productMap.put("Description", productDescription);
		productMap.put("Price", price);
		productMap.put("Shipping Availability", shippingAvailability);
		productMap.put("Store Availability", storeAvailability);
		productMap.put("Store Name", storeName);
		productMap.put("Add To Cart", addToCart);
		return productMap;
	}
}
