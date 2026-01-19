package api;

import api.models.Book;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static config.Prop.PROP;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class DemoqaAPITests {
    String code;
    String message;
    String token;
    String userId;

    public DemoqaAPITests() {
        getUUID();
    }

    public String getToken() {
        this.checkSuccessGenerationToken();
        return "Bearer " + token;
    }

    @Test
    public void checkSuccessAutorization() {
        Response response = given()
                .when()
                .spec(Specification.getStartData())
                .post("Account/v1/Authorized")
                .then().log().all()
                .statusCode(200)
                .body("", equalTo(true))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get(""), true);
    }

    @Test
    public void checkUnSuccessAuthorizationWithOutAutData() {
        code = "1200";
        message = "UserName and Password required.";

        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .post("Account/v1/Authorized")
                .then().log().ifError()
                .statusCode(400)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    public void checkUnSuccessAuthorizationWithWrongAutData() {
        code = "1207";
        message = "User not found!";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "userName", "srting",
                        "password", "string"
                ))
                .post("Account/v1/Authorized")
                .then().log().ifError()
                .statusCode(404)
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    public void checkSuccessGenerationToken() {
        String status = "Success";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword()
                ))
                .contentType(ContentType.JSON)
                .post("Account/v1/GenerateToken")
                .then().log().ifError()
                .statusCode(200)
                .body("token", notNullValue())
                .body("expires", notNullValue())
                .body("status", equalTo(status))
                .extract().response();
        Assertions.assertTrue(Instant.parse(response.jsonPath().get("expires")).isAfter(Instant.now()));
        token = response.jsonPath().get("token");
    }

    @Test
    public void checkAuthFailedGenerationToken() {
        message = "User authorization failed.";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .body(Map.of(
                        "userName", "string",
                        "password", "string"
                ))
                .contentType(ContentType.JSON)
                .post("Account/v1/GenerateToken")
                .then().log().ifError()
                .statusCode(200)
                .body("result", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("result"), message);
    }

    @Test
    public void createExistUser() {
        code = "1204";
        message = "User exists!";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword()
                ))
                .contentType(ContentType.JSON)
                .post("Account/v1/User")
                .then().log().all()
                .statusCode(406)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    public void createNotExistUserWithWrongPassword() {
        code = "1300";
        message = "Passwords must have at least one non alphanumeric character, one digit ('0'-'9'), one " +
                "uppercase ('A'-'Z'), one lowercase ('a'-'z'), one special character and Password must be eight " +
                "characters or longer.";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", "string"
                ))
                .contentType(ContentType.JSON)
                .post("Account/v1/User")
                .then().log().all()
                .statusCode(400)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    public void checkSuccesStringUUID() {
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .header("Authorization", getToken())
                .contentType(ContentType.JSON)
                .get("/Account/v1/User/" + userId)
                .then().log().ifError()
                .statusCode(200)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    public void checkUnSuccesStringUUID() {
        code = "1200";
        message = "User not authorized!";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .header(new Header(PROP.getLogin(), PROP.getPassword()))
                .contentType(ContentType.JSON)
                .get("/Account/v1/User/" + userId)
                .then().log().all()
                .statusCode(401)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    public void getBooks() {
        List<String> response = given()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .get("BookStore/v1/Books")
                .then().log().all()
                .statusCode(200)
                .body("books", notNullValue())
                .extract().response().jsonPath().getList("books");

        Assertions.assertTrue(!response.isEmpty());
    }

    @Test
    public void sucessAddBooks() {
        List<Book.ISBN> isbnList = new ArrayList<>();
        isbnList.add(new Book.ISBN("1234567"));
        isbnList.add(new Book.ISBN("2345678"));
        Book book = Book.builder()
                .userId(userId)
                .collectionOfIsbns(isbnList)
                .build();
        List<Book> response = given()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .header("Authorization", getToken())
                .body(book)
                .post("BookStore/v1/Books")
                .then().log().all()
                .statusCode(201)
                .extract().response().jsonPath().getList("books");

        Assertions.assertTrue(!response.isEmpty());
    }

    @Test
    public void unSuccessAddBooks() {
        code = "1205";
        message = "ISBN supplied is not available in Books Collection!";
        List<Book.ISBN> isbnList = new ArrayList<>();
        isbnList.add(new Book.ISBN("ddd"));
        Book book = Book.builder()
                .userId(userId)
                .collectionOfIsbns(isbnList)
                .build();

        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .header("Authorization", getToken())
                .body(book)
                .post("BookStore/v1/Books")
                .then().log().all()
                .statusCode(400)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    private void getUUID(){
        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword()
                ))
                .post(PROP.getURL() + "Account/v1/Login");

        userId = loginResponse.jsonPath().getString("userId");
    }

}
