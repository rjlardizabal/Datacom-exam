package com.datacom.cucumber.catalog.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.Locale;
import java.util.Map;

public class BugFormPage {
    private final Page page;

    public BugFormPage(Page page) {
        this.page = page;
    }

    private Locator firstName() {
        return page.locator("#firstName");
    }

    private Locator lastName() {
        return page.locator("#lastName");
    }

    private Locator phone() {
        return page.locator("#phone");
    }

    private Locator country() {
        return page.locator("#countries_dropdown_menu");
    }

    private Locator email() {
        return page.locator("#emailAddress");
    }

    private Locator password() {
        return page.locator("#password");
    }

    private Locator register() {
        return page.locator("#registerBtn");
    }

    private Locator resultsSection() { return page.locator("#results-section"); }
    private Locator message()        { return page.locator("#message"); }
    private Locator resultFn()       { return page.locator("#resultFn"); }
    private Locator resultLn()       { return page.locator("#resultLn"); }
    private Locator resultPhone()    { return page.locator("#resultPhone"); }
    private Locator resultCountry()  { return page.locator("#country"); }
    private Locator resultEmail()    { return page.locator("#resultEmail"); }


    public boolean isOnSpotTheBugsPage() {
        return page.getByText("CHALLENGE - Spot the BUGS!").isVisible();
    }

    public void fillForm(Map<String, String> data) {
        if (data.containsKey("firstName")) firstName().fill(orEmpty(data.get("firstName")));
        if (data.containsKey("lastName"))  lastName().fill(orEmpty(data.get("lastName")));
        if (data.containsKey("phone"))     phone().fill(orEmpty(data.get("phone")));
        if (data.containsKey("email"))     email().fill(orEmpty(data.get("email")));
        if (data.containsKey("password"))  password().fill(orEmpty(data.get("password")));

        if (data.containsKey("country")) {
            String countryValue = data.get("country");
            if (countryValue != null && !countryValue.isBlank()) {
                country().selectOption(new SelectOption().setLabel(countryValue));
            }
        }
    }

    private static String orEmpty(String s) { return s == null ? "" : s; }


    public void submit() {
        register().click();
        waitForResultsSection();
    }
    public void waitForResultsSection() {
        resultsSection().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
    }


    public boolean successfulRegistrationDisplayed() {
        return message().isVisible() && message().innerText().contains("Successfully registered the following information");
    }

    private String extractValue(Locator locator) {
        String full = locator.textContent();
        if (full == null) return "";
        int idx = full.indexOf(':');
        return (idx >= 0 && idx + 1 < full.length())
                ? full.substring(idx + 1).trim()
                : full.trim();
    }

    public String getFirstName()   { return extractValue(resultFn()); }
    public String getLastName()    { return extractValue(resultLn()); }
    public String getPhoneNumber() { return extractValue(resultPhone()); }
    public String getCountry()     { return extractValue(resultCountry()); }
    public String getEmail()       { return extractValue(resultEmail()); }

    public boolean isRequiredFieldMessageDisplayed(String field) {
        return message().isVisible() && message().innerText().toLowerCase(Locale.ROOT).contains(field.toLowerCase(Locale.ROOT));
    }

    public boolean isShowingPasswordValidationMessage() {
        return message().isVisible() && (message().innerText().equalsIgnoreCase("The password should contain between [6,20] characters!"));
    }

    public boolean isShowingPhoneValidationMessage() {
        return message().isVisible() && (message().innerText().equalsIgnoreCase("The phone number should contain at least 10 characters!"));
    }

    public boolean validationShownForField(String field) {
        return switch (field.toLowerCase(Locale.ROOT)) {
            case "phone number" ->
                    message().isVisible() && (message().innerText().equalsIgnoreCase("The phone number should only contain digits!"));
            case "email address" ->
                    message().isVisible() && (message().innerText().equalsIgnoreCase("Email address is invalid!"));
            default -> false;
        };
    }

}
