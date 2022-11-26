package script.tests;

import bankTransaction.Reporter;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.utils.BaseTest;

public class GetRequestTest extends BaseTest {

    private int id = 1;

    @Parameters({"endpoint"})
    @Test
   public void checkingDuplicatesTest(String endpoint) {
        Reporter.info("Checking the users were from de endpoint correctly");
        Assert.assertEquals(getAllUsers(endpoint), 200, "Users were not from endpoint");

        Reporter.info("Checking for not duplicate emails");
        Assert.assertTrue(verifyEmailDuplicate(endpoint),"Emails duplicate");
    }
}
