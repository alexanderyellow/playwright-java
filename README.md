# Playwright Java Automation Framework

A robust, scalable automation framework built with **Java 21**, **Playwright**, and **JUnit 5**, designed for end-to-end testing of modern web applications. This framework uses [DemoQA](https://demoqa.com) for demonstration purposes.

## Key Features

- **Page Object Model (POM)**: Clean separation of concerns between test logic and UI elements.
- **Parallel Execution**: Configured for class-level and method-level concurrency.
- **Advanced Reporting**: Integrated with **Allure Report** for detailed test results and historical data.
- **Playwright Traces**: Automatic trace recording for failed tests, viewable via the Playwright CLI.
- **YAML Configuration**: Flexible environment and browser settings managed via `application-config.yml`.
- **CI/CD Ready**: Pre-configured GitHub Actions workflow with caching and artifact management.
- **Data Generation**: Uses **Datafaker** for realistic test data.

---

## Tech Stack

- **Language**: Java 21
- **Build Tool**: Gradle
- **Automation**: [Playwright Java](https://playwright.dev/java/)
- **Test Runner**: JUnit 5
- **Assertions**: AssertJ
- **Reporting**: Allure Report
- **Logging**: Logback
- **Data Generation**: Datafaker

---

## Prerequisites

- **Java Development Kit (JDK) 21** or higher.
- **Gradle** (optional, uses `./gradlew` wrapper).
- **Git** for version control.

---

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/playwright-java.git
cd playwright-java
```

### 2. Install Playwright Browsers
Download the required browser binaries (Chromium, Firefox, WebKit):
```bash
./gradlew playwrightInstall
```

---

## Running Tests

### Local Execution
Run all tests in headless mode (default):
```bash
./gradlew test
```

### Execution Options
- **Headed Mode**:
  ```bash
  ./gradlew test -Dheadless=false
  ```
- **Specific Test Class**:
  ```bash
  ./gradlew test --tests "com.example.TextBoxTest"
  ```
- **Parallelism**: Configured in `build.gradle` via JUnit 5 system properties.

---

## Reporting & Debugging

### Allure Report
Generate and open the Allure report locally:
```bash
./gradlew allureReport
./gradlew allureServe
```

### Playwright Traces
Traces are captured automatically for tests. To open the most recent trace for debugging:
```bash
./gradlew openLastTrace
```
*Note: Traces are stored in `build/playwright-traces/`.*

---

## Project Structure

```text
├── .github/workflows/       # CI/CD (GitHub Actions)
├── gradle/                  # Dependency management (libs.versions.toml)
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── config/      # Configuration management (YAML loader)
│   │   │   └── pages/       # Page Object Model classes
│   │   └── resources/       # application-config.yml
│   └── test/
│       ├── java/com/example/
│       │   ├── extensions/  # JUnit 5 Extensions (Allure/Trace logic)
│       │   └── tests/       # Test suites (TextBox, CheckBox, etc.)
│       └── resources/       # Test logging & Allure properties
└── build.gradle             # Build configuration & dependencies
```

---

## Configuration

All framework settings are managed in `src/main/resources/application-config.yml`:

```yaml
app:
  url: https://demoqa.com

playwright:
  browser: chromium
  headless: true
  slowMo: 500
```

---

## CI/CD Pipeline

The project includes a GitHub Actions workflow (`playwright.yml`) that:
1.  Triggers on `push` and `pull_request` to `main`/`develop`.
2.  Caches Gradle dependencies for speed.
3.  Installs Playwright browsers.
4.  Runs tests and generates Allure reports.
5.  Uploads Playwright traces (on failure) and Allure reports as artifacts.
