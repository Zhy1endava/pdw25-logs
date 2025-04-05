

import static org.pd_week.driver_factory.DriverFactory.quitDriver;

import org.openqa.selenium.WebDriver;
import org.pd_week.app_pom.Page1Pom;
import org.pd_week.driver_factory.DriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Page1ChromeTest {

  private static final Logger logger = LoggerFactory.getLogger(Page1ChromeTest.class);

  private WebDriver driver;

  @BeforeMethod
  public void initialize() {
    logger.info("Hello from Chrome beforeMethod");
  }

  @Test
  public void testCreateChromeDriver() {
    // Create a Chrome driver instance
    driver = DriverFactory.createDriver("chrome");

    // Simple assertion to check the driver is not null.
    logger.info("Trying to create a ChromeDriver instance...");
    Assert.assertNotNull(driver, "The ChromeDriver instance should not be null");
    logger.info("ChromeDriver instance created successfully!");

    // Create a LoginPage instance and call doSomething method
    Page1Pom page1Pom = new Page1Pom();
    page1Pom.doSomething(10, 200);
  }

  @AfterMethod
  public void tearDown() {
    quitDriver(driver);
  }
}
