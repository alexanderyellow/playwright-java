package com.example.extensions;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TraceExtension implements TestExecutionExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(TraceExtension.class);
    // This holds the context unique to the current thread
    public static ThreadLocal<BrowserContext> threadContext = new ThreadLocal<>();

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        BrowserContext playwrightContext = threadContext.get();
        if (playwrightContext != null) {
            String testName = context.getDisplayName().replaceAll(" ", "_");
            Path tracePath = Paths.get("build/playwright-traces/" + testName + ".zip");

            try {
                Files.createDirectories(tracePath.getParent());
                playwrightContext.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

                try (var is = Files.newInputStream(tracePath)) {
                    Allure.addAttachment("Playwright Trace", "application/zip", is, ".zip");
                }
            } catch (IOException e) {
                logger.error("Could not attach Playwright trace to Allure report for test: {}", testName, e);
            }
        }

        threadContext.remove();
        throw throwable;
    }
}
