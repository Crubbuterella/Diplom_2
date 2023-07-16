import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import apis.UserAPI;
import models.UserGenerateData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
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
    @DisplayName("Логин пользователя с валидными данными")
    public void loginUserTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseLogin = UserAPI.userLogin(user);
        responseLogin
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Логин пользователя с неправильным email")
    public void loginUserWithWrongEmailTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        user.setEmail(user.getEmail() + "test");
        ValidatableResponse responseLogin = UserAPI.userLogin(user);
        responseLogin
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин пользователя с неправильным паролем")
    public void loginUserWithWrongPasswordTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        user.setPassword(user.getPassword() + "test");
        ValidatableResponse responseLogin = UserAPI.userLogin(user);
        responseLogin
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"));
    }
}
