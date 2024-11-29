package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
public class WeatherController {

    @GetMapping("/employees")
    String all() throws IOException {
//        Integer employees = (Integer) new ObjectMapper().readValue("{\"test\": 1, \"test2\": 2}", Map.class).get("test2");
//        Map<String, Object> employees = new ObjectMapper().readValue(new File("weather.json"), Map.class);
        Root employee = new ObjectMapper().readValue(new File("weather.json"), Root.class);
        String text = "data: " + employee.fact.temp.toString();
        return text;
    }
}