import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import apis.UserAPI;
import models.UserGenerateData;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTest {
    User user;
    private UserAPI userApi;
    private String authToken;

    @Before
    public void setUp() {
        userApi = new UserAPI();
        user = new UserGenerateData().getRandomUser();
    }

    @After
    public void tearDown() {
        if (authToken == null) return;
        userApi.deleteUser(authToken);
    }

    @Test
    @DisplayName("Регистрации нового пользователя")
    public void createUserTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        responseReg
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться двух одинаковых пользователей")
    public void createUserDuplicateTest() {
        ValidatableResponse responseUser1 = userApi.userReg(user);
        authToken = responseUser1.extract().path("accessToken");
        responseUser1
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
        ValidatableResponse responseUser2 = userApi.userReg(user);
        responseUser2
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без email")
    public void createUserWithoutEmailTest() {
        user.setEmail(null);
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        responseReg
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword(null);
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        responseReg
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Нельзя зарегистрироваться пользователя без имени")
    public void createUserWithoutNameTest() {
        user.setName(null);
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        responseReg
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"));
    }
}
