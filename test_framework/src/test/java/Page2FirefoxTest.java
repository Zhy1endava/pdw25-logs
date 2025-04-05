import static org.pd_week.driver_factory.DriverFactory.quitDriver;

import org.openqa.selenium.WebDriver;
import org.pd_week.app_pom.Page2Pom;
import org.pd_week.driver_factory.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Page2FirefoxTest {

  private static final Logger logger = LoggerFactory.getLogger(Page2FirefoxTest.class);

  private WebDriver driver;

  @BeforeMethod
  public void initialize() {
    logger.info("Hello from Firefox beforeMethod");
  }

  @Test
  public void testCreateFirefoxDriver() {
    // Create a Chrome driver instance
    driver = DriverFactory.createDriver("firefox");

    // Simple assertion to check the driver is not null.
    logger.info("Trying to create a FirefoxDriver instance...");
    Assert.assertNotNull(driver, "The FirefoxDriver instance should not be null");
    logger.info("FirefoxDriver instance created successfully!");

    // Create a Page2Pom instance and call doSomething method
    Page2Pom page2Pom = new Page2Pom();
    page2Pom.doSomething(20, 100);
  }

  @AfterMethod
  public void tearDown() {
    quitDriver(driver);
  }
}
