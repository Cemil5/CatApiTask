package com.thecatapi.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CatUtils {
    public static Response deleteId(String  id){
        System.out.println("id to delete = " + id);
        return given()
                  .header("x-api-key", "DEMO-API-KEY")
                  .accept(ContentType.JSON)
                  .when()
                  .delete(ConfigurationReader.get("uri")+"/"+id);
    }
}
