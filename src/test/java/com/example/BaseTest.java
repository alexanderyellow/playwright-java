package com.example;

import com.example.extensions.AllureTestWatcher;
import io.qameta.allure.Attachment;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.*;
import com.example.config.AppConfig;
import com.example.config.AppConfigManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Extensions(
        @ExtendWith({AllureJunit5.class, AllureTestWatcher.class})
)
@Timeout(300)
public abstract class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    public Page getPage() {
        return page;
    }

    @BeforeAll
    static void launchBrowser() {
        AppConfig config = AppConfigManager.INSTANCE.getConfig();
        playwright = com.microsoft.playwright.Playwright.create();

        BrowserType browserType = switch (config.getPlaywright().getBrowser().toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };

        browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(config.getPlaywright().isHeadless())
                .setSlowMo(config.getPlaywright().getSlowMo())
                .setArgs(List.of("--start-maximized")));
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = context.newPage();
    }

    @AfterEach
    void closeBrowser() {
        if (page != null) page.close();
        if (context != null) context.close();
    }

    @AfterAll
    static void closePlaywright() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public byte[] takeScreenshot()  {
        Path screenshotDir = Paths.get(System.getProperty("user.dir"), "build", "screenshots");
        try {
            Files.createDirectories(screenshotDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return page.screenshot(
                new Page.ScreenshotOptions()
                        .setPath(screenshotDir.resolve("screenshot.png"))
                        .setFullPage(true)
        );
    }

    @Attachment(value = "HTML on failure", type = "text/html")
    public String takeHtml() {
        return page.content();
    }
}
