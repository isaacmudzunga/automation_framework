package selenium_tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;

public class base {
    private static WebDriver driver;
    private static final String URL = "http://www.demo.guru99.com/V4/";

    public static WebDriver getDriver() {
        return driver;
    }

    public static String getUrl() {
        return URL;
    }

    static {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\Isaac\\Downloads\\geckodriver-v0.34.0-win32\\geckodriver.exe");
        beforeSpec();
    }

    private static void beforeSpec() {
        // Create FirefoxOptions instance
        FirefoxOptions options = new FirefoxOptions();
        // Set capability to accept insecure SSL certificates
        options.setAcceptInsecureCerts(true);
        // Initialize WebDriver with FirefoxOptions
        driver = new FirefoxDriver(options);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public static void afterSpec() {
        driver.quit();
    }
}
