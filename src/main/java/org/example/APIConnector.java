package org.example;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.Scanner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//import static com.sun.security.ntlm.NTLMException.PROTOCOL;

public class APIConnector {

    //GOAL: Write a method that takes in a JSON string and ret a BabyTVDTO
    public static BabyTVDTO convertBaby(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            BabyTVDTO dto = mapper.readValue(jsonString, BabyTVDTO.class); //mapper needs the class, then it can give the dto
            return dto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public static String makeGETRequest(String url) {
        //setting up the query/call
        HttpClient client = HttpClient.newHttpClient(); //this guy connects to the internet
        URI uri = URI.create(url); //URL is turned into a URI-Uniform Resource Identifier
        HttpRequest request = HttpRequest.newBuilder()//this object holds data about the query
                .uri(uri)
                .header("Accept", "application/json")//http requests have a head & body. The head holds meta-data
                //the body has arms & legs
                .GET()
                .build();
        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString()); //where the magic happens
            //client.send(request--> results in the request...httpResponse
            int statusCode = httpResponse.statusCode(); // a secret code that tells you how it went
            //in general, 200s are good, 400-500s are bad
            if (statusCode == 200) {//means everything went OK
                return httpResponse.body();
            } else {
                // String.format is fun! Worth a Google if you're interested
                return String.format("GET request failed: %d status code received", statusCode);
                //%d is a place holder for a variable...the letter that comes after the % is what you are looking for so
                //d stands for int
            }
        } catch (IOException | InterruptedException e) {//will ret either .getMessage that happens
            return e.getMessage();
        }
    }

    public static void main(String[] args) {


        //GOAL #1: Make a dynamic URL
        //I want to use Scanner to read in a tvshow
        //And then I want to build the URL for that show
        //https://api.tvmaze.com/singlesearch/shows?q=cutthroat%20kitchen

        final String ROOT_URL = "api.tvmaze.com";
        final String PROTOCOL = "https://";
        String path = "/singlesearch/shows";

        //three steps for user input
        //0.Import the scanner
        //1.Init the scanner
        Scanner scan = new Scanner(System.in);
        //2.Prompt the user(tell them what we need!)
        System.out.println("Enter the name of a tv show");
        //3.Rec the input
        String tvShowInput = scan.nextLine();

        String queryValue = tvShowInput.replaceAll(" ", "%20").replaceAll("'", "%27");
        queryValue = queryValue.toLowerCase();

        String URL = PROTOCOL + ROOT_URL + path + "?q=" + queryValue;
        System.out.println(URL);

        try {
            String jsonResponse = makeGETRequest(URL);
            System.out.println(jsonResponse);

            BabyTVDTO DTO = convertBaby(jsonResponse);
            System.out.println("Show Name: " + DTO.getName());
            System.out.println("Show language: " + DTO.getLanguage());

            //What time does this show air?
                //Need to:
                    //Make the TVDTO w/ the data!
                    //Get the schedule
                    //Then, get the time
            ObjectMapper mapper = new ObjectMapper();
            TVDTO tvdto = mapper.readValue(jsonResponse, TVDTO.class);
            System.out.println(tvdto.getSchedule().getTime());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

