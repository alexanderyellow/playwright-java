package com.example.extensions;

import com.example.BaseTest;
import com.microsoft.playwright.Video;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.nio.file.Files;
import java.nio.file.Path;

public class AllureTestWatcher implements TestExecutionExceptionHandler {

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        Object testInstance = context.getRequiredTestInstance();

        if (testInstance instanceof BaseTest) {
            ((BaseTest) testInstance).takeScreenshot();
            ((BaseTest) testInstance).takeHtml();

            Video video = ((BaseTest) testInstance).getPage().video();
            if (video != null) {
                Path videoPath = video.path();

                if (videoPath != null) {
                    Allure.addAttachment(
                            "Video on failure",
                            "video/webm",
                            Files.newInputStream(videoPath),
                            "webm"
                    );
                }
            }
        }

        throw throwable;
    }
}
