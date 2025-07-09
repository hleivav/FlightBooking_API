package se.lexicon.flightbooking_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.BookFlightRequestDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;
import se.lexicon.flightbooking_api.dto.FlightListDTO;
import se.lexicon.flightbooking_api.service.FlightBookingService;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Tag(name = "Flight Booking API", description = "APIs for flight booking operations")
public class FlightBookingController {

    private final FlightBookingService flightBookingService;

    @Operation(summary = "Get all flights", description = "Returns a list of all flights")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all flights")
    @GetMapping
    public ResponseEntity<List<FlightListDTO>> getAllFlights() {
        return ResponseEntity.ok(flightBookingService.findAll());
    }

    @Operation(summary = "Get available flights", description = "Returns a list of all available flights")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available flights")
    @GetMapping("/available")
    public ResponseEntity<List<AvailableFlightDTO>> getAvailableFlights() {
        return ResponseEntity.ok(flightBookingService.findAvailableFlights());
    }

    @Operation(summary = "Book a flight", description = "Books a flight for a passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight successfully booked"),
            @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Flight not available", content = @Content)
    })
    @PostMapping("/{flightId}/book")
    public ResponseEntity<FlightBookingDTO> bookFlight(
            @Parameter(description = "ID of the flight to book") @PathVariable Long flightId,
            @Parameter(description = "Booking request details") @RequestBody BookFlightRequestDTO bookingRequest) {
        return ResponseEntity.ok(flightBookingService.bookFlight(flightId, bookingRequest));
    }

    @Operation(summary = "Get bookings by email", description = "Returns all bookings for a given email")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings")
    @GetMapping("/bookings")
    public ResponseEntity<List<FlightBookingDTO>> getBookingsByEmail(
            @Parameter(description = "Email to search bookings for") @RequestParam String email) {
        return ResponseEntity.ok(flightBookingService.findBookingsByEmail(email));
    }

    @Operation(summary = "Cancel a flight booking", description = "Cancels a flight booking for a given flight ID and email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight successfully cancelled"),
            @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid email for the booking", content = @Content)
    })
    @DeleteMapping("/{flightId}/cancel")
    public ResponseEntity<Void> cancelFlight(
            @Parameter(description = "ID of the flight to cancel") @PathVariable Long flightId,
            @Parameter(description = "Email associated with the booking") @RequestParam String email) {
        flightBookingService.cancelFlight(flightId, email);
        return ResponseEntity.noContent().build();
    }
}