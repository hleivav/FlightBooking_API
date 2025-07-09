package se.lexicon.flightbooking_api.dto;

import java.time.LocalDateTime;

public record FlightListDTO(
    Long id,
    String flightNumber,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    String status,
    String destination,
    Double price
) {}