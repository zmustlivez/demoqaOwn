package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static config.Prop.PROP;

public class Specification {

    public static RequestSpecification getStartData() {
        return new RequestSpecBuilder()
                .setBaseUri(PROP.getURL())
                .setContentType(ContentType.JSON)
/*                .addHeader(
                        "Authorization", "Bearer "+ new DemoqaAPITests().getToken()
                )*/
                .setBody(Map.of(
                        "userName", PROP.getLogin(),
                        "password", PROP.getPassword())
                )
                .build();
    }
}
