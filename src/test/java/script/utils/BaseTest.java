package script.utils;


import bankTransaction.Reporter;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import script.pojo.User;

import java.util.*;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected int getUsers;

    protected List<User> getUsersList(String endpoint) {
        RestAssured.baseURI = endpoint;
        RequestSpecification httpRequest = given();
        Response response = httpRequest.get("");

        JsonPath jsonPathEvaluator = response.jsonPath();

        List<User> usersList = new ArrayList<>();

        try {
            usersList = jsonPathEvaluator.getList(".", User.class);
        } catch (Exception e) {
            Reporter.error(String.valueOf(e));
        }
        getUsers = response.getStatusCode();
        return usersList;

    }

    protected int getAllUsers(String endpoint) {
        getUsersList(endpoint);
        return getUsers;
    }

    protected int deleteUser(String endpoint, User user) {
        Response response = given()
                .contentType("application/json")
                .when()
                .delete(endpoint + "/" + user.getId());
        return response.getStatusCode();

    }

    protected int newUser(String endpoint, User user) {
        Response response = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(endpoint);
        return response.getStatusCode();

    }

    protected int updateUser(String endpoint, User user) {
        Response response = given()
                .contentType("application/json")
                .body(user)
                .when()
                .put(endpoint + "/" + user.getId());
        return response.getStatusCode();

    }

    protected boolean deleteUserList(String endpoint) {
        List<Boolean> deleteUser = new ArrayList<>();
        List<Integer> status = new ArrayList<>();
        List<User> users = getUsersList(endpoint);
        if(users.size() > 0) {
            for(int i = 0; i < users.size(); i++) {
                status.add(deleteUser(endpoint, users.get(i)));
                deleteUser.add(status.get(i) == 200);
                if (status.get(i) != 200) {
                    Reporter.error("Is not possible to delete the user " + users.get(i).getId() + "Http´response " + status.get(i));
                }
            }
        }
        return deleteUser.contains(true);
    }

    protected List<User> createANewUser(int userNumber) {
        List<User> users = new ArrayList<>();
        Faker javaFaker = Faker.instance(new Locale("ENG"));
        for (int i = 0; i < userNumber; i++) {
            users.add(new User(
                    javaFaker.name().firstName(),
                    javaFaker.name().lastName(),
                    javaFaker.number().numberBetween(100,100000),
                    javaFaker.number().randomDouble(10, 1000, 10000000),
                    javaFaker.options().option("payment", "deposit", "amount"),
                    javaFaker.internet().emailAddress(),
                    javaFaker.random().nextBoolean(),
                    javaFaker.country().name(),
                    javaFaker.phoneNumber().cellPhone()
                    ));
        }

        String duplicateEmail = javaFaker.internet().emailAddress();
        users.get(0).setEmail(duplicateEmail);
        users.get(userNumber - 1).setEmail(duplicateEmail);

        return users;


    }

    protected int userIsUpdate(String endpoint, int id) {
        User user = getUsersList(endpoint).get(id - 1);
        Faker javaFaker = Faker.instance(new Locale("ENG"));

        user.setFirstName(javaFaker.name().firstName());
        user.setLastName(javaFaker.name().lastName());
        user.setAccountNumber(javaFaker.number().numberBetween(100,100000));
        user.setAmount(javaFaker.number().randomDouble(10,1000, 10000000));
        user.setTransactionType(javaFaker.options().option("payment", "deposit", "amount"));
        user.setEmail(javaFaker.internet().emailAddress());
        user.setActive(javaFaker.random().nextBoolean());
        user.setTelephone(javaFaker.phoneNumber().cellPhone());

        return userIsUpdate(endpoint, id);
    }


    protected boolean verifyEmailDuplicate(String endpoint) {
        List<String> emails = new ArrayList<>();
        getUsersList(endpoint).forEach(user -> {
            emails.add(user.getEmail());
        });

        Set<String> setUser = new HashSet<>(emails);
        return setUser.size() == getUsersList(endpoint).size();
    }

    protected boolean newUsers(String endpoint, int usersNumber) {
        List<User> users = createANewUser(usersNumber);
        List<Boolean> userStatus = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            boolean emailCreated = false;
            for (int j = 0; j < users.size() && !emailCreated; j++) {
                if (users.get(i).getEmail().equals(users.get(j).getEmail()) && i < j) {
                    Reporter.info("This Email " + users.get(j).getEmail() + "is already created. Try again with another one");
                    emailCreated = true;
                }
            }
            if (!emailCreated) {
                userStatus.add(newUser(endpoint, users.get(i)) == 201);
            }
        }
        return !userStatus.contains(false);

    }

}
