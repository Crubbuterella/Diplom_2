import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import apis.OrderAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import apis.UserAPI;
import models.UserGenerateData;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;

public class OrdersUserTest {
    User user;
    Order order;
    private UserAPI userApi;
    private OrderAPI orderApi;
    private String authToken;

    @Before
    public void setUp() {
        userApi = new UserAPI();
        user = new UserGenerateData().getRandomUser();
        orderApi = new OrderAPI();
        order = new Order();
    }

    @After
    public void tearDown() {
        if (authToken == null) return;
        userApi.deleteUser(authToken);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getUserOrdersWithAuthTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        UserAPI.userLogin(user);
        OrderAPI.createOrder(order, authToken);
        ValidatableResponse responseOrdersUser = orderApi.getUserOrdersWithAuth(authToken);
        responseOrdersUser
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getUserOrdersWithoutAuthTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseOrders = orderApi.getUserOrdersWithoutAuth();
        responseOrders
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", is("You should be authorised"));
    }
}
