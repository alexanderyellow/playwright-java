package com.example.elements;

import com.example.pages.TextBoxPage;
import org.junit.jupiter.api.Test;
import com.example.BaseTest;

public class TextBoxTest extends BaseTest {

    private final static String NAME = "John Doe";
    private final static String EMAIL = "john.doe@example.com";
    private final static String CURRENT_ADDRESS = "123 Main St";
    private final static String PERMANENT_ADDRESS = "456 Elm St";

    @Test
    public void testSuccessfulFormSubmission() {
        TextBoxPage textBoxPage = new TextBoxPage(page);
        textBoxPage
                .navigate()
                .fillFullName(NAME)
                .fillEmail(EMAIL)
                .fillCurrentAddress(CURRENT_ADDRESS)
                .fillPermanentAddress(PERMANENT_ADDRESS)
                .submitForm()
                .softAssert(
                        TextBoxPage.TextBoxPageAssertion::assertOutputIsVisible,
                        assertion -> assertion.assertOutputName(NAME),
                        assertion -> assertion.assertOutputEmail(EMAIL),
                        assertion -> assertion.assertOutputCurrentAddress(CURRENT_ADDRESS),
                        assertion -> assertion.assertOutputPermananetAddress(PERMANENT_ADDRESS)
                );
    }
}
