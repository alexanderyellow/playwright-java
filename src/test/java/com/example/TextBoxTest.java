package com.example;

import com.example.pages.TextBoxPage;
import org.junit.jupiter.api.Test;

public class TextBoxTest extends BaseTest {
    String name = faker.name().fullName();
    String email = faker.internet().emailAddress();
    String currentAddress = faker.address().fullAddress();
    String permanentAddress = faker.address().fullAddress();

    @Test
    public void testSuccessfulFormSubmission() {
        TextBoxPage textBoxPage = new TextBoxPage(page);
        textBoxPage
                .navigate()
                .fillFullName(name)
                .fillEmail(email)
                .fillCurrentAddress(currentAddress)
                .fillPermanentAddress(permanentAddress)
                .submitForm()
                .softAssert(
                        TextBoxPage.TextBoxPageAssertion::assertOutputIsVisible,
                        assertion -> assertion.assertOutputName(name),
                        assertion -> assertion.assertOutputEmail(email),
                        assertion -> assertion.assertOutputCurrentAddress(currentAddress),
                        assertion -> assertion.assertOutputPermananetAddress(permanentAddress)
                );
    }
}
