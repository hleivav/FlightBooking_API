package se.lexicon.flightbooking_api.dto;

public record BookFlightRequestDTO(
    String passengerName,
    String passengerEmail
) {}