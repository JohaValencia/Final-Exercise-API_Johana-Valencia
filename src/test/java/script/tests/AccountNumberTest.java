package script.tests;

import bankTransaction.Reporter;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.utils.BaseTest;

public class AccountNumberTest extends BaseTest {

    private int id = 1;

    @Parameters({"endpoint"})
    @Test
    public void accountNumberTest(String endpoint) {
        Reporter.info("Checking the account number was correctly updated");
        Assert.assertEquals(userIsUpdate(endpoint, id), 200, "User is not updated");
    }
}
