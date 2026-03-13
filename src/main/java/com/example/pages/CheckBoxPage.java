package com.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckBoxPage extends BasePage<CheckBoxPage, CheckBoxPage.CheckBoxPageAssertion> {
    private final static String END_POINT = "/checkbox";

    private final Locator resultText;

    public CheckBoxPage(Page page) {
        super(page, END_POINT);
        this.resultText = page.locator("#result");
    }

    @Override
    CheckBoxPageAssertion createAssertionInstance() {
        return new CheckBoxPageAssertion();
    }

    @Step("Expand checkbox with label: {label}")
    public CheckBoxPage expand(String label) {
        // Find the switcher for the specific label. 
        // In the current layout, it's a sibling of the checkbox/title.
        page.locator("div[role='treeitem']")
            .filter(new Locator.FilterOptions().setHas(page.getByText(label, new Page.GetByTextOptions().setExact(true))))
            .locator(".rc-tree-switcher")
            .click();
        return this;
    }

    @Step("Check checkbox by label: {label}")
    public CheckBoxPage check(String label) {
        page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(label)).check();
        return this;
    }

    @Step("Uncheck checkbox by label: {label}")
    public CheckBoxPage uncheck(String label) {
        page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(label)).uncheck(new Locator.UncheckOptions().setForce(true));
        return this;
    }

    public List<String> getSelectedItems() {
        return resultText.locator(".text-success").allTextContents();
    }

    public class CheckBoxPageAssertion implements BasePage.Assertion {

        public void assertResultContains(String... items) {
            for (String item : items) {
                assertThat(resultText).containsText(item);
            }
        }

        public void assertNoResult() {
            assertThat(resultText).isHidden();
        }
    }
}
