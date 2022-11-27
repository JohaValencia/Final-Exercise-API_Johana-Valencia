package script.tests;

import bankTransaction.Reporter;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.pojo.User;
import script.utils.BaseTest;

import java.util.Collections;
import java.util.List;

public class VerifyEndpointTest extends BaseTest {

    @Parameters({"endpoint"})
    @Test
    public void endpointTest(String endpoint) {

        deleteUserFromList(endpoint);
        Reporter.info("Verifying if the user is obtained from the endpoint");
        Assert.assertEquals(getUsersList(endpoint), Collections.EMPTY_LIST, "There are users on the endpoint");

    }

    private void deleteUserFromList(String endpoint) {
        List<User> usersList = getUsersList(endpoint);
        if (usersList.size() != 0) {
            deleteUserList(endpoint);

        }
    }

}
