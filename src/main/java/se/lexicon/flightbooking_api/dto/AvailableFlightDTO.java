package se.lexicon.flightbooking_api.dto;

import java.time.LocalDateTime;

public record AvailableFlightDTO(
    Long id,
    String flightNumber,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    String destination,
    Double price
) {}