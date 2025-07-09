package se.lexicon.flightbooking_api.mapper;

import org.springframework.stereotype.Component;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;
import se.lexicon.flightbooking_api.entity.FlightBooking;

@Component
public class FlightBookingMapper {
    
    public FlightBookingDTO toDTO(FlightBooking entity) {
        return new FlightBookingDTO(
            entity.getId(),
            entity.getFlightNumber(),
            entity.getPassengerName(),
            entity.getPassengerEmail(),
            entity.getDepartureTime(),
            entity.getArrivalTime(),
            entity.getStatus().toString(),
            entity.getDestination(),
            entity.getPrice()
        );
    }
    
    public AvailableFlightDTO toAvailableFlightDTO(FlightBooking entity) {
        return new AvailableFlightDTO(
            entity.getId(),
            entity.getFlightNumber(),
            entity.getDepartureTime(),
            entity.getArrivalTime(),
            entity.getDestination(),
            entity.getPrice()
        );
    }

    public FlightListDTO toListDTO(FlightBooking entity) {
        return new FlightListDTO(
                entity.getId(),
                entity.getFlightNumber(),
                entity.getDepartureTime(),
                entity.getArrivalTime(),
                entity.getStatus().toString(),
                entity.getDestination(),
                entity.getPrice()
        );
    }

}