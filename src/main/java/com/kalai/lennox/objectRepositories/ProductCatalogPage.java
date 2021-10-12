package com.kalai.lennox.objectRepositories;

public class ProductCatalogPage {
	public static final String CATEGORY_LINK = "//a[@title='%s']";
	public static final String HEARDER_CATEGORY_PAGE = "//h1[contains(text(),'%s')]";
	public static final String SUB_CATEGORY_LINK = "//ul[@class='menu filters']//a[contains(text(),'%s')]";
	public static final String DESCRIPTION_LABEL = "//div[@class='description']//p";
	public static final String SUB_CATERGORY_BREADCRUMB = "//nav[@class='cirrus']//li//a[contains(text(),'%s')]";
	public static final String CURRENT_PAGE_NUMBER = "//ul[@class='pagination']//li[@class='active']";
	public static final String NEXT_PAGE_BUTTON = "//li[@class='next']//a";
	public static final String PAGE_NUMBER = "//ul[@class='pagination']//li[@class='active']//*[contains(text(),'%s')]";
	
	public static final String PRODUCT_CATALOG = "//a[@data-product-code='%s']";
	public static final String PRODUCT_DETAILS = "//a[@data-product-code='%s']//div[@class='sku']";
	public static final String PRODUCT_DESCRIPTION = "//a[@data-product-code='%s']//h2";
	public static final String PRODUCT_PRICE = "//a[@data-product-code='%s']//div[@class='price']//p[@class='your-price']";
	public static final String PRODUCT_SHIPPING_AVAILABILITY = "//a[@data-product-code='%s']/..//div[@class='ship-to-availability availability']";
	public static final String PRODUCT_LOCAL_AVAILABILITY = "//a[@data-product-code='%s']/..//div[@class='local-availability availability']";
	public static final String PRODUCT_PICK_UP_STORE = "//a[@data-product-code='%s']/..//div[@class='pickup']";
	public static final String PRODUCT_ADD_TO_CART_BUTTON = "//a[@data-product-code='%s']/..//button";
	
}
