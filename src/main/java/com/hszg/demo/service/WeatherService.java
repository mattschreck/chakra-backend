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
     * Ruft das aktuelle Wetter für 'city' ab und gibt es als vereinfachtes JSON (String) zurück.
     */
    public String getCurrentWeather(String city) {
        try {
            // Beispiel-URL: https://api.openweathermap.org/data/2.5/weather?q=Cottbus&appid=APIKEY&units=metric
            String url = String.format("%s/weather?q=%s&appid=%s&units=metric",
                    baseUrl, city, apiKey);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Original-Antwort als String
                String jsonBody = response.getBody();

                // Jackson ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                // in ein JsonNode parsen
                JsonNode root = mapper.readTree(jsonBody);

                // temp in root.main.temp
                double temp = root.path("main").path("temp").asDouble();
                // description in root.weather[0].description
                String description = root.path("weather").get(0).path("description").asText();

                // Jetzt ein eigenes "leichtes" JSON bauen
                ObjectNode result = mapper.createObjectNode();
                result.put("city", city);
                result.put("temperature", temp);
                result.put("description", description);

                return result.toString();  // -> {"city":"Cottbus","temperature":13.37,"description":"broken clouds"}

            } else {
                // Wenn OWM-Call fehlschlägt
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode error = mapper.createObjectNode();
                error.put("error", "API call failed with status: " + response.getStatusCodeValue());
                return error.toString();
            }
        } catch (Exception e) {
            // Falls ein Fehler auftritt (z. B. falsche Stadt)
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode error = mapper.createObjectNode();
            error.put("error", e.getMessage());
            return error.toString();
        }
    }
}