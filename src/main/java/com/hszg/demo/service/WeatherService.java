package com.hszg.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.baseurl}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Holt aktuelles Wetter für city via OpenWeatherMap
     * und gibt ein kleines JSON-Objekt zurück (als String),
     * z.B. {"city":"Berlin","temperature":10.0,"description":"clear sky","icon":"01d"}
     */
    public String getCurrentWeather(String city) {
        try {
            // URL z.B. https://api.openweathermap.org/data/2.5/weather?q=Berlin&appid=XYZ&units=metric
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric",
                    baseUrl, city, apiKey);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String jsonBody = response.getBody();

                // Jackson zum Parsen
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(jsonBody);

                // Hole Temperatur
                double temp = root.path("main").path("temp").asDouble();
                // Hole Beschreibung
                String description = root.path("weather").get(0).path("description").asText();
                // Hole Icon-Code z.B. "04d"
                String iconCode = root.path("weather").get(0).path("icon").asText();

                // Baue eigenes "leichtes" JSON
                ObjectNode result = mapper.createObjectNode();
                result.put("city", city);
                result.put("temperature", temp);
                result.put("description", description);
                result.put("icon", iconCode);

                return result.toString();
            } else {
                // Fehlerfall (z.B. 404 city not found)
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode errorJson = mapper.createObjectNode();
                errorJson.put("error", "API call failed, status = " + response.getStatusCodeValue());
                return errorJson.toString();
            }
        } catch (Exception e) {
            // Catch generische Fehler
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode errorJson = mapper.createObjectNode();
            errorJson.put("error", e.getMessage());
            return errorJson.toString();
        }
    }
}