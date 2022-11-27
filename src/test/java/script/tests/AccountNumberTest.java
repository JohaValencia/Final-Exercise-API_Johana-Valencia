package script.tests;

import bankTransaction.Reporter;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.pojo.User;
import script.utils.BaseTest;

public class AccountNumberTest extends BaseTest {

    private int accountNumber = 666;


    @Test
    @Parameters("endpoint")
    public void accountNumberTest(String endpoint) {
        User userToUpdate = createRandomUser();
        userToUpdate = newUser(endpoint, userToUpdate);
        userToUpdate.setAccountNumber(accountNumber);
        updateUser(endpoint, userToUpdate);
        User userFromAPI =  getUser(endpoint, userToUpdate.getId());

        Reporter.info("Checking the account number was correctly updated");
        Assert.assertEquals(userFromAPI.getAccountNumber(), accountNumber, "User is not updated");
    }
}
