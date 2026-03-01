package com.example.config;

public class AppConfig {

    private App app;
    private PlaywrightConfig playwright;

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public PlaywrightConfig getPlaywright() {
        return playwright;
    }

    public void setPlaywright(PlaywrightConfig playwright) {
        this.playwright = playwright;
    }

    public static class App {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class PlaywrightConfig {
        private String browser;
        private boolean headless;
        private int slowMo;

        public String getBrowser() {
            return browser;
        }

        public void setBrowser(String browser) {
            this.browser = browser;
        }

        public boolean isHeadless() {
            return headless;
        }

        public void setHeadless(boolean headless) {
            this.headless = headless;
        }

        public int getSlowMo() {
            return slowMo;
        }

        public void setSlowMo(int slowMo) {
            this.slowMo = slowMo;
        }
    }
}
