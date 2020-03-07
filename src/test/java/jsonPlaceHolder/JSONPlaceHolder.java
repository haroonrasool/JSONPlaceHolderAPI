package jsonPlaceHolder;

/*
 * Get a random user (userID), print out its email address to console.
 * Using this userID, get this user’s associated posts and verify they contains a valid Post IDs (an Integer between 1 and 100).
 * Do a post using same userID with a non-empty title and body, verify the correct response is returned (since this is a mock API,
 * it might not return Response code 200, so check the documentation).
 */

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JSONPlaceHolder {
    @Test
    public void getUserId() {
        Response response = given().when().get("https://jsonplaceholder.typicode.com/users?id=2")
                .then().assertThat().statusCode(200).extract().response();
        response.prettyPrint();
        List<String> email = response.jsonPath().getList("email");
        System.out.println("\nemail address" + email);
    }

    @Test
    public void userPost() {
        Response response = given().contentType(ContentType.JSON).when().get("https://jsonplaceholder.typicode.com/posts?userId=2")
                .then().assertThat().statusCode(200).extract().response();
        String responseInString = response.asString();
//        System.out.println(responseInString);

        // Using the userID, get the user’s associated posts
        JsonPath jsonPath = new JsonPath(responseInString);
//        String userPosts = jsonPath.getString("title");
        //System.out.println(userPosts);

        // and verify the Posts contain valid Post IDs (an Integer between 1 and 100).
//        String postId = response.asString();
//        System.out.println(postId);
        response.then().assertThat().body("id", everyItem(greaterThanOrEqualTo(1)));
        response.then().assertThat().body("id", everyItem(lessThanOrEqualTo(100)));
    }

    @Test
    public void postByUser() {
//    Do a post using same userID with a non-empty title and non-empty body,
//    verify the correct response is returned (since this is a mock API,
//    it might not return Response code 200, so check the documentation).
        String payload = "{\n" +
                "\"title\": \"my post title\",\n" +
                "\"post\": \"my post body\"\n" +
                "}";
        Response response = given().contentType("application/json").when().body(payload).put("https://jsonplaceholder.typicode.com/user2/post");
        response.prettyPrint();
        Assert.assertEquals(404, response.getStatusCode());
        response.then().assertThat().statusCode(404);
    }

    @Test
    public void users() {
        Response response = given().contentType(ContentType.JSON).when().get("https://jsonplaceholder.typicode.com/users");
        response.prettyPrint();
        Assert.assertEquals(200, response.getStatusCode());
        response.then().assertThat().statusCode(200)
                .body("id", hasItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    public void posts() {
        Response response = given().contentType(ContentType.JSON).when().get("https://jsonplaceholder.typicode.com/posts");
        response.prettyPrint();
        Assert.assertEquals(200, response.getStatusCode());
        response.then().assertThat().statusCode(200)
                .body("id", hasItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }
}
