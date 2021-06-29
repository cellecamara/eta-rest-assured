import io.restassured.response.Response;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class PostProductTests extends TestBase {

    private static Product product1;
    private static User validUser;
    private static User nonAdminUser;
    private static User invalidUser;

    @BeforeClass
    public static void generateTestData (){
        nonAdminUser = new User("Helena", "hc@gmail.com", "123", "false");
        nonAdminUser.registerUserRequest(SPEC);
        validUser = new User("Cris", "cris@gmail.com", "123", "true");
        validUser.registerUserRequest(SPEC);
        product1 = new Product("MacBook Pro", "5000", "Macbook Air 13 - 256GB SSD Dourado", 5);
        invalidUser = new User("Non existent", "non@qa.com", "643", "true");
    }

    @AfterClass
    public void removeTestData(){
        nonAdminUser.deleteUserRequest(SPEC);
        validUser.deleteUserRequest(SPEC);
    }

    @Test
    public void shouldCreateProductAndStatus201() {
        validUser.authenticate();

        Response prodResponse = product1.registerProductRequest(validUser.getAuthToken(), SPEC);
        prodResponse.then().assertThat().
                statusCode(201).
                body("message",equalTo("Cadastro realizado com sucesso"));
    }

    @Test
    public void shouldNotCreateProductWhenNotAdminAndStatus403() {

        nonAdminUser.authenticate();
        Response prodResponse = product1.registerProductRequest(nonAdminUser.getAuthToken(), SPEC);
        prodResponse.then().assertThat().
                statusCode(403).
                body("message",equalTo("Rota exclusiva para administradores"));
    }

    @Test
    public void shouldNotCreateProductWithExistingNameAndStatus400() {

        validUser.authenticate();
        Response prodResponse = product1.registerProductRequest(validUser.getAuthToken(), SPEC);
        prodResponse.then().assertThat().
                statusCode(400).
                body("message",equalTo("Já existe produto com esse nome"));
    }

    @Test
    public void shouldNotCreateProductWithInvalidTokenAndStatus401() {

        validUser.setAuthToken("323-109301293-01203");
        Response prodResponse = product1.registerProductRequest(validUser.getAuthToken(), SPEC);
        prodResponse.then().assertThat().
                statusCode(401).
                body("message",equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }

    @Test
    public void shouldNotCreateProductWithoutTokenAndStatus401() {

        validUser.setAuthToken("");
        Response prodResponse = product1.registerProductRequest(validUser.getAuthToken(), SPEC);
        prodResponse.then().assertThat().
                statusCode(401).
                body("message",equalTo("Token de acesso ausente, inválido, expirado ou usuário do token não existe mais"));
    }
}