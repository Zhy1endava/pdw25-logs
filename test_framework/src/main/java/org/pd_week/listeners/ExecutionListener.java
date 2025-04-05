package org.pd_week.listeners;

import org.pd_week.utils.LoggingInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.IExecutionListener;

public class ExecutionListener implements IExecutionListener {
  private static final Logger logger;

  private long startTime;

  static {
    LoggingInitializer.initializeLog4j2();
    logger = LoggerFactory.getLogger(ExecutionListener.class);
  }

  @Override
  public void onExecutionStart() {
    startTime = System.currentTimeMillis();
    MDC.put("testName", "start_up_shut_down");
    logger.info("TestNG Starting... ");
  }

  @Override
  public void onExecutionFinish() {
    logger.info("TestNG has finished, Completed in: {}ms", (System.currentTimeMillis() - startTime));
    MDC.remove("testName");
  }
}