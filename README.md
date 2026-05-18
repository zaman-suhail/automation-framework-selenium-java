# Selenium Automation Framework

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Selenium](https://img.shields.io/badge/Selenium-4.18-green?style=for-the-badge&logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.9-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.8-red?style=for-the-badge&logo=apachemaven)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-CI/CD-black?style=for-the-badge&logo=githubactions)

> A production-ready Selenium automation framework built with **9 design patterns**, clean architecture, and full CI/CD integration via GitHub Actions.

---

## What This Framework Does

- Automates browser testing using **Selenium 4 + Java + TestNG**
- Generates beautiful **HTML reports** with screenshots on failure
- Reads test data from **JSON files** — no hardcoded credentials
- Runs automatically on every **GitHub push** via GitHub Actions
- Supports **Chrome, Firefox, and Edge** — switch with one config change

---

## Quick Start

```bash
# 1. Clone the repo
git clone https://github.com/zaman-suhail/automation-framework-selenium-java.git


# 2. Set your website URL in config
# Edit: src/test/resources/config/config.properties
url=https://your-website.com

# 3. Run tests
mvn test

# 4. Open the HTML report
xdg-open reports/Report_*.html
```

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 21 | Programming language |
| Selenium WebDriver | 4.18 | Browser automation |
| TestNG | 7.9 | Test runner, annotations, DataProvider |
| WebDriverManager | 5.7 | Auto downloads browser drivers |
| ExtentReports | 5.1 | Dark theme HTML reports with screenshots |
| Log4j2 | 2.23 | Logging to console and log file |
| Jackson | 2.16 | Reads JSON test data |
| Maven Surefire | 3.2.5 | Runs TestNG suite from testng.xml |
| GitHub Actions | — | CI/CD pipeline |

---

## Design Patterns

| Pattern | File | What It Does |
|---|---|---|
| **Singleton** | `ConfigReader.java` | Config loaded once, shared everywhere |
| **Singleton + ThreadLocal** | `DriverManager.java` | One browser per thread — parallel safe |
| **Factory** | `BrowserFactory.java` | Creates Chrome/Firefox/Edge from config |
| **Page Object Model** | `LoginPage.java`, `ProductsPage.java` | Locators in one place — change once, fix all |
| **Fluent Interface** | `LoginPage.java` | `.enterUsername().enterPassword().clickLogin()` |
| **Template Method** | `BasePage.java`, `BaseTest.java` | Common actions in parent — child inherits |
| **Observer** | `TestListener.java` | Auto screenshot on fail, auto report |
| **Data-Driven** | `TestDataReader.java` | JSON data feeds multiple test runs |
| **Facade** | `CheckoutFacade.java` | Complex workflow behind one simple method |

---

## Folder Structure

```
selenium-framework/
├── .github/
│   └── workflows/
│       └── selenium-tests.yml         # GitHub Actions CI/CD
├── src/
│   ├── main/java/com/framework/
│   │   ├── config/
│   │   │   └── ConfigReader.java      # Singleton — reads config.properties
│   │   ├── driver/
│   │   │   └── DriverManager.java     # Singleton + ThreadLocal
│   │   ├── factory/
│   │   │   └── BrowserFactory.java    # Factory Pattern
│   │   ├── pages/
│   │   │   ├── BasePage.java          # Template Method — common actions
│   │   │   ├── LoginPage.java         # POM + Fluent Interface
│   │   │   └── ProductsPage.java      # Page Object Model
│   │   ├── facade/
│   │   │   └── CheckoutFacade.java    # Facade Pattern
│   │   ├── listeners/
│   │   │   └── TestListener.java      # Observer Pattern
│   │   └── utils/
│   │       ├── ExtentReportManager.java  # HTML Reports
│   │       └── TestDataReader.java       # Data-Driven Pattern
│   ├── main/resources/
│   │   └── log4j2.xml                 # Logging configuration
│   └── test/
│       ├── java/com/framework/tests/
│       │   ├── BaseTest.java          # Template Method
│       │   └── LoginTest.java         # All patterns combined
│       └── resources/
│           ├── config/
│           │   └── config.properties  # Framework settings
│           └── testdata/
│               └── loginData.json     # Test data (JSON)
├── reports/                           # HTML reports (auto-generated)
├── logs/                              # Log files (auto-generated)
├── testng.xml                         # TestNG suite config
├── .gitignore
└── pom.xml                            # Maven dependencies
```

---

## Configuration

Edit `src/test/resources/config/config.properties`:

```properties
# Browser: chrome | firefox | edge
browser=chrome

# Your website URL
url=https://www.saucedemo.com

# Timeouts in seconds
implicit.wait=10
explicit.wait=15
page.load.timeout=30

# Take screenshot when test fails
screenshot.on.failure=true

# Set true for CI/CD (no browser window)
headless=false
```

---

## Run Options

```bash
# Default — Chrome, all tests
mvn test

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge

# Headless — no browser window (for CI/CD)
mvn test -Dheadless=true

# Smoke tests only
mvn test -Dgroups=smoke

# Regression tests only
mvn test -Dgroups=regression
```

---

## Test Data

Test data lives in `src/test/resources/testdata/loginData.json`.
Add more users here — tests run automatically for each row. No code change needed.

```json
{
  "validUsers": [
    { "username": "standard_user", "password": "secret_sauce", "description": "Standard user" }
  ],
  "invalidUsers": [
    { "username": "wrong_user", "password": "wrong_pass", "expectedError": "do not match" }
  ]
}
```

---

## Reports

After running tests, open the HTML report:

```bash
xdg-open reports/Report_*.html
```

**What the report contains:**
- Pass / Fail / Skip — green / red / yellow
- Screenshots embedded on failure
- System info panel — OS, Java, Browser, URL, Date
- Full stack trace for failed tests
- Execution time per test

**Log file location:** `logs/framework.log`

---

## Add Your Own Page

**Step 1** — Update URL in `config.properties`:
```properties
url=https://your-website.com
```

**Step 2** — Create a page object in `src/main/java/com/framework/pages/`:
```java
public class MyPage extends BasePage {

    // Add your locators here
    private final By myButton = By.id("your-button-id");
    private final By myField  = By.name("your-field-name");

    // Add your actions — Fluent Interface
    public MyPage clickButton() {
        click(myButton);   // BasePage method
        return this;
    }

    public MyPage enterText(String text) {
        type(myField, text);   // BasePage method
        return this;
    }

    @Override
    public boolean isPageLoaded() {
        return isVisible(myButton);
    }
}
```

**Step 3** — Create a test in `src/test/java/com/framework/tests/`:
```java
public class MyTest extends BaseTest {

    @Test(description = "My feature test")
    public void testMyFeature() {
        ExtentReportManager.info("Testing my feature");

        new MyPage()
            .enterText("hello")
            .clickButton();

        Assert.assertTrue(new MyPage().isPageLoaded(), "Page did not load!");
        ExtentReportManager.pass("Test passed!");
    }
}
```

**Step 4** — Add to `testng.xml`:
```xml
<class name="com.framework.tests.MyTest"/>
```

**Step 5** — Run:
```bash
mvn test
```

---

## GitHub Actions — CI/CD

Every push to `main` branch automatically:

1. Downloads your code on GitHub's server
2. Sets up Java 21
3. Caches Maven dependencies (faster builds)
4. Installs Chrome
5. Runs `mvn test -Dheadless=true`
6. Uploads HTML report as downloadable artifact (kept 30 days)

**Download report after a run:**
1. Go to the **Actions** tab on GitHub
2. Click the latest run
3. Scroll down to **Artifacts**
4. Download `Report-N` zip file
5. Open the `.html` file inside

**Manual trigger:**
- Actions tab → Selenium Tests → Run workflow → select branch → Run

---

## Common Issues

| Problem | Solution |
|---|---|
| `Source option 5 is no longer supported` | Add `<maven.compiler.source>21</maven.compiler.source>` in `pom.xml` |
| `package org.testng does not exist` | Remove `<scope>test</scope>` from TestNG in `pom.xml` |
| `Config file not found` | Check path: `src/test/resources/config/config.properties` |
| `Element not found` | Wrong locator — check with F12 in Chrome DevTools |
| Report not generated | Make sure Surefire plugin version is `3.2.5` in `pom.xml` |
| GitHub Actions fails | Pass `-Dheadless=true` in the workflow run command |
| CDP Warning in logs | Safe to ignore — tests still run correctly |

---

## Locator Guide

| Locator | When to Use |
|---|---|
| `By.id("x")` | Best option — fastest and most stable |
| `By.name("x")` | Good for form input fields |
| `By.cssSelector("[data-test='x']")` | Best for custom attributes |
| `By.className("x")` | Only when class is unique to one element |
| `By.linkText("x")` | For anchor tags with exact visible text |
| `By.xpath("//x")` | Last resort — use when nothing else works |

---

## License

This project is open source and available under the [MIT License](LICENSE).

---

*Built with Java 21 · Selenium 4 · TestNG · 9 Design Patterns · GitHub Actions*