import static org.pd_week.driver_factory.DriverFactory.waitABit;

import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.pd_week.driver_factory.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class DriverFactoryTest {

  private static final Logger logger = Logger.getLogger(DriverFactoryTest.class.getName());

  private WebDriver driver;

  @Test
  public void testCreateChromeDriver() {
    // Create a Chrome driver instance
    driver = DriverFactory.createDriver("chrome");
    // Simple assertion to check the driver is not null.
    logger.info("Trying to create a ChromeDriver instance...");
    Assert.assertNotNull(driver, "The ChromeDriver instance should not be null");
    waitABit(5);
    logger.info("ChromeDriver instance created successfully!");
  }

  @AfterMethod
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
