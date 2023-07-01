package apis;

import io.restassured.response.ValidatableResponse;
import models.Order;
import static io.restassured.RestAssured.given;

public class OrderAPI extends EndpointsAPI {
    public static ValidatableResponse createOrder(Order order, String bearerToken) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .headers("Authorization", bearerToken)
                .body(order)
                .post(EndpointsAPI.CREATE_ORDER_API)
                .then();
    }

    public static ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .body(order)
                .post(EndpointsAPI.CREATE_ORDER_API)
                .then();
    }

    public static ValidatableResponse getAllIngredients() {
        return given()
                .spec(BaseAPI.requestSpecification())
                .get(EndpointsAPI.INGREDIENT_API)
                .then();
    }

    public ValidatableResponse getUserOrdersWithAuth(String bearerToken) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .headers("Authorization", bearerToken)
                .get(EndpointsAPI.USER_ORDERS_API)
                .then();
    }

    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(BaseAPI.requestSpecification())
                .get(EndpointsAPI.USER_ORDERS_API)
                .then();
    }
}