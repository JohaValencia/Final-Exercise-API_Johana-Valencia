package script.tests;

import bankTransaction.Reporter;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import script.pojo.User;
import script.utils.BaseTest;

import java.util.List;
import java.util.stream.Collectors;

public class POJOTest extends BaseTest {

    private int numberOfUsers = 10;

    @Parameters({"endpoint"})
    @Test
    public void pojoTest(String endpoint) {
        List<User> listOfRandomUsers = createRandomUsers(numberOfUsers);
        createUsersInAPI(endpoint, listOfRandomUsers);
        List<User> readListUserInAPI = getUsersList(endpoint);
        Reporter.info("Checking users were correctly created");
        List<String>emailsOfRandomUsers = listOfRandomUsers.stream().map(User::getEmail).collect(Collectors.toList());
        List<String>emailsReadListUsers = readListUserInAPI.stream().map(User::getEmail).collect(Collectors.toList());
        Assert.assertTrue(emailsReadListUsers.containsAll(emailsOfRandomUsers), "The users were not found");
    }

    private void createUsersInAPI(String endpoint, List<User> listOfRandomUsers) {
        for (int i = 0; i < listOfRandomUsers.size() ; i++){
            newUser(endpoint, listOfRandomUsers.get(i));
        }
    }
}
