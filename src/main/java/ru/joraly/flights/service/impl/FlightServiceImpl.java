package ru.joraly.flights.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.joraly.flights.domain.Flight;
import ru.joraly.flights.service.FlightService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {

    private static FlightServiceImpl instance;

    private FlightServiceImpl() {
    }

    public static FlightService getInstance() {
        if (instance == null) {
            instance = new FlightServiceImpl();
        }
        return instance;
    }

    @Override
    public JsonArray getTicketsFromFile(String fileName) throws FileNotFoundException {
        JsonElement fileElement = JsonParser.parseReader(new FileReader(fileName));
        JsonObject fileObject = fileElement.getAsJsonObject();
        return fileObject.get("tickets").getAsJsonArray();
    }

    public Map<String, List<Flight>> getFlightsBetweenCities(JsonArray tickets, String origin, String destination) {
        Map<String, List<Flight>> carrierFlights = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");

        for (JsonElement ticketElement : tickets) {
            JsonObject ticketObject = ticketElement.getAsJsonObject();

            if (ticketObject.get("origin").getAsString().equals(origin) && ticketObject.get("destination").getAsString().equals(destination)) {
                String carrier = ticketObject.get("carrier").getAsString();
                LocalTime departureTime = LocalTime.parse(ticketObject.get("departure_time").getAsString(), formatter);
                LocalTime arrivalTime = LocalTime.parse(ticketObject.get("arrival_time").getAsString(), formatter);
                long flightTime = ChronoUnit.MINUTES.between(departureTime, arrivalTime);
                int price = ticketObject.get("price").getAsInt();

                Flight flight = new Flight(flightTime, price);
                carrierFlights.computeIfAbsent(carrier, k -> new ArrayList<>()).add(flight);
            }
        }

        return carrierFlights;
    }

    @Override
    public long getMinFlightTime(List<Flight> flights) {
        return flights.stream().mapToLong(Flight::flightTime).min().orElse(0);
    }

    @Override
    public double getPriceDifference(Map<String, List<Flight>> carrierFlights) {
        List<Integer> prices = new ArrayList<>();
        for (List<Flight> flights : carrierFlights.values()) {
            prices.addAll(flights.stream().mapToInt(Flight::price).boxed().toList());
        }
        Collections.sort(prices);
        double medianPrice;
        if (prices.size() % 2 == 0)
            medianPrice = ((double) prices.get(prices.size() / 2) + (double) prices.get(prices.size() / 2 - 1)) / 2;
        else
            medianPrice = (double) prices.get(prices.size() / 2);
        double averagePrice = prices.stream().mapToInt(val -> val).average().orElse(0.0);
        return Math.abs(averagePrice - medianPrice);
    }


}
