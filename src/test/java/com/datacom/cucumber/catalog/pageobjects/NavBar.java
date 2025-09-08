package com.datacom.cucumber.catalog.pageobjects;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Open the home page")
    public void openHomePage() {
        page.navigate("https://qa-practice.netlify.app/bugs-form");
    }


}
