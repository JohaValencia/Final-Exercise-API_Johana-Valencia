package script.utils;


import bankTransaction.Reporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class Listener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Reporter.info("Starting test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Reporter.info("Test: " + result.getName() + " PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.error("Test " + result.getName() + " FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) { }

    @Override
    public  void onStart(ITestContext iTestContext) { }

    @Override
    public void onFinish(ITestContext iTestContext) { }
}
