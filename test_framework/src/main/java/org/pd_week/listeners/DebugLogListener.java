package org.pd_week.listeners;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.IConfigurationListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class DebugLogListener implements ITestListener, IInvokedMethodListener {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String MDC_TEST_NAME_KEY = "testName";

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult result) {
    MDC.put(MDC_TEST_NAME_KEY, getExtendedTestName(result));
    log.warn("OnMethodStart: {}", result.getMethod().getMethodName());
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult result) {
    log.warn("OnMethodSuccess: {}", result.getMethod().getMethodName());
    MDC.remove(MDC_TEST_NAME_KEY);
  }

  @Override
  public void onTestStart(ITestResult result) {
    MDC.put(MDC_TEST_NAME_KEY, getExtendedTestName(result));
    log.warn("OnTestStart: {}", result.getName());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    MDC.put(MDC_TEST_NAME_KEY, getExtendedTestName(result));
    log.warn("OnTestSkipped: {}", result.getName());
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

  }

  @Override
  public void onStart(ITestContext iTestContext) {
    log.info("ON execution context start: {}", MDC.get(MDC_TEST_NAME_KEY));
  }

  @Override
  public void onFinish(ITestContext iTestContext) {
    log.info("ON  execution context finish: {}", MDC.get(MDC_TEST_NAME_KEY));
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    MDC.put(MDC_TEST_NAME_KEY, getExtendedTestName(result));
    log.warn("OnTestSuccess: {}", result.getName());
  }

  @Override
  public void onTestFailure(ITestResult result) {
    MDC.put(MDC_TEST_NAME_KEY, getExtendedTestName(result));
    log.warn("OnTestFailure: {}", result.getName());
  }
  
  private String getExtendedTestName(ITestResult result) {
    return result.getInstanceName().concat(".").concat(result.getMethod().getMethodName());
  }
}
