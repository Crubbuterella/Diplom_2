import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import apis.UserAPI;
import models.UserGenerateData;
import models.UserNewData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class UpdateUserTest {
    User user;
    UserNewData userNewData;
    private UserAPI userApi;
    private String authToken;

    @Before
    public void setUp() {
        userApi = new UserAPI();
        user = new UserGenerateData().getRandomUser();
        userNewData = new UserNewData();
    }

    @After
    public void tearDown() {
        if (authToken == null) return;
        userApi.deleteUser(authToken);
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    public void updateDataUserWithAuthTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseUpdate = userApi.updateDataUserWithAuth(userNewData.random(), authToken);
        responseUpdate
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Изменение данных незалогиненного пользователя")
    public void updateDataUserWithoutAuthTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseUpdate = userApi.updateDataUserWithoutAuth(userNewData);
        responseUpdate
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }
}
