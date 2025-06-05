package com.bookstore.hooks;

import com.bookStore.utils.ExtentReportUtil;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class TestNGHooks {

    @BeforeSuite
    public void beforeSuite() {
        ExtentReportUtil.initReport();
        System.out.println("📘 Test Suite Started.");
    }

    @BeforeMethod
    public void beforeMethod(java.lang.reflect.Method method) {
        String testName = method.getName();
        ExtentReportUtil.createTest(testName);
        ExtentReportUtil.step("INFO", "🔹 Starting test: " + testName);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentReportUtil.step("FAIL", " Test failed: " + testName);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentReportUtil.step("PASS", " Test passed: " + testName);
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentReportUtil.step("INFO", " Test skipped: " + testName);
        }

        ExtentReportUtil.flushReport();
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("Test Suite Completed.");
    }
}
