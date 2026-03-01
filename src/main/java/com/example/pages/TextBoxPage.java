package com.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TextBoxPage extends BasePage<TextBoxPage, TextBoxPage.TextBoxPageAssertion> {
    private final static String END_POINT = "/text-box";

    private final Locator fullNameInput;
    private final Locator emailInput;
    private final Locator currentAddressTextarea;
    private final Locator permanentAddressTextarea;
    private final Locator submitButton;
    private final Locator outputDiv;
    private final Locator nameOutput;
    private final Locator emailOutput;
    private final Locator currentAddressOutput;
    private final Locator permanentAddressOutput;

    public TextBoxPage(Page page) {
        super(page, END_POINT);

        this.fullNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Full Name"));
        this.emailInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("name@example.com"));
        this.currentAddressTextarea = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Current Address"));
        this.permanentAddressTextarea = page.locator("#permanentAddress");
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
        this.outputDiv = page.locator("#output");
        this.nameOutput = outputDiv.locator("#name");
        this.emailOutput = outputDiv.locator("#email");
        this.currentAddressOutput = outputDiv.locator("#currentAddress");
        this.permanentAddressOutput = outputDiv.locator("#permanentAddress");
    }

    @Override
    TextBoxPageAssertion createAssertionInstance() {
        return new TextBoxPageAssertion();
    }

    @Step("Fill full name")
    public TextBoxPage fillFullName(String name) {
        fullNameInput.fill(name);
        return this;
    }

    @Step("Fill email")
    public TextBoxPage fillEmail(String email) {
        emailInput.fill(email);
        return this;
    }

    @Step("Fill current address")
    public TextBoxPage fillCurrentAddress(String address) {
        currentAddressTextarea.fill(address);
        return this;
    }

    public TextBoxPage fillPermanentAddress(String address) {
        permanentAddressTextarea.fill(address);
        return this;
    }

    public TextBoxPage submitForm() {
        submitButton.click();
        return this;
    }

    public String getNameOutput() {
        return nameOutput.textContent().replace("Name:", "").trim();
    }

    public String getEmailOutput() {
        return emailOutput.textContent().replace("Email:", "").trim();
    }

    public String getCurrentAddressOutput() {
        return currentAddressOutput.textContent().replace("Current Address :", "").trim();
    }

    public String getPermanentAddressOutput() {
        return permanentAddressOutput.textContent().replace("Permananet Address :", "").trim(); // Note: Typo in site ("Permananet")
    }

    public class TextBoxPageAssertion implements BasePage.Assertion {

        public void assertOutputIsVisible() {
            assertThat(outputDiv).isVisible();
        }

        public void assertOutputName(String name) {
            assertThat(nameOutput).hasText(String.format("Name:%s", name));
        }

        public void assertOutputEmail(String email) {
            assertThat(emailOutput).hasText(String.format("Email:%s", email));
        }

        public void assertOutputCurrentAddress(String currentAddress) {
            assertThat(currentAddressOutput).hasText(String.format("Current Address :%s", currentAddress));
        }

        public void assertOutputPermananetAddress(String permananetAddress) {
            assertThat(permanentAddressOutput).hasText(String.format("Permananet Address :%s", permananetAddress));
        }

        public boolean hasEmailError() {
            // The site adds 'field-error' class to invalid email input
            return emailInput.evaluate("el => el.classList.contains('field-error')").equals(true);
        }
    }
}
