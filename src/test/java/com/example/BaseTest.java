package com.example;

import com.example.extensions.AllureExtension;
import com.example.extensions.TraceExtension;
import com.microsoft.playwright.*;
import io.qameta.allure.junit5.AllureJunit5;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import com.example.config.AppConfig;
import com.example.config.AppConfigManager;
import org.junit.jupiter.api.extension.Extensions;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Extensions(
        @ExtendWith({
                AllureJunit5.class,
                AllureExtension.class,
                TraceExtension.class
        })
)
@Timeout(value = 30, unit = TimeUnit.SECONDS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected final Faker faker = new Faker();

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    void launchBrowser() {
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
                .setArgs(List.of(
                        "--start-maximized"
//                        Implement for CI
//                        "--no-sandbox",
//                        "--disable-dev-shm-usage"
                )));
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

        // Start recording the trace
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        // Register the context so the Extension can find it
        TraceExtension.threadContext.set(context);

        page = context.newPage();
        // Register the page so the Extension can find it
        AllureExtension.threadPage.set(page);
    }

    @AfterEach
    void closePageAndContext() {
        if (page != null) page.close();
        if (context != null) context.close();
    }

    @AfterAll
    void closeBrowserAndPlaywright() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
