package org.example.disneyapi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisneyCastViewer {
    final static String ROOT_URL = "https://api.disneyapi.dev/character";

    public static CastDTO convert(String json){
        ObjectMapper mapper = new ObjectMapper();
        try{
            CastDTO dto = mapper.readValue(json, CastDTO.class);
            return dto;
        }catch(Exception b) {
            System.out.println("Something went wrong");
            return null;
        }
    }

    public static String getFilm(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your favorite Disney movie title: ");
        String movie = scan.nextLine();
        return movie;
    }
    //GOAL: Take in a movie name and return the formatted URL
    //parameter? --> String movieTitle
    //ret type? --> String URL
    //https://api.disneyapi.dev/character
    public static String formatURL(String URL, String movieTitle){
        movieTitle = movieTitle.replaceAll(" ", "%20");
        movieTitle = movieTitle.replaceAll("'", "%27");
        String bigBoyURL = URL + "?" + "films=" + movieTitle;
        return bigBoyURL;

    }

    public static String letsGETit(String URL){
        HttpClient client = HttpClient.newHttpClient(); //this guy connects to the internet
        URI uri = URI.create(URL); //URL is turned into a URI-Uniform Resource Identifier
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
    //GOAL: Take a list of characters ret a list of URLs
    //String URL
    // If stat. on the movie name
    //inputs: movie names outputs:
    public static List<String> grabURLs(List<Data> people, String movie){
        ArrayList<String> castURL = new ArrayList<>();
        for(Data c : people){
            if(c.getFilms().contains(movie)){
                String image = c.getImageUrl();
                castURL.add(image);//to add to an ArrayList
            }
        }
        return castURL;
    }
    //GOAL: Write a HTML file that has all the image URLs
    /*
    HTMLS
    <html>
    <head></head>
    <body>
    <img src = "link">
    </body>
    </html>
    StringBuilder
    try/catch
    FileWriter & BufferedWriter & close
     */
    public static void writeToFile(List<String> imgURL){
        try{
            FileWriter fw = new FileWriter("index.html");
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write("<html>\n");
            writer.write("<head></head>\n");
            writer.write("<body>\n");
            //TODO: Insert img links
            for(int i = 0; i < imgURL.size(); i++){
                //Get out current URL
                String currURL = imgURL.get(i);
                writer.write("<img src = \"");
                writer.write(currURL);
                writer.write("\">\n");//close quote, close >, next line
            }
            writer.write("</body>\n");
            writer.write("</html>");


            writer.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
         String movie = getFilm();
        System.out.println(movie);
        String URL = formatURL(ROOT_URL, movie);
        System.out.println(URL);
        String json = letsGETit(URL);
        System.out.println(json);

        CastDTO dto = convert(json);
        List<Data> characters = dto.getData();
        writeToFile(grabURLs(characters, movie));
        /*
        System.out.println(characters.get(0).getName());
        for(Data character : characters) {
            if(character.getFilms().contains(movie)) {
                System.out.print(character.getName());
               // System.out.println(character.getFilms().contains(movie));
            }
        }

         */
    }
}
