package org.ulpgc.dacd.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Rate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class XoteloSupplier implements HotelSupplier {

    public Hotel getHotel(List<String> hotelData, String checkIn, String checkOut) {
        Hotel hotel = null;
        try {
            String apiUrl = buildApiUrl(hotelData.get(2), checkIn, checkOut);
            String jsonResponse = fetchDataFromApi(apiUrl);
            hotel = createHotel(jsonResponse, hotelData);
            System.out.println(hotel);
            return hotel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hotel;
    }

    private String buildApiUrl(String hotelKey, String checkIn, String checkOut) {
        return "https://data.xotelo.com/api/rates?" +
                "hotel_key=" + hotelKey +
                "&chk_in=" + checkIn +
                "&chk_out=" + checkOut +
                "&currency=EUR";
    }

    private String fetchDataFromApi(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error: " + responseCode);
        } else {
            return readApiResponse(url);
        }
    }

    private String readApiResponse(URL url) throws IOException {
        StringBuilder informationString = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
        }
        return informationString.toString();
    }

    private Hotel createHotel(String jsonResponse, List<String> hotelData) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject result = jsonObject.getAsJsonObject("result");
        List<Rate> rates = createRateList(result.get("rates").getAsJsonArray());
        return new Hotel(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                "Xotelo",
                hotelData.get(0),
                hotelData.get(1),
                result.get("chk_in").getAsString(),
                result.get("chk_out").getAsString(),
                rates
                );
    }

    private List<Rate> createRateList(JsonArray jsonRates) {
        List<Rate> rates = new ArrayList<>();
        for (int i = 0; i < jsonRates.size(); i++) {
            JsonObject jsonRate = jsonRates.get(i).getAsJsonObject();
            Rate rate = new Rate(
                    jsonRate.get("name").getAsString(),
                    jsonRate.get("rate").getAsDouble()
            );
            rates.add(rate);
        }
        return rates;
    }

}
