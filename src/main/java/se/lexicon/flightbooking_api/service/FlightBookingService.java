package se.lexicon.flightbooking_api.service;

import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.BookFlightRequestDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;

import java.util.List;

public interface FlightBookingService {

    FlightBookingDTO bookFlight(Long flightId, BookFlightRequestDTO bookingRequest);

    void cancelFlight(Long flightId, String passengerEmail);

    List<FlightListDTO> findAll();

    List<AvailableFlightDTO> findAvailableFlights();

    List<FlightBookingDTO> findBookingsByEmail(String email);
}