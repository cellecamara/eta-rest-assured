package models;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

public class Product {
    private String id;
    private String name;
    private String price;
    private String description;
    public int quantity;

    public Product(String name, String price, String description, int qty) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = qty;
    }
    public Response registerProductRequest(String userToken, RequestSpecification spec) {

        JSONObject prodJsonRepresentation = new JSONObject();
        prodJsonRepresentation.put("nome", this.name);
        prodJsonRepresentation.put("preco",this.price);
        prodJsonRepresentation.put("descricao", this.description);
        prodJsonRepresentation.put("quantidade",this.quantity);

        Response response =
                given().
                        spec(spec).
                        header("Content-Type", "application/json").
                        header("Authorization", userToken).
                and().
                        body(prodJsonRepresentation.toJSONString()).
                when().
                        post("produtos");


        JsonPath jsonPathEvaluator = response.jsonPath();
        setId(jsonPathEvaluator.get("_id"));

        return response;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
