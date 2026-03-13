package com.example.extensions;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Video;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureExtension implements TestExecutionExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AllureExtension.class);
    public static ThreadLocal<Page> threadPage = new ThreadLocal<>();

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        Page page = threadPage.get();
        if (page != null) {
            String testName = context.getDisplayName().replaceAll(" ", "_");

            // Take Screenshot
            Path screenshotPath = Paths.get("build/screenshots/" + testName + ".png");
            Files.createDirectories(screenshotPath.getParent());
            page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath).setFullPage(true));
            try (var is = Files.newInputStream(screenshotPath)) {
                Allure.addAttachment("Screenshot on failure", "image/png", is, ".png");
            }

            // Take HTML
            Path htmlPath = Paths.get("build/pages/" + testName + ".html");
            Files.createDirectories(htmlPath.getParent());
            Files.writeString(htmlPath, page.content());
            try (var is = Files.newInputStream(htmlPath)) {
                Allure.addAttachment("HTML on failure", "text/html", is, ".html");
            }

            // Video attachment
            Video video = page.video();
            if (video != null) {
                Path videoPath = video.path();
                if (videoPath != null) {
                    try (var is = Files.newInputStream(videoPath)) {
                        Allure.addAttachment(
                                "Video on failure",
                                "video/webm",
                                is,
                                "webm"
                        );
                    } catch (IOException e) {
                        logger.error("Could not attach video to Allure report for test: {}", testName, e);
                    }
                }
            }
        }

        threadPage.remove();
        throw throwable;
    }
}
