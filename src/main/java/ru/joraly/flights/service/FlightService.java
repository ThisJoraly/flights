package ru.joraly.flights.service;

import com.google.gson.JsonArray;
import ru.joraly.flights.domain.Flight;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface FlightService {
    JsonArray getTicketsFromFile(String fileName) throws FileNotFoundException;
    Map<String, List<Flight>> getFlightsBetweenCities(JsonArray tickets, String origin, String destination);
    long getMinFlightTime(List<Flight> flights);
    double getPriceDifference(List<Flight> flights);
}
