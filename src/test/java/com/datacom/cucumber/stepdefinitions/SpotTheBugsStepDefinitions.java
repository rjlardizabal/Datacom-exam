package com.datacom.cucumber.stepdefinitions;

import com.datacom.cucumber.catalog.pageobjects.NavBar;
import com.datacom.cucumber.catalog.pageobjects.BugFormPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class SpotTheBugsStepDefinitions {
    NavBar navBar;
    BugFormPage bugFormPage;


    @Before
    public void setupPageObjects() {
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        bugFormPage = new BugFormPage(PlaywrightCucumberFixtures.getPage());

    }


    @Given("the Spot the Bugs Form is available to the user")
    public void theSpotTheBugsFormIsAvailableToTheUser() {
        navBar.openHomePage();
        assertTrue(bugFormPage.isOnSpotTheBugsPage());
    }

    @When("the user submits the form with:")
    public void theUserSubmitsTheFormWith(Map<String, String>  dataTable) {
        bugFormPage.fillForm(dataTable);
        bugFormPage.submit();
    }

    @Then("the submission is accepted")
    public void theSubmissionIsAccepted() {
        assertTrue(bugFormPage.successfulRegistrationDisplayed(), "Successful registration message is not displayed.");
    }


    @When("the user submits the form with {} missing and all other fields valid")
    public void theUserSubmitsTheFormWithMissingAndAllOtherFieldsValid(String field) {
        Map<String, String> data = validBaseline();
        String lowercaseField = field.toLowerCase(Locale.ROOT);
        switch (lowercaseField) {
            case "first":
            case "firstname":
            case "first name":
                data.remove("firstName");
                break;
            case "last":
            case "lastname":
            case "last name":
                data.remove("lastName");
                break;
            case "phone":
            case "phone number":
                data.remove("phone");
                break;
            case "email":
            case "email address":
                data.remove("email");
                break;
            case "password":
                data.remove("password");
                break;
            case "country":
                data.remove("country");
                break;
            default:
                throw new IllegalArgumentException("Unknown field label: " + field);
        }
        bugFormPage.fillForm(data);
        bugFormPage.submit();
    }

    private Map<String, String> validBaseline() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("firstName", "Jane");
        data.put("lastName", "Doe");
        data.put("phone", "6421234567");
        data.put("country", "New Zealand"); 
        data.put("email", "jane.doe@example.com");
        data.put("password", "Secr3t!");
        return data;
    }

    @Then("a required-field message is shown for {}")
    public void aRequiredFieldMessageIsShownFor(String field) {
        assertTrue(bugFormPage.isRequiredFieldMessageDisplayed(field));
    }


    @When("the user submits the form with phone {string} and all other fields valid")
    public void theUserSubmitsTheFormWithPhoneAndAllOtherFieldsValid(String phoneNumber) {
        Map<String, String> valid = validBaseline();
        valid.put("phone", phoneNumber);
        bugFormPage.fillForm(valid);
        bugFormPage.submit();
    }

    @Then("the form {} a phone-length validation message")
    public void theFormAPhoneLengthValidationMessage(String isShowingPhoneValidationMessage) {
        boolean shouldShow = isShowingPhoneValidationMessage.equalsIgnoreCase("shows");
        assertEquals(shouldShow, bugFormPage.isShowingPhoneValidationMessage());
    }

    @When("the user submits the form with a phone containing non-digits \\(e.g., {string}) and all other fields valid")
    public void theUserSubmitsTheFormWithAPhoneContainingNonDigitsEGAndAllOtherFieldsValid(String sample) {
        Map<String, String> valid = validBaseline();
        valid.put("phone", sample);
        bugFormPage.fillForm(valid);
        bugFormPage.submit();
    }

    @Then("a phone-format or phone-length validation message is shown")
    public void aPhoneFormatOrPhoneLengthValidationMessageIsShown() {
        assertTrue(bugFormPage.validationShownForField("Phone number"), "Expected phone validation (format/length) message");
    }

    @When("the user submits the form with password {string} and all other fields valid")
    public void theUserSubmitsTheFormWithPasswordAndAllOtherFieldsValid(String password) {
        Map<String, String> valid = validBaseline();
        valid.put("password", password);
        bugFormPage.fillForm(valid);
        bugFormPage.submit();
    }

    @Then("the form {} a password-length validation message")
    public void theFormAPasswordLengthValidationMessage(String isShowingPasswordValidationMessage) {
        boolean shouldShow = isShowingPasswordValidationMessage.equalsIgnoreCase("shows");
        assertEquals(shouldShow, bugFormPage.isShowingPasswordValidationMessage());
    }

    @When("the user submits the form with email {string} and all other fields valid")
    public void theUserSubmitsTheFormWithEmailAndAllOtherFieldsValid(String email) {
        Map<String, String> valid = validBaseline();
        valid.put("email", email);
        bugFormPage.fillForm(valid);
        bugFormPage.submit();
    }

    @Then("the form {} an email-format validation message")
    public void theFormAnEmailFormatValidationMessage(String isShowingEmailValidationMessage) {
        boolean shouldShow = isShowingEmailValidationMessage.equalsIgnoreCase("shows");
        assertEquals(shouldShow, bugFormPage.validationShownForField("Email address"));
    }


    @And("the following registration details are displayed:")
    public void theFollowingRegistrationDetailsAreDisplayed(Map<String, String> dataTable) {
        assertEquals(dataTable.getOrDefault("First Name", ""), bugFormPage.getFirstName());
        assertEquals(dataTable.getOrDefault("Last Name", ""), bugFormPage.getLastName());
        assertEquals(dataTable.getOrDefault("Phone Number", ""), bugFormPage.getPhoneNumber());
        assertEquals(dataTable.getOrDefault("Country", ""), bugFormPage.getCountry());
        assertEquals(dataTable.getOrDefault("Email", ""), bugFormPage.getEmail());
    }
}
