package com.thecatapi.step_definitions;

import com.thecatapi.utilities.CatUtils;
import com.thecatapi.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class CatTask {
    String url = ConfigurationReader.get("uri");
    Response response;
    List<Map<String ,Object>> allVoteList;
    Map<String ,Object> randomElement;
    Integer idToKeep;

    @When("the user gets votes information using token")
    public void the_user_gets_votes_information_using_token() {
        response = given().accept(ContentType.JSON)
                .header("x-api-key", "DEMO-API-KEY")
                .when().get(url);
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int code) {
        Assert.assertEquals(code, response.statusCode());
    }

    @Then("length of the response result is more than {int}")
    public void length_of_the_response_result_is_more_than(Integer int1) {
        Assert.assertTrue(response.header("Content-Length").length() > 0);
    }

    @Given("the user gets all votes")
    public void the_user_gets_all_votes() {
        the_user_gets_votes_information_using_token();
        allVoteList = response.body().as(List.class);
      //  System.out.println(allVoteList);
    }


    @When("the user selects a random element")
    public void the_user_selects_a_random_element() {
        Random random = new Random();
        int rnNum = random.nextInt(allVoteList.size());
        randomElement = allVoteList.get(rnNum);
        double idDouble = (double)randomElement.get("id");
        int id = (int) idDouble;
        response = given().accept(ContentType.JSON)
                .header("x-api-key", "DEMO-API-KEY")
                .when().get(url + "/"+id);
        Assert.assertEquals(200, response.statusCode());
        response.prettyPrint();
        System.out.println("randomElement : " + randomElement);
    }

    @Then("all field in response object match the selected element")
    public void all_field_in_response_object_match_the_selected_element() {
        Map<String ,Object> actual = response.body().as(Map.class);
        actual.remove("user_id");
        Assert.assertEquals(randomElement, actual);
    }

    @When("the user create a new vote with image id {string} and sub id {string} and value {int}")
    public void the_user_create_a_new_vote_with_image_id_and_sub_id_and_value(String imgId, String subId,
                                                                              int value) {
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("image_id", imgId);
        postMap.put("sub_id", subId);
        postMap.put("value", value);

        response = given() //.log().all()
                .accept(ContentType.JSON)
                .header("x-api-key", "DEMO-API-KEY")
                .and().contentType(ContentType.JSON)
                .and().body(postMap)
                .when()
                .post(url);
      //  response.body().prettyPrint();
    }

    @Then("body response has {string} message")
    public void body_response_has_message(String expectedMessage) {
        String actual = response.path("message");
      //  System.out.println("actual = " + actual);
        Assert.assertEquals(expectedMessage, actual);
    }

    @Then("id response is not empty")
    public void id_respones_is_not_empty() {
        Integer actual = response.path("id");
        System.out.println("actual id = " + actual);
        Assert.assertTrue(actual > 0);
    }

    @Given("the user creates a new vote and gets its id")
    public void the_user_creates_a_new_vote_and_gets_its_id() {
        the_user_create_a_new_vote_with_image_id_and_sub_id_and_value("asf2", "my-user-1234", 1);
        Assert.assertEquals(200, response.statusCode());
    }

    @When("the user deletes this id")
    public void the_user_deletes_this_id() {
        Integer id = response.path("id");
        CatUtils.deleteId(id);
    }

    @Given("the user deletes a vote and keeps its id")
    public void the_user_deletes_a_vote_and_keeps_its_id() {
        the_user_create_a_new_vote_with_image_id_and_sub_id_and_value("asf2", "my-user-1234", 1);
        idToKeep = response.path("id");
        CatUtils.deleteId(idToKeep);
    }

    @When("the user tries to get this element")
    public void theUserTriesToGetThisElement() {
        response = given().accept(ContentType.JSON)
                .header("x-api-key", "DEMO-API-KEY")
                .when().get(url + "/"+idToKeep);
    }
}
