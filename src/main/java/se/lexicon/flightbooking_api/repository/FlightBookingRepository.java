package se.lexicon.flightbooking_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.flightbooking_api.entity.FlightBooking;
import se.lexicon.flightbooking_api.entity.FlightStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    
    // Find flights by status (available, booked, cancelled)
    List<FlightBooking> findByStatus(FlightStatus status);
    
    // Find bookings by passenger email
    List<FlightBooking> findByPassengerEmail(String email);
    
    // Find flights by destination
    List<FlightBooking> findByDestination(String destination);
    
    // Find flights by departure time range
    List<FlightBooking> findByDepartureTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Find flights by flight number
    FlightBooking findByFlightNumber(String flightNumber);
    
    // Find available flights to a specific destination
    List<FlightBooking> findByStatusAndDestination(FlightStatus status, String destination);
    
    // Find flights below a certain price
    List<FlightBooking> findByPriceLessThanEqual(Double maxPrice);
}