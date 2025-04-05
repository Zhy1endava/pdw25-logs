package org.pd_week.driver_factory;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

  // Using JUL for logging
  private static final Logger logger = Logger.getLogger(DriverFactory.class.getName());

  /**
   * Creates a WebDriver instance for the specified browser.
   *
   * @param browser The browser name (e.g., "chrome", "firefox")
   * @return An instance of WebDriver
   */
  public static WebDriver createDriver(String browser) {

    logger.log(Level.INFO, "Request received to create a driver for: {0}", browser);

    switch (browser.toLowerCase()) {
      case "chrome":
        logger.info("Creating ChromeDriver...");
        // If you rely on local driver binaries, ensure they're on PATH or configured.
        // Otherwise, consider WebDriverManager if you want automatic downloads.
        ChromeOptions chromeOptions = new ChromeOptions();
        // Add any Chrome-specific options if needed
        return new ChromeDriver(chromeOptions);

      case "firefox":
        logger.info("Creating FirefoxDriver...");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        // Add any Firefox-specific options if needed
        return new FirefoxDriver(firefoxOptions);

      default:
        logger.warning("Unknown browser specified, defaulting to ChromeDriver.");
        return new ChromeDriver();
    }
  }

  /**
   * Quits the given WebDriver instance.
   *
   * @param driver The WebDriver instance to quit
   */
  public static void quitDriver(WebDriver driver) {
    if (driver != null) {
      logger.info("Quitting the WebDriver instance...");
      driver.quit();
    } else {
      logger.warning("WebDriver instance is null, cannot quit.");
    }
  }

  /**
   * Helper method to wait for a given time in milliseconds.
   *
   * @param milliseconds The time to wait in milliseconds
   */
  public static void waitABit(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      logger.log(Level.SEVERE, "Thread was interrupted while waiting", e);
      Thread.currentThread().interrupt();
    }
  }
}
