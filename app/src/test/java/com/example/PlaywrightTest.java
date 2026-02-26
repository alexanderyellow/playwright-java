package com.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.MouseButton;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaywrightTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void prewarmDriver() {
        System.out.println("-----> BeforeAll: " + Thread.currentThread().getName());
        Playwright.create();
    }

    @BeforeEach
    void launchBrowser() {
        System.out.println("-----> BeforeEach: " + Thread.currentThread().getName());
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setSlowMo(1500));
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeBrowser() {
        System.out.println("-----> AfterEach: " + Thread.currentThread().getName());
        context.close();
        browser.close();
        playwright.close();
    }

    @Test
    void testTextBoxSubmission() {
        System.out.println("-----> Test1: " + Thread.currentThread().getName());
        page.navigate("https://demoqa.com/text-box");
        page.fill("#userName", "John Doe");
        page.fill("#userEmail", "john@example.com");
        page.fill("#currentAddress", "123 Main St");
        page.fill("#permanentAddress", "456 Side St");
        page.click("#submit");

        assertTrue(page.locator("#name").textContent().contains("John Doe"));
        assertTrue(page.locator("#email").textContent().contains("john@example.com"));
    }

    @Test
    void testCheckBoxSelection() {
        page.navigate("https://demoqa.com/checkbox");
        page.click("button[title='Expand all']");
        page.locator("label[for='tree-node-desktop']").click();

        assertTrue(page.locator("#result").textContent().contains("desktop"));
    }

    @Test
    void testRadioButtonInteraction() {
        System.out.println("-----> Test2: " + Thread.currentThread().getName());
        page.navigate("https://demoqa.com/radio-button");
        page.locator("label[for='impressiveRadio']").click();

        assertEquals("Impressive", page.locator("span.text-success").textContent());
    }

    @Test
    void testWebTables() {
        page.navigate("https://demoqa.com/webtables");
        page.click("#addNewRecordButton");
        page.fill("#firstName", "Jane");
        page.fill("#lastName", "Smith");
        page.fill("#userEmail", "jane@example.com");
        page.fill("#age", "30");
        page.fill("#salary", "50000");
        page.fill("#department", "IT");
        page.click("#submit");

        assertTrue(page.locator(".rt-tbody").textContent().contains("Jane"));
        assertTrue(page.locator(".rt-tbody").textContent().contains("Smith"));
    }

    @Test
    void testButtons() {
        page.navigate("https://demoqa.com/buttons");
        page.dblclick("#doubleClickBtn");
        assertEquals("You have done a double click",
                page.locator("#doubleClickMessage").textContent());

        page.click("#rightClickBtn", new Page.ClickOptions().setButton(MouseButton.RIGHT));
        assertEquals("You have done a right click",
                page.locator("#rightClickMessage").textContent());
    }
}
