package com.bases;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.utils.ScreenshotCleanupUtil;
import com.config.EnvironmentConfig;
import com.ui.BasePage;
import com.ui.pages.LoginPage;
import com.ui.pages.ProductPage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    protected static WebDriver driver;
    protected static WebDriverWait wait;

    protected static BasePage basePage;
    protected static LoginPage loginPage;
    protected static ProductPage productPage;

    protected void initDriver() throws IOException {
        cleanupOldScreenshots();

        String browserType = EnvironmentConfig.getBrowserType();

        switch (browserType.toLowerCase()) {
            case "chrome":
                driver = initChromeDriver();
                break;
            default:
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(EnvironmentConfig.getPageLoadTimeout()));
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(EnvironmentConfig.getImplicitWait()));
        driver.get(EnvironmentConfig.getBaseUrl());

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        initializePageObjects();
    }

    private WebDriver initChromeDriver() throws IOException {
        ChromeOptions options = new ChromeOptions();
        
        String ciEnv = System.getenv("CI");
        boolean isHeadless = EnvironmentConfig.isHeadless() || "true".equalsIgnoreCase(ciEnv);
        
        if (isHeadless) {
            Path tempUserDataDir = Files.createTempDirectory("chrome-user-data");
            options.addArguments("--user-data-dir=" + tempUserDataDir);
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        
        return new ChromeDriver(options);
    }

    private void initializePageObjects() {
        basePage = new BasePage();
        basePage.setDriver(driver);

        loginPage = new LoginPage();
        loginPage.setDriver(driver);
        
        productPage = new ProductPage();
        productPage.setDriver(driver);
    }

    protected void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {}
        }
    }
    
    protected void takeScreenshotOnFailure(ITestResult testResult) {
        if(!EnvironmentConfig.takeScreenshotOnFailure()) {
            return;
        }

        if (ITestResult.FAILURE == testResult.getStatus() && driver !=null) {
            try {
                File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
                String fileName = String.format("%s_%s_%s.png",
                    EnvironmentConfig.getEnvironment(), testResult.getName(), timestamp);

                File destination = new File(EnvironmentConfig.getScreenshotPath() + File.separator + fileName);
                destination.getParentFile().mkdirs();

                Files.copy(source.toPath(), destination.toPath());
                logger.info("Screenshot saved: {}", destination.getAbsolutePath());
                
                // Attach to TestNG report
                testResult.setAttribute("screenshotPath", destination.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Failed to capture screenshot", e);
            }
        }
    }

    protected String getEnvironment() {
        return EnvironmentConfig.getEnvironment();
    }

    private void cleanupOldScreenshots() {
        try {
            String screenshotPath = EnvironmentConfig.getScreenshotPath();
            int daysToKeep = 7; // Keep screenshots for 7 days
            
            int deletedCount = ScreenshotCleanupUtil.deleteOldScreenshots(screenshotPath, daysToKeep);
            
            if (deletedCount > 0) {
                logger.info("Cleaned up {} old screenshots (older than {} days)", deletedCount, daysToKeep);
            }
            
            // Log current screenshot directory status
            int currentCount = ScreenshotCleanupUtil.countScreenshots(screenshotPath);
            double sizeMB = ScreenshotCleanupUtil.getScreenshotDirectorySize(screenshotPath);
            
            if (currentCount > 0) {
                logger.info("Screenshot directory: {} files, {:.2f} MB", currentCount, sizeMB);
            }
        } catch (Exception e) {
            logger.warn("Failed to cleanup old screenshots: {}", e.getMessage());
        }
    }

}
