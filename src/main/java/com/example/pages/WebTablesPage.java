package com.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class WebTablesPage extends BasePage<WebTablesPage, WebTablesPage.WebTablesPageAssertion> {
    private final static String END_POINT = "/webtables";

    private final Locator addButton;
    private final Locator searchBox;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator emailInput;
    private final Locator ageInput;
    private final Locator salaryInput;
    private final Locator departmentInput;
    private final Locator submitButton;

    public WebTablesPage(Page page) {
        super(page, END_POINT);

        this.addButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add"));
        this.searchBox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Type to search"));
        this.firstNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First Name"));
        this.lastNameInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last Name"));
        this.emailInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("name@example.com"));
        this.ageInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Age"));
        this.salaryInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Salary"));
        this.departmentInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Department"));
        this.submitButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit"));
    }

    @Override
    WebTablesPageAssertion createAssertionInstance() {
        return new WebTablesPageAssertion();
    }

    @Step("Click Add button")
    public WebTablesPage clickAdd() {
        addButton.click();
        return this;
    }

    @Step("Fill registration form")
    public WebTablesPage fillRegistrationForm(String firstName, String lastName, String email, String age, String salary, String department) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        emailInput.fill(email);
        ageInput.fill(age);
        salaryInput.fill(salary);
        departmentInput.fill(department);
        return this;
    }

    @Step("Submit registration form")
    public WebTablesPage submitForm() {
        submitButton.click();
        page.locator("#registration-form-modal").waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN));
        return this;
    }

    @Step("Search for record")
    public WebTablesPage search(String text) {
        searchBox.clear();
        searchBox.fill(text);
        return this;
    }

    @Step("Edit record by email")
    public WebTablesPage editRecord(String email) {
        getRowByEmail(email).locator("[title='Edit']").click();
        return this;
    }

    @Step("Delete record by email")
    public WebTablesPage deleteRecord(String email) {
        getRowByEmail(email).locator("[title='Delete']").click();
        return this;
    }

    private Locator getRowByEmail(String email) {
        // Preference for getByRole as requested
        return page.getByRole(AriaRole.ROW).filter(new Locator.FilterOptions().setHasText(email));
    }

    public class WebTablesPageAssertion implements BasePage.Assertion {

        public void assertRecordVisible(String email) {
            assertThat(getRowByEmail(email)).isVisible();
        }

        public void assertRecordNotVisible(String email) {
            assertThat(getRowByEmail(email)).isHidden();
        }

        public void assertRecordData(String email, String firstName, String lastName, String age, String salary, String department) {
            Locator row = getRowByEmail(email);
            assertThat(row).containsText(firstName);
            assertThat(row).containsText(lastName);
            assertThat(row).containsText(age);
            assertThat(row).containsText(salary);
            assertThat(row).containsText(department);
        }
    }
}
