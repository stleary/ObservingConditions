package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import org.json.JSONObject;

/**
 * Calculates phases of the moon for a given date in a very basic and limited way
 */
public class MoonPhaseCalculatorRest implements MoonPhaseCalculator {


    private static final String API_URL = "https://aa.usno.navy.mil/api/rstt/oneday";


    @Override
    public MoonPhase getMoonPhase(LocalDate date) {
        try {
            String json = fetchJson(date);
            return parseMoonPhase(json);
        } catch (Exception e) {
            // the site is down, the network is off, or the response was not what we expected
            System.out.println("Unable to get moon phase: " + e);
            return null;
        }
    }

    @Override
    public MoonPhase parseMoonPhase(String sampleResponse) {
        JSONObject root = new JSONObject(sampleResponse);
        JSONObject properties = root.getJSONObject("properties");
        JSONObject data = properties.getJSONObject("data");
        String curPhase = data.getString("curphase");

        for (MoonPhase moonPhase : MoonPhase.values()) {
            if (moonPhase.getName().equals(curPhase)) {
                return moonPhase;
            }
        }
        return null;
    };

    /**
     * Calls the REST API and returns the response body, which is a JSON document
     */
    private String fetchJson(LocalDate date) throws Exception {
        // date.toString() is already in the yyyy-MM-dd format the API wants
        String url = API_URL + "?date=" + date + "&coords=0,0";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Unexpected status code: " + response.statusCode());
        }
        return response.body();
    }
}
 