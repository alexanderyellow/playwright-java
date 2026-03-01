package com.example.config;

import java.io.InputStream;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public enum AppConfigManager {
    INSTANCE;

    private static final String CONFIG_FILE = "application-config.yml";
    private final AppConfig config;

    AppConfigManager() {
        try (InputStream inputStream =
                AppConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("application-config.yml not found in classpath");
            }

            LoaderOptions options = new LoaderOptions();
            Yaml yaml = new Yaml(new Constructor(AppConfig.class, options));
            this.config = yaml.load(inputStream);

            overrideWithSystemProperties();

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private void overrideWithSystemProperties() {
        if (config == null) return;

        // Override App settings
        if (config.getApp() != null) {
            String appUrl = System.getProperty("app.url");
            if (appUrl != null) {
                config.getApp().setUrl(appUrl);
            }
        }

        // Override Playwright settings
        if (config.getPlaywright() != null) {
            String browser = System.getProperty("playwright.browser");
            if (browser != null) {
                config.getPlaywright().setBrowser(browser);
            }

            String headless = System.getProperty("playwright.headless");
            if (headless != null) {
                config.getPlaywright().setHeadless(Boolean.parseBoolean(headless));
            }

            String slowMo = System.getProperty("playwright.slowMo");
            if (slowMo != null) {
                config.getPlaywright().setSlowMo(Integer.parseInt(slowMo));
            }
        }
    }

    public AppConfig getConfig() {
        return config;
    }
}
