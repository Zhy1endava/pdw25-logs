package org.pd_week.app_pom;

import static org.pd_week.driver_factory.DriverFactory.waitABit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Page1Pom {
  private static final Log logger = LogFactory.getLog(Page1Pom.class);

  public Page1Pom() {
    logger.info("Page1Pom initialized.");
  }

  public void doSomething(int repetitions, int timeoutMs) {
    for (int i = 1; i <= 10; i++) {
      logger.info("Page1Pom is doing something for " + repetitions + " times, attempt number " + i);
      waitABit(timeoutMs);
    }
  }
}
