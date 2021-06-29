import io.restassured.response.Response;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

public class PostUserTests extends TestBase {

    private static User validUser;
    private static User invalidUser;

    @BeforeClass
    public static void generateTestData (){
        validUser = new User("Helena", "helena@gmail.com", "123", "false");
        invalidUser = new User("Fulano", "fulano@qa.com", "643", "true");
    }

    @AfterClass
    public void removeTestData(){
        validUser.deleteUserRequest(SPEC);
    }

    @Test
    public void shouldCreateUserAndStatus201() {

        Response userResponse = validUser.registerUserRequest(SPEC);
        userResponse.then().assertThat().
                statusCode(201).
                body("message",equalTo("Cadastro realizado com sucesso"));
    }

    @Test
    public void shouldNotCreateUserWithExistingEmailAndStatus400() {

        Response userResponse = invalidUser.registerUserRequest(SPEC);
        userResponse.then().assertThat().
                statusCode(400).
                body("message",equalTo("Este email já está sendo usado"));
    }
}