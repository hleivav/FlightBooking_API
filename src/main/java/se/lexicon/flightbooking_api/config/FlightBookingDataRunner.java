package se.lexicon.flightbooking_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.lexicon.flightbooking_api.entity.FlightBooking;
import se.lexicon.flightbooking_api.entity.FlightStatus;
import se.lexicon.flightbooking_api.repository.FlightBookingRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class FlightBookingDataRunner implements CommandLineRunner {

    private final FlightBookingRepository flightBookingRepository;

    @Override
    public void run(String... args) {
        // Available flights (7)
        FlightBooking flight1 = FlightBooking.builder()
                .flightNumber("FL001")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .status(FlightStatus.AVAILABLE)
                .destination("London")
                .price(199.99)
                .build();

        FlightBooking flight2 = FlightBooking.builder()
                .flightNumber("FL002")
                .departureTime(LocalDateTime.now().plusDays(2))
                .arrivalTime(LocalDateTime.now().plusDays(2).plusHours(3))
                .status(FlightStatus.AVAILABLE)
                .destination("Paris")
                .price(249.99)
                .build();

        FlightBooking flight3 = FlightBooking.builder()
                .flightNumber("FL003")
                .departureTime(LocalDateTime.now().plusDays(3))
                .arrivalTime(LocalDateTime.now().plusDays(3).plusHours(4))
                .status(FlightStatus.AVAILABLE)
                .destination("Rome")
                .price(299.99)
                .build();

        FlightBooking flight4 = FlightBooking.builder()
                .flightNumber("FL004")
                .departureTime(LocalDateTime.now().plusDays(4))
                .arrivalTime(LocalDateTime.now().plusDays(4).plusHours(1))
                .status(FlightStatus.AVAILABLE)
                .destination("Amsterdam")
                .price(179.99)
                .build();

        FlightBooking flight5 = FlightBooking.builder()
                .flightNumber("FL005")
                .departureTime(LocalDateTime.now().plusDays(5))
                .arrivalTime(LocalDateTime.now().plusDays(5).plusHours(3))
                .status(FlightStatus.AVAILABLE)
                .destination("Barcelona")
                .price(229.99)
                .build();

        FlightBooking flight6 = FlightBooking.builder()
                .flightNumber("FL006")
                .departureTime(LocalDateTime.now().plusDays(6))
                .arrivalTime(LocalDateTime.now().plusDays(6).plusHours(2))
                .status(FlightStatus.AVAILABLE)
                .destination("Berlin")
                .price(189.99)
                .build();

        FlightBooking flight7 = FlightBooking.builder()
                .flightNumber("FL007")
                .departureTime(LocalDateTime.now().plusDays(7))
                .arrivalTime(LocalDateTime.now().plusDays(7).plusHours(3))
                .status(FlightStatus.AVAILABLE)
                .destination("Vienna")
                .price(259.99)
                .build();

        // Booked flights (3)
        FlightBooking bookedFlight1 = FlightBooking.builder()
                .flightNumber("FL008")
                .passengerName("John Doe")
                .passengerEmail("john.doe@example.com")
                .departureTime(LocalDateTime.now().plusDays(1))
                .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .status(FlightStatus.BOOKED)
                .destination("Madrid")
                .price(219.99)
                .build();

        FlightBooking bookedFlight2 = FlightBooking.builder()
                .flightNumber("FL009")
                .passengerName("Jane Smith")
                .passengerEmail("jane.smith@example.com")
                .departureTime(LocalDateTime.now().plusDays(2))
                .arrivalTime(LocalDateTime.now().plusDays(2).plusHours(3))
                .status(FlightStatus.BOOKED)
                .destination("Prague")
                .price(199.99)
                .build();

        FlightBooking bookedFlight3 = FlightBooking.builder()
                .flightNumber("FL010")
                .passengerName("Mike Johnson")
                .passengerEmail("mike.johnson@example.com")
                .departureTime(LocalDateTime.now().plusDays(3))
                .arrivalTime(LocalDateTime.now().plusDays(3).plusHours(2))
                .status(FlightStatus.BOOKED)
                .destination("Copenhagen")
                .price(239.99)
                .build();

        // Save all flights
        Arrays.asList(flight1, flight2, flight3, flight4, flight5, flight6, flight7,
                        bookedFlight1, bookedFlight2, bookedFlight3)
                .forEach(flight -> {
                    try {
                        flightBookingRepository.save(flight);
                        System.out.println("Created flight: " + flight.getFlightNumber() +
                                " (Status: " + flight.getStatus() +
                                ", Destination: " + flight.getDestination() + ")");
                    } catch (Exception e) {
                        System.err.println("Error creating flight: " + flight.getFlightNumber() +
                                " - " + e.getMessage());
                    }
                });

        // Print summary
        System.out.println("\nFlight Booking Statistics:");
        System.out.println("Total flights: " + flightBookingRepository.findAll().size());
        System.out.println("Available flights: " +
                flightBookingRepository.findAll().stream()
                        .filter(f -> f.getStatus() == FlightStatus.AVAILABLE)
                        .count());
        System.out.println("Booked flights: " +
                flightBookingRepository.findAll().stream()
                        .filter(f -> f.getStatus() == FlightStatus.BOOKED)
                        .count());
    }
}