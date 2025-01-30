package com.hszg.demo.controller;

import com.hszg.demo.service.WeatherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // GET /api/weather?city=Berlin
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWeather(@RequestParam String city) {
        return weatherService.getCurrentWeather(city);
    }
}