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

    public PlaywrightTest() {
        System.out.println("-----> Constructor: " + Thread.currentThread().getName());
    }

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
                        .setHeadless(false)
                        .setArgs(java.util.Arrays.asList("--start-maximized"))
                        .setSlowMo(1500));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
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
    void testRadioButtonInteraction() {
        System.out.println("-----> Test2: " + Thread.currentThread().getName());
        page.navigate("https://demoqa.com/radio-button");
        page.locator("label[for='impressiveRadio']").click();

        assertEquals("Impressive", page.locator("span.text-success").textContent());
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
