import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PostLoginTests extends TestBase {

    private static User validUser;
    private static User invalidPasswordUser;
    private static User invalidLoginUser;

    @BeforeClass
    public static void generateTestData (){
        validUser = new User("Fulano", "fulano@qa.com", "teste", "true");
        invalidPasswordUser = new User("Rodrigo", "fulano@qa.com", "teste2", "true");
        invalidLoginUser = new User("Invalido", "invalido@qa.com", "teste2", "true");
    }

    @Test
    public void shouldAuthenticateUserAndStatus200() {
        Response loginResponse = validUser.authenticate();
        loginResponse.then().assertThat().
                statusCode(200).
                body("message",equalTo("Login realizado com sucesso"));
    }

    @Test
    public void shouldNotAuthenticateUserWithWrongPasswordAndStatus401() {
        given().
                spec(SPEC).
                header("Content-Type","application/json").
        and().
                body(invalidPasswordUser.getUserCredentials()).
        when().
                post("login").
        then().
                assertThat().
                statusCode(401).
                body("message",equalTo("Email e/ou senha inválidos")).
                body("authorization", nullValue());
    }
}
