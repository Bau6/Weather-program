package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class WeatherProgram {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();

        try {
//            JsonNode weatherData = mapper.readTree(new File("weather.json")); // можно взять данные из файла
            String key = "3ee80feb-137c-4efd-8cfa-cedaf33c0bc6";
            String headerName = "X-Yandex-Weather-Key";
            String headerValue = key;
            HttpClient client = HttpClient.newHttpClient();
            String uri = "https://api.weather.yandex.ru/v2/forecast?lat=59.773002&lon=30.59422";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header(headerName, headerValue)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            JsonNode weatherData = mapper.readTree(body);
            System.out.println("Все данные: " + weatherData);
            double currentTemp = weatherData.get("fact").get("temp").asDouble();
            System.out.println("Текущая температура: " + currentTemp + "℃");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите дату в формате \"ГГГГ-ММ-ДД\": ");
            String dateString = scanner.nextLine();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);

            JsonNode forecast = null;
            ArrayNode forecasts = (ArrayNode) weatherData.get("forecasts");
            for (JsonNode forecastItem : forecasts) {
                if (forecastItem.get("date").asText().equals(dateString)) {
                    forecast = forecastItem;
                    break;
                }
            }

            if (forecast == null) {
                System.out.println("Прогноз погоды на эту дату не найден");
                return;
            }

            JsonNode dayShort = forecast.get("parts").get("day_short");
            double dayShortTemp = dayShort.get("temp").asDouble();
            JsonNode nightShort = forecast.get("parts").get("night_short");
            double nightShortTemp = nightShort.get("temp").asDouble();

            double avgTemp = (dayShortTemp + nightShortTemp) / 2;

            System.out.println("Средняя температура суток за " + dateString + " : " + avgTemp + "℃");
        } catch (ParseException e) {
            System.out.println("Неверный формат даты");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
