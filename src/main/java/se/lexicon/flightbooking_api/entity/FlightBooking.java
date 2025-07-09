package se.lexicon.flightbooking_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String flightNumber;
    
    private String passengerName;
    
    private String passengerEmail;
    
    @Column(nullable = false)
    private LocalDateTime departureTime;
    
    @Column(nullable = false)
    private LocalDateTime arrivalTime;
    
    @Enumerated(EnumType.STRING)
    private FlightStatus status;
    
    private String destination;
    
    private Double price;
}