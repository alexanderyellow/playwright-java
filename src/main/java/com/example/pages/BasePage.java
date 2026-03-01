package com.example.pages;

import com.example.config.AppConfigManager;
import com.microsoft.playwright.Page;
import org.opentest4j.MultipleFailuresError;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class BasePage<T extends BasePage<T, K>, K extends BasePage.Assertion> {

    protected final static String BASE_URL = AppConfigManager.INSTANCE.getConfig().getApp().getUrl();
    private final String endPoint;
    protected final Page page;

    public BasePage(Page page, String endPoint) {
        this.page = page;
        this.endPoint = endPoint;
    }

    abstract K createAssertionInstance();

    @SuppressWarnings("unchecked")
    public T navigate() {
        page.navigate(BASE_URL + endPoint);

        return (T) this;
    }

    @SafeVarargs
    public final void softAssert(Consumer<K>... assertions) {
        List<Throwable> failures = new ArrayList<>();
        K assertionInstance = createAssertionInstance();

        for (Consumer<K> assertThat : assertions) {
            try {
                assertThat.accept(assertionInstance);
            } catch (Throwable t) {
                failures.add(t);
            }
        }
        if (!failures.isEmpty()) {
            throw new MultipleFailuresError("Multiple assertion failures (" + failures.size() + " failures)", failures);
        }
    }

    public interface Assertion { }
}
