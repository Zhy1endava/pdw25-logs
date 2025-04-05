package org.pd_week.utils;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.async.AsyncLoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class LoggingInitializer {

  private static boolean initialized = false;
  private static boolean gridUsed;
  private static Level rootLoggingLevel;
  private static String contextCustomName;
  private static LoggerContext context;

  public static void main(String[] args) {
    initializeLog4j2();
  }

  public static synchronized void initializeLog4j2() {
    if (initialized) {
      System.out.println("Logs already initialized...");
      return;
    }
    System.out.println("Logs Starting... ---------------------------------");

    getSystemProperties();
    if (gridUsed) {
      configureAsyncLogsForJenkins();
      setErrorForGridConsole();
    }
    else {
      configureSyncLogsForLocal();
    }
    confirmValidContextStarted();
    resetJulLogs();
    stopContextIfOff();
    initialized = true;

    System.out.printf("Log context: %s, context state: %s\n" +
                          "--------------------------------------------------\n",
                      context.getName(),
                      context.getState()
    );
  }

  // Retrieve log level and grid info from system properties
  private static void getSystemProperties() {
    String logLevel = System.getProperty("log.level", "OFF").toUpperCase();
    String gridUsedString = System.getProperty("test.grid", "false").toUpperCase();
    gridUsed = Boolean.parseBoolean(gridUsedString);
    rootLoggingLevel = Level.getLevel(logLevel);
    if (rootLoggingLevel == null) {
      rootLoggingLevel = Level.OFF;
    }
    System.out.printf("Log level from system properties: %s, Grid run: %s\n", rootLoggingLevel, gridUsedString);
  }

  // Force async logging for all appenders (due to performance, line numbers are lost)
  // more details here: https://logging.apache.org/log4j/2.x/manual/async.html#AllAsync
  private static void configureAsyncLogsForJenkins() {
    contextCustomName = "Grid_run";

    // Get current context to confirm if async is used
    context = (LoggerContext) LogManager.getContext(true);
  }

  // Set ERROR console level for jenkins runs
  private static void setErrorForGridConsole() {
    Configuration config = context.getConfiguration();
    ConsoleAppender consoleAppender = config.getAppender("ConsoleAppender");

    // Modify existing filter to accept only ERROR and FATAL
    if (consoleAppender != null && !rootLoggingLevel.equals(Level.OFF)) {
      ThresholdFilter errorFilter = ThresholdFilter.createFilter(Level.ERROR, null, null);
      consoleAppender.getFilter().stop();
      consoleAppender.addFilter(errorFilter);

      context.updateLoggers();

      System.out.println("Grid run recognized -> Console threshold level set to ERROR!");
    }
  }

  // Setting basic context for local runs to get line number
  private static void configureSyncLogsForLocal() {
    contextCustomName = "Local_run";
  }

  // Get current context to confirm if async is used
  private static void confirmValidContextStarted() {
    context = (LoggerContext) LogManager.getContext(true);
    if (context instanceof AsyncLoggerContext) {
      contextCustomName += "_Async -> Log4j2";
    }
    else {
      contextCustomName += "_Sync -> Log4j2";
    }
    context.setName(contextCustomName);
  }

  // Reset JUL logs and forward them to SLF4j
  private static void resetJulLogs() {
    java.util.logging.LogManager.getLogManager().reset();
    SLF4JBridgeHandler.install();
  }

  // If level is OFF, stop config and stop context to prevent/minimize any logging activity in the background
  private static void stopContextIfOff() {
    LoggerContext context = (LoggerContext) LogManager.getContext(true);
    if (rootLoggingLevel.equals(Level.OFF)) {
      context.getConfiguration().stop();
      context.stop(1, TimeUnit.SECONDS);
    }
  }
}
