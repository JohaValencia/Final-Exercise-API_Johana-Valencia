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

    protected User getUser(String endpoint, int id) {
        Response response = given()
                .contentType("application/json")
                .when()
                .get(endpoint + "/" + id);
        return response.getBody().as(User.class);
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

    protected User newUser(String endpoint, User user) {
        Response response = given()
                .contentType("application/json")
                .body(user)
                .when()
                .post(endpoint);
        return response.getBody().as(User.class);

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

    protected List<User> createRandomUsers(int userNumber) {
        List<User> users = new ArrayList<>();


        for (int i = 0; i < userNumber; i++) {
            users.add(createRandomUser());
        }

        return users;


    }

    protected User createRandomUser() {
        Faker javaFaker = Faker.instance(new Locale("ENG"));
        return new User(
                javaFaker.name().firstName(),
                javaFaker.name().lastName(),
                javaFaker.number().numberBetween(100, 100000),
                javaFaker.number().randomDouble(10, 1000, 10000000),
                javaFaker.options().option("payment", "deposit", "amount"),
                javaFaker.internet().emailAddress(UUID.randomUUID().toString()),
                javaFaker.random().nextBoolean(),
                javaFaker.country().name(),
                javaFaker.phoneNumber().cellPhone()
        );
    }

    protected boolean verifyEmailDuplicate(String endpoint) {
        List<String> emails = new ArrayList<>();
        getUsersList(endpoint).forEach(user -> {
            emails.add(user.getEmail());
        });

        Set<String> setUser = new HashSet<>(emails);
        return setUser.size() == getUsersList(endpoint).size();
    }


}
