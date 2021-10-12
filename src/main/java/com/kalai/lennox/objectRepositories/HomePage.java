package com.kalai.lennox.objectRepositories;

public class HomePage {

	public static final String BUTTON_SIGN_IN = "//a[@id='samlSignInLink']";
	public static final String BUTTON_MENU = "//a[contains(@href,'navigation')]//i";
	public static final String SUBMENU_NAVIGATION = "//div[contains(@class,'v2-hamburger-menu')]//a[contains(text(),'%s')]";
	public static final String SUBMENU_PAGE_HEADER = "//h1[contains(text(),'%s')]";
	public static final String ACCEPT_ALL_COOKIES = "//button[contains(@id,'onetrust-accept-btn-handler')]"; 
	public static final String BUTTON_PROFILE = "//div[@class='login-div']//button";
	public static final String BUTTON_LOGOUT = "//a[@id='logout_id']";
}
