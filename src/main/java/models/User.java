package models;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    public String isAdmin;
    private String authToken;

    public User(String name, String email, String password, String isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUserCredentials(){
        JSONObject userJsonRepresentation = new JSONObject();
        userJsonRepresentation.put("email",this.email);
        userJsonRepresentation.put("password", this.password);
        return userJsonRepresentation.toJSONString();
    }

    public Response authenticate(){
        Response loginResponse =
                given().
                    header("accept", "application/json").
                    header("Content-Type", "application/json").
                and().
                    body(getUserCredentials()).
                when().post("http://localhost:3000/login");

        JsonPath jsonPathEvaluator = loginResponse.jsonPath();
        setAuthToken(jsonPathEvaluator.get("authorization"));

        return loginResponse;

    }
//
//    public Response registerUserRequest(){
//
//        JSONObject userJsonRepresentation = new JSONObject();
//        userJsonRepresentation.put("nome", this.name);
//        userJsonRepresentation.put("email",this.email);
//        userJsonRepresentation.put("password", this.password);
//        userJsonRepresentation.put("administrador",this.isAdmin);
//
//        Response response =
//                given().
//                        header("accept", "application/json").
//                        header("Content-Type", "application/json").
//                and().
//                        body(userJsonRepresentation.toJSONString()).
//                when().
//                        post("http://localhost:3000/usuarios");
//
//        JsonPath jsonPathEvaluator = response.jsonPath();
//        setId(jsonPathEvaluator.get("_id"));
//
//        return response;
//    }

    public Response registerUserRequest(RequestSpecification spec){

        JSONObject userJsonRepresentation = new JSONObject();
        userJsonRepresentation.put("nome", this.name);
        userJsonRepresentation.put("email",this.email);
        userJsonRepresentation.put("password", this.password);
        userJsonRepresentation.put("administrador",this.isAdmin);

        Response response =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").
                and().
                        body(userJsonRepresentation.toJSONString()).
                when().
                        post("http://localhost:3000/usuarios");

        JsonPath jsonPathEvaluator = response.jsonPath();
        setId(jsonPathEvaluator.get("_id"));

        return response;
    }


    public Response deleteUserRequest(RequestSpecification spec){
        Response deleteUserResponse =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").
                when().
                        delete("usuarios/"+this.getId());

        return deleteUserResponse;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String isAdmin() {
        return isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setAuthToken (String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken () {
        return this.authToken;
    }

    public void setId (String id) {
        this.id = id;
    }
}
