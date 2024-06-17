package me.andidroid.testwar.cucumber;

import static io.restassured.RestAssured.given;
import io.restassured.matcher.RestAssuredMatchers.*;

public class TestStepDefinition {


    @Test
    public void testGetById()
    {
        
        me.andidroid.testwar.Test test = given().accept("application/json").get("testwar/testservice/test/1").then().statusCode(200).contentType("application/json").extract().as(me.andidroid.testwar.Test.class);
        
        Assertions.assertEquals(1l, test.getId());
        Assertions.assertEquals("Test 1", test.getText());
    }

}
