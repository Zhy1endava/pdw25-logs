package org.pd_week.app_pom;

import static org.pd_week.driver_factory.DriverFactory.waitABit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Page2Pom {
  private static final Log logger = LogFactory.getLog(Page2Pom.class);

  public Page2Pom() {
    logger.info("Page2Pom initialized.");
  }

  public void doSomething(int repetitions, int timeoutMs) {
    for (int i = 1; i <= 10; i++) {
      logger.info("Something is being done by Page2Pom for " + repetitions + " times, attempt number " + i);
      waitABit(timeoutMs);
    }
  }
}
