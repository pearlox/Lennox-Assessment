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

	Utilities utilities = new Utilities();

	public void navigateToCategoryLink(String pageNavigationLinkName) throws Exception {
		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));

		utilities.scrollInto("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));
		utilities.clickElement("xpath", String.format(ProductCatalogPage.CATEGORY_LINK,pageNavigationLinkName));

		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));
	}

	public void navigateToSubCategoryLink(String pageNavigationLinkName) throws Exception {

		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));

		utilities.scrollInto("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));
		utilities.clickElement("xpath", String.format(ProductCatalogPage.SUB_CATEGORY_LINK,pageNavigationLinkName));

		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.HEARDER_CATEGORY_PAGE,pageNavigationLinkName));

		System.out.println(pageNavigationLinkName+" : "+driver.getTitle());
	}

	public void waitForSubCategoryPageToLoad(String pageNavigationLink) {
		utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.SUB_CATERGORY_BREADCRUMB,pageNavigationLink));
		utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.SUB_CATERGORY_BREADCRUMB,pageNavigationLink));
	}

	public void verifyPageDescription(String pageDescription) throws Exception {
		String actualPageDescription = utilities.getText("xpath", ProductCatalogPage.DESCRIPTION_LABEL);
		if(actualPageDescription.equals(pageDescription)) {
			tlNode.get().pass("Verified Page description! <br>Expected Page Description : "+pageDescription+"<br> Actual Page Description : "+actualPageDescription,
					MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
		} else {
			tlNode.get().fail("Page description verification failed! <br>Expected Page Description : "+pageDescription+"<br> Actual Page Description : "+actualPageDescription,
					MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
		}
	}

	public void locateProduct(String productCatalog) throws Exception {

		String currentPageNumber = utilities.getText("xpath", ProductCatalogPage.CURRENT_PAGE_NUMBER);
		List<WebElement> productCategoryWebElementList = driver.findElements(By.xpath(String.format(ProductCatalogPage.PRODUCT_CATALOG, productCatalog)));

		System.out.println(currentPageNumber+" "+productCategoryWebElementList.isEmpty());
		while(productCategoryWebElementList.isEmpty()) {

			utilities.scrollInto("xpath", ProductCatalogPage.NEXT_PAGE_BUTTON);
			utilities.clickElement("xpath", ProductCatalogPage.NEXT_PAGE_BUTTON);

			utilities.waitUntilVisiblityOfElement("xpath", String.format(ProductCatalogPage.PAGE_NUMBER,Integer.valueOf(currentPageNumber)+1));
			utilities.waitUntilElementToBePresent("xpath", String.format(ProductCatalogPage.PAGE_NUMBER,Integer.valueOf(currentPageNumber)+1));

			currentPageNumber = utilities.getText("xpath", ProductCatalogPage.CURRENT_PAGE_NUMBER);
			productCategoryWebElementList = driver.findElements(By.xpath(String.format(ProductCatalogPage.PRODUCT_CATALOG, productCatalog)));
			System.out.println(currentPageNumber+" "+productCategoryWebElementList.isEmpty());
		}

		utilities.scrollInto("xpath", String.format(ProductCatalogPage.PRODUCT_CATALOG, productCatalog));
		tlNode.get().pass("Product is located at Page No "+currentPageNumber,
				MediaEntityBuilder.createScreenCaptureFromPath(utilities.takeScreenshot()).build());
	}

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
