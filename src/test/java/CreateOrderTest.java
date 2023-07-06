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
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    User user;
    Order order;
    UserAPI userApi;
    OrderAPI orderApi;
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
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrderTest() {
        order = new Order(getIngredientList());
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        UserAPI.userLogin(user);
        ValidatableResponse responseCreateOrder = OrderAPI.createOrder(order, authToken);
        responseCreateOrder
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void createOrderWithoutAuthTest() {
        order = new Order(getIngredientList());
        ValidatableResponse responseCreateOrder = OrderAPI.createOrderWithoutAuth(order);
        responseCreateOrder
                .assertThat()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без добавления ингредиентов авторизованным пользователем")
    public void createOrderWithoutIngredientTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderAPI.createOrder(order, authToken);
        responseCreateOrder
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов авторизованным пользователем")
    public void createOrderWithWrongHashIngredientTest() {
        order = new Order(getWrongIngredientList());
        ValidatableResponse responseReg = userApi.userReg(user);
        authToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderAPI.createOrder(order, authToken);
        responseCreateOrder
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    private List<String> getIngredientList() {
        ValidatableResponse validatableResponse = OrderAPI.getAllIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(2));
        ingredients.add(list.get(4));
        ingredients.add(list.get(0));
        return ingredients;
    }

    private List<String> getWrongIngredientList() {
        ValidatableResponse validatableResponse = OrderAPI.getAllIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(2).repeat(2));
        ingredients.add(list.get(4).repeat(1));
        ingredients.add(list.get(0));
        return ingredients;
    }
}
