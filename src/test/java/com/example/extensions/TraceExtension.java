package com.example.extensions;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TraceExtension implements TestExecutionExceptionHandler {
    // This holds the context unique to the current thread
    public static ThreadLocal<BrowserContext> threadContext = new ThreadLocal<>();

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        BrowserContext playwrightContext = threadContext.get();
        if (playwrightContext == null) return;

        String testName = context.getDisplayName().replaceAll(" ", "_");
        Path tracePath = Paths.get("build/playwright-traces/" + testName + ".zip");

        if (context.getExecutionException().isPresent()) {
            // Stop and SAVE
            playwrightContext.tracing().stop(new Tracing.StopOptions().setPath(tracePath));

            // ATTACH TO ALLURE
            try (var is = Files.newInputStream(tracePath)) {
                Allure.addAttachment("Playwright Trace", "application/zip", is, ".zip");
            }
        } else {
            // Stop and DISCARD
            playwrightContext.tracing().stop();
        }
        threadContext.remove();

        throw throwable;
    }
}
