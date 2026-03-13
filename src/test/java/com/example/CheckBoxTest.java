package com.example;

import com.example.pages.CheckBoxPage;
import org.junit.jupiter.api.Test;

public class CheckBoxTest extends BaseTest {

    @Test
    public void testCheckHomeCheckbox() {
        CheckBoxPage checkBoxPage = new CheckBoxPage(page);
        checkBoxPage
                .navigate()
                .check("Home")
                .softAssert(
                        assertion -> assertion.assertResultContains("home", "desktop", "notes", "commands", "documents", "workspace", "react", "angular", "veu", "office", "public", "private", "classified", "general", "downloads", "wordFile", "excelFile")
                );
    }

    @Test
    public void testCheckSpecificSubItem() {
        CheckBoxPage checkBoxPage = new CheckBoxPage(page);
        checkBoxPage
                .navigate()
                .expand("Home")
                .check("Desktop")
                .softAssert(
                        assertion -> assertion.assertResultContains("desktop", "notes", "commands")
                );
    }

    @Test
    public void testUncheckCheckbox() {
        CheckBoxPage checkBoxPage = new CheckBoxPage(page);
        checkBoxPage
                .navigate()
                .check("Home")
                .uncheck("Home")
                .softAssert(
                        CheckBoxPage.CheckBoxPageAssertion::assertNoResult
                );
    }
}
