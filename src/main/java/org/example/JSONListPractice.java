package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONListPractice {
    public static void main(String[] args) {
        //GOAL: Turn the list of cereals into a list of
            //CerealDTOs

        //SETUP:
        // 1.Get the JSON
        // 2.Make the ObjectMapper
        //NEW STEP: Make the TypeReference(ObjectMapper needs help w/ lists)
        //PROCESS:
        //3.Read the json + map the DTO
        //AFTERWARDS:
        //4. You play w/ the object
        File jsonFile = new File("ListOfCereals.json");
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<List<CerealDTO_Generated>>dataType = new TypeReference<>(){};

        try {
            List<CerealDTO_Generated> cereals = mapper.readValue(jsonFile, dataType);
        for(CerealDTO_Generated cereal : cereals) {
            System.out.println(cereal.getName());
        }

        //Converting DTO to JSON
        CerealDTO_Generated grapeNuts = cereals.get(2);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String grapeJSON = mapper.writeValueAsString(grapeNuts);
            System.out.println(grapeJSON);
            /*
            Makeing a PUT Request
            String JSONPUTString = "{\"name\":\"test\",\"salary\":\"123\",\"age\":\"23\"}";
            String response = makePUTRequest("https://dummy.restapiexample.com/api/v1/update/21", JSONPUTString);
            System.out.println(response);
             */

        } catch (IOException e) {
            System.out.println("Something went wrong");

        }
    }
}
