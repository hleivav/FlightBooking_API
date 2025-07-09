package se.lexicon.flightbooking_api.dto;

import java.time.LocalDateTime;

public record FlightBookingDTO(
    Long id,
    String flightNumber,
    String passengerName,
    String passengerEmail,
    LocalDateTime departureTime,
    LocalDateTime arrivalTime,
    String status,
    String destination,
    Double price
) {}