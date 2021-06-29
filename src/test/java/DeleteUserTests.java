import io.restassured.response.Response;
import models.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class DeleteUserTests extends TestBase {

    private static User userToDelete;
    private static User nonExistentUser;

    @BeforeClass
    public static void generateTestData (){
        userToDelete = new User("Marcelle", "delete_me@qa.com", "teste", "true");
        userToDelete.registerUserRequest(SPEC);
        nonExistentUser = new User("Maria", "maria@qa.com", "teste", "true");
    }

    @Test
    public void shouldDeleteUserAndStatus200() {
        Response deleteUserResponse = userToDelete.deleteUserRequest(SPEC);
        System.out.println(userToDelete.getId());

        deleteUserResponse.then().
                assertThat().
                statusCode(200).
                body("message",equalTo("Registro excluído com sucesso"));
    }

    @Test
    public void shouldValidateWhenUserDoesNotExistToDelete() {
        Response deleteUserResponse = nonExistentUser.deleteUserRequest(SPEC);
        deleteUserResponse.then().
                assertThat().
                statusCode(200).
                body("message",equalTo("Nenhum registro excluído"));
    }

}
