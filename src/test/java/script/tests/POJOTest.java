package script.tests;

import bankTransaction.Reporter;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.utils.BaseTest;

public class POJOTest extends BaseTest {

    private int numberOfUsers = 12;

    @Parameters({"endpoint"})
    @Test
    public void pojoTest(String endpoint) {
        Reporter.info("Checking users were correctly created");
        Assert.assertTrue(newUsers(endpoint, numberOfUsers), "FAILED");
    }
}
