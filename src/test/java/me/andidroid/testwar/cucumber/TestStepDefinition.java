package me.andidroid.testwar.cucumber;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;

import static io.restassured.RestAssured.given;
import io.restassured.matcher.RestAssuredMatchers.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


public class TestStepDefinition {

    @Given("Given test data is present")
    public void givenTest()
    {

    }

    @When("When users sends get request")
    public void whenTest()
    {

    }

    @Then("When users sends get request")
    public void thenTest()
    {
        me.andidroid.testwar.Test test = given().accept("application/json").get("testwar/testservice/test/1").then().statusCode(200).contentType("application/json").extract().as(me.andidroid.testwar.Test.class);
        
        Assertions.assertEquals(1l, test.getId());
        Assertions.assertEquals("Test 1", test.getText());
    }



}
