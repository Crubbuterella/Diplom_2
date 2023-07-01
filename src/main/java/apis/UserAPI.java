package apis;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import models.User;
import models.UserNewData;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.CoreMatchers.is;

public class UserAPI extends EndpointsAPI {
    public static ValidatableResponse userLogin(User user) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .and()
                .body(user)
                .when()
                .post(EndpointsAPI.LOGIN_API)
                .then();
    }

    public ValidatableResponse userReg(User user) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .and()
                .body(user)
                .when()
                .post(EndpointsAPI.CREATE_USER_API)
                .then();
    }

    public ValidatableResponse deleteUser(String bearerToken) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .headers("Authorization", bearerToken)
                .delete(EndpointsAPI.DELETE_USER_API)
                .then()
                .statusCode(SC_ACCEPTED)
                .and().body("message", is("User successfully removed"));
    }

    public ValidatableResponse updateDataUserWithAuth(UserNewData userNewData, String bearerToken) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .header("Authorization", bearerToken)
                .contentType(ContentType.JSON)
                .and()
                .body(userNewData)
                .when()
                .patch(EndpointsAPI.PATCH_USER_API)
                .then();
    }

    public ValidatableResponse updateDataUserWithoutAuth(UserNewData userNewData) {
        return given()
                .spec(BaseAPI.requestSpecification())
                .and()
                .body(userNewData)
                .patch(EndpointsAPI.PATCH_USER_API)
                .then();
    }
}
