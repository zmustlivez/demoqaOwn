package api;

import api.models.Book;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static config.Prop.PROP;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoqaAPITests {
    private String code;
    private String message;
    private static String token;
    private static String userId;

    @BeforeAll
    static void setup() {
        // 1. Логинимся и получаем сразу TOKEN и USERID
        Response response = given()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", PROP.getLogin(), "password", PROP.getPassword()))
                .post(PROP.getURL() + "Account/v1/Login");

        if (response.getStatusCode() == 200 && !response.asString().isEmpty()) {
            // Если залогинились успешно — сохраняем данные
            token = response.jsonPath().getString("token");
            userId = response.jsonPath().getString("userId");
        } else {
            // логика создания...

            given()
                    .when()
                    .baseUri(PROP.getURL())
                    .body(Map.of(
                            "userName", PROP.getLogin(),
                            "password", PROP.getPassword()
                    ))
                    .contentType(ContentType.JSON)
                    .post("Account/v1/User")
                    .then()
                    .statusCode(201);

            Response responsecreateUser = given()
                    .when()
                    .baseUri(PROP.getURL())
                    .body(Map.of(
                            "userName", PROP.getLogin(),
                            "password", PROP.getPassword()
                    ))
                    .contentType(ContentType.JSON)
                    .post("Account/v1/User")
                    .then().log().ifError()
                    .extract().response();
            token = responsecreateUser.jsonPath().getString("token");
            userId = responsecreateUser.jsonPath().getString("userID");


            given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("userName", PROP.getLogin(), "password", PROP.getPassword()))
                    .post(PROP.getURL() + "Account/v1/Authorized")
                    .then()
                    .statusCode(200);
        }


    }

    @Test
    @Order(1)
    public void checkSuccessAutorization() {
        checkSuccessGenerationToken();
        Response response = given()
                .log().all()
                .when()
                .spec(Specification.getStartData())
                .post("Account/v1/Authorized")
                .then().log().all()
                .statusCode(200)
                .body("", equalTo(true))
                .extract().response();
        Assertions.assertEquals(true, response.jsonPath().get(""));
    }

    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
                .then().log().ifError()
                .statusCode(406)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    @Order(7)
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
                .then().log().ifError()
                .statusCode(400)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    @Order(100)
    public void createNotExistUserWithRightPassword() {
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
//                .header("Authorization", getToken())
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword()
                ))
                .contentType(ContentType.JSON)
                .post("Account/v1/User")
                .then().log().ifError()
                .statusCode(201)
                .body("userID", notNullValue())
                .body("username", equalTo(PROP.getLogin()))
                .extract().response();

        Assertions.assertNotNull(response.jsonPath().get("userID"));
        Assertions.assertNotNull(response.jsonPath().get("username"));

    }

    @Test
    @Order(99)
    public void checkSuccessDeleteExistUser() {
        given()
//                .log().all()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
/*
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword()
                ))*/
                .delete("Account/v1/User/" + userId)
                .then().log().ifError()
                .statusCode(204);
    }

    @Test
    @Order(8)
    public void checkSuccessGetExistUser() {
        Response response = given()
                .baseUri(PROP.getURL())
//                .body(Map.of("userName", PROP.getLogin(), "password", PROP.getPassword()))

                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .when()
                .get("Account/v1/User/" + userId)
                .then().log().ifError()
                .statusCode(200)
                .body("userId", notNullValue())
                .body("username", equalTo(PROP.getLogin()))
                .extract().response();

        Assertions.assertNotNull(response.jsonPath().get("userId"));
        Assertions.assertEquals(response.jsonPath().get("username"), PROP.getLogin());
    }


    @Test
    @Order(9)
    public void checkSuccesStringUUID() {

        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .header("Authorization", "Bearer " + token)
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
    @Order(10)
    public void checkUnSuccessStringUUID() {
        code = "1200";
        message = "User not authorized!";
        Response response = given()
                .when()
                .baseUri(PROP.getURL())
                .header(new Header(PROP.getLogin(), PROP.getPassword()))
                .contentType(ContentType.JSON)
                .get("/Account/v1/User/" + userId)
                .then().log().ifError()
                .statusCode(401)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    @Order(11)
    public void checkSuccessGetBooks() {
        List<String> response = given()
                .when()
                .baseUri(PROP.getURL())
                .contentType(ContentType.JSON)
                .get("BookStore/v1/Books")
                .then().log().ifError()
                .statusCode(200)
                .body("books", notNullValue())
                .extract().response().jsonPath().getList("books");

        System.out.println(response);
        Assertions.assertTrue(!response.isEmpty());
    }

    @Test
    @Order(12)
    public void checkSuccessAddBooks() {
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
                .header("Authorization", "Bearer " + token)
                .body(book)
                .post("BookStore/v1/Books")
                .then().log().ifError()
                .statusCode(201)
                .extract().response().jsonPath().getList("books");

        Assertions.assertTrue(!response.isEmpty());
    }

    @Test
    @Order(13)
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
                .header("Authorization", "Bearer " + token)
                .body(book)
                .post("BookStore/v1/Books")
                .then().log().ifError()
                .statusCode(400)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(response.jsonPath().get("code"), code);
        Assertions.assertEquals(response.jsonPath().get("message"), message);
    }

    @Test
    @Order(14)
    public void checkSuccessDeleteBooks() {
        message = "";
        if (!token.isEmpty() || !userId.isEmpty()) {
            given()
                    .log().all()
                    .when()
                    .baseUri(PROP.getURL())
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .queryParam("UserId", userId)
                    .delete("BookStore/v1/Books")
                    .then().log().ifError()
                    .statusCode(204);
        }
    }

    @Test
    @Order(15)
    public void checkUnSuccessPutWrongIsbnBook() {
        code = "1205";
        message = "ISBN supplied is not available in Books Collection!";
        String isbn = "34567890";
        Response response = given()
                .log().all()
                .when()
                .baseUri(PROP.getURL())
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("userId", userId, "isbn", isbn))
                .put("BookStore/v1/Books/" + isbn)
                .then().log().all()
                .statusCode(400)
                .body("code", equalTo(code))
                .body("message", equalTo(message))
                .extract().response();

        Assertions.assertEquals(code, response.jsonPath().get("code"));
        Assertions.assertEquals(message, response.jsonPath().get("message"));
    }

    @Test
    @Order(16)
    public void checkSuccessPutBook() {
        String oldIsbn = "9781449331818";
        String newIsbn = "9781593277574";

        given()
                .baseUri(PROP.getURL())
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "userId", userId,
                        "collectionOfIsbns", List.of(Map.of("isbn", oldIsbn))
                ))
                .post("BookStore/v1/Books")
                .then()
                .statusCode(201); // Убедились, что книга добавлена

        Response response = given()
                .log().all()
                .when()
                .baseUri(PROP.getURL())
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("userId", userId, "isbn", newIsbn))
                .put("BookStore/v1/Books/" + oldIsbn)
                .then().log().all()
                .statusCode(200)
                .body("userId", equalTo(userId))
                .body("username", equalTo(PROP.getLogin()))
                .extract().response();

        Assertions.assertEquals(userId, response.jsonPath().get("userId"));
        Assertions.assertEquals(PROP.getLogin(), response.jsonPath().get("username"));
    }

/*    private void getUUID() {
        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword()
                ))
                .post(PROP.getURL() + "Account/v1/Login");
        System.out.println(loginResponse.jsonPath().getString("userId"));
        writeKeyValue("userId", loginResponse.jsonPath().getString("userId"));
    }*/

/*    private void writeKeyValue(String key, String value) {
        Path path = Paths.get("src/test/resources/variables.values");
        try {

            List<String> linesVariablesValues = Files.readAllLines(path);

            boolean foundKey = false;
            String lineKeyValue = key + " = " + value;
            for (int i = 0; i < linesVariablesValues.size(); i++) {
                if (linesVariablesValues.get(i).startsWith(key + " =")) {
                    linesVariablesValues.set(i, lineKeyValue);
                    foundKey = true;
                    break;
                }
            }
            if (!foundKey) {
                linesVariablesValues.add("\n" + lineKeyValue);
            }
            Files.write(path, linesVariablesValues);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

}
