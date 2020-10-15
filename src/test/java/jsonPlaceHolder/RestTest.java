package jsonPlaceHolder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestTest {
    public static Response doGetRequest(String endpoint) {
        RestAssured.defaultParser = Parser.JSON;
        return
                given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().get(endpoint).
                        then().contentType(ContentType.JSON).extract().response();
    }

    public static void main(String[] args) {
        Response response = doGetRequest("https://jsonplaceholder.typicode.com/users");
        List<String> jsonResponse = response.jsonPath().getList("$");
        System.out.println("Number of records in the JSON array: " + jsonResponse.size());

        // if we wanted to get the username of all entries, we could use:
        String userNames = response.jsonPath().getString("username");
        System.out.println("Usernames of all entries" + userNames);

        // If we then want to get the username of the first entry we could use:
        System.out.print("Username of the first entry: ");
        String userNames2 = response.jsonPath().getString("username[0]");

        // Using a List we can use:
        List<String> jsonResponse2 = response.jsonPath().getList("username");
        System.out.println(jsonResponse2.get(0));

        //Parsing JSON ArrayList and HashMap
        //Looking at the above JSON structure, the company is actually a map. If we only had one record, we could use:
        Response response3 = doGetRequest("https://jsonplaceholder.typicode.com/users/1");
        Map<String, String> company = response3.jsonPath().getMap("company");
        System.out.println(company.get("name"));

        // But if the response returns an array and we want to extract the first company name, we could use:
        Response response4 = doGetRequest("https://jsonplaceholder.typicode.com/users/");
        Map<String, String> company2 = response4.jsonPath().getMap("company[0]");
        System.out.println(company2.get("name"));

        // Alternatively, we could use:
        Response response5 = doGetRequest("https://jsonplaceholder.typicode.com/users/");
        List<Map<String, String>> companies = response5.jsonPath().getList("company");
        System.out.println(companies.get(0).get("name"));
    }
}
