package script.tests;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.utils.BaseTest;
import bankTransaction.Reporter;

public class VerifyEndpointTest extends BaseTest {

    @Parameters({"endpoint"})
    @Test
    public void endpointTest(String endpoint) {
        Reporter.info("Verifying if the user is obtained from the endpoint");
        Assert.assertEquals(getUsersList(endpoint), 200, "Users have not been from endpoint");

        Reporter.info("Checking if the endpoint is empty" );
        Reporter.info("Records in the endpoint " + getUsersList(endpoint).size());

        Reporter.info("Checking previous updates was delete correctly");
        Assert.assertTrue(deleteUserList(endpoint),"The totally of users were not delete");
    }

}
