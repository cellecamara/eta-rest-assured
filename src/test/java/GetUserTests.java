import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.DataProvider;

public class GetUserTests extends TestBase{

    private static User validUser1;
    private static User validUser2;
    private static User validUser3;
    private static User nonExistentUser;

    @BeforeClass
    public static void generateTestData(){
        validUser1 = new User("Lais", "let@gmail.com", "123", "true");
        validUser1.registerUserRequest(SPEC);
        validUser2 = new User("Leticia", "lais@gmail.com", "321", "false");
        validUser2.registerUserRequest(SPEC);
        validUser3 = new User("Rodrigo", "rodrigo@gmail.com", "456", "true");
        validUser3.registerUserRequest(SPEC);
        nonExistentUser = new User("João", "joao@gmail.com", "321", "false");
    }

    @AfterClass
    public void removeTestData(){
        validUser1.deleteUserRequest(SPEC);
        validUser2.deleteUserRequest(SPEC);
    }

    public RequestSpecification spec = new RequestSpecBuilder()
            .addHeader("accept", "application/json")
            .setBaseUri("http://localhost:3000").build();

    @DataProvider(name = "usersData")
    public Object[][] createTestData() {
        return new Object[][]{
                {"?nome="+ validUser1.getName() + "&email=" + validUser1.getEmail(),1},
                {"?nome="+ nonExistentUser.getName(),0},
                {"?administrador=true",3},
                {"", 4}
        };
    }

    @Test(dataProvider = "usersData")
    public void shouldReturnUserAndStatus200(String query, int totalUsers) {

        given().
                spec(SPEC).
                when().
                get("usuarios"+query).
                then().
                assertThat().
                statusCode(200).
                body("quantidade",equalTo(totalUsers));
    }
}