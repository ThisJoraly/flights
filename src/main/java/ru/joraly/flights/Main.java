package ru.joraly.flights;

import com.google.gson.JsonArray;
import ru.joraly.flights.domain.Flight;
import ru.joraly.flights.service.impl.FlightServiceImpl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            JsonArray tickets = FlightServiceImpl.getInstance().getTicketsFromFile("tickets.json");
            Map<String, List<Flight>> carrierFlights = FlightServiceImpl.getInstance().getFlightsBetweenCities(tickets, "VVO", "TLV");

            for (String carrier : carrierFlights.keySet()) {
                List<Flight> flights = carrierFlights.get(carrier);
                long minFlightTime = FlightServiceImpl.getInstance().getMinFlightTime(flights);
                double priceDiff = FlightServiceImpl.getInstance().getPriceDifference(flights);

                System.out.println("Минимальное время полета для " + carrier + ": " + minFlightTime + " минут");
                System.out.println("Разница между средней ценой и медианой для " + carrier + ": " + priceDiff);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}