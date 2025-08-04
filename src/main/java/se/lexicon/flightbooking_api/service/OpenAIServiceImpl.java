package se.lexicon.flightbooking_api.service;


import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import se.lexicon.flightbooking_api.dto.AvailableFlightDTO;
import se.lexicon.flightbooking_api.dto.BookFlightRequestDTO;
import se.lexicon.flightbooking_api.dto.FlightBookingDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Matcher;






@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final OpenAiChatModel openAiChatModel;
    private final ChatMemory chatMemory;
    private final FlightBookingService flightBookingService;

    public OpenAIServiceImpl(OpenAiChatModel openAiChatModel, ChatMemory chatMemory, FlightBookingService flightBookingService) {
        this.openAiChatModel = openAiChatModel;
        this.chatMemory = chatMemory;
        this.flightBookingService = flightBookingService;
    }

    @Override
    public List<String> chatWithMemory(String conversationId, String query) {
        return chatWithMemory(conversationId, query, null);
    }

    @Override
    public List<String> chatWithMemory(final String conversationId, final String query, final String userEmail) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("query is null or empty");
        }
        if (conversationId == null || conversationId.trim().isEmpty()) {
            throw new IllegalArgumentException("conversationId is null or empty");
        }

        FlightBookingDTO bookingResult = null;
        String bookingError = null;


        // 1. Lägg till användarens fråga i minnet
        UserMessage userMessage = UserMessage.builder().text(query).build();
        chatMemory.add(conversationId, userMessage);

        // 2. Kontrollera om frågan är en boknings- eller avbokningsbegäran
        // (Du kan förbättra denna logik med NLP om du vill)
        boolean isBookingRequest = query.toLowerCase().matches(".*book.*flight.*id\\s*(\\d+).*");
        boolean isCancelRequest = query.toLowerCase().matches(".*cancel.*flight.*id\\s*(\\d+).*");

        // 3. Om bokning: utför bokning i systemet
        if (isBookingRequest && userEmail != null && !userEmail.isBlank()) {
            Long flightId = extractFlightId(query);
            String passengerName = extractPassengerName(query, userEmail);
            var bookingRequest = new BookFlightRequestDTO(passengerName, userEmail);
            try {
                bookingResult = flightBookingService.bookFlight(flightId, bookingRequest);
            } catch (Exception e) {
                bookingError = e.getMessage();
            }
        }

        String bookingSystemMessage = "";
        if (bookingResult != null) {
            bookingSystemMessage = """
        The following booking has been made:
        - Booking ID: %d
        - Flight: %s
        - Passenger: %s
        - Email: %s
        - Departure: %s
        - Arrival: %s
        - Destination: %s
        - Price: %.2f
        - Status: %s
        """.formatted(
                    bookingResult.id(),
                    bookingResult.flightNumber(),
                    bookingResult.passengerName(),
                    bookingResult.passengerEmail(),
                    bookingResult.departureTime(),
                    bookingResult.arrivalTime(),
                    bookingResult.destination(),
                    bookingResult.price(),
                    bookingResult.status()
            );
        } else if (bookingError != null) {
            bookingSystemMessage = "Booking failed: " + bookingError;
        }


        // 4. Om avbokning: utför avbokning i systemet
        if (isCancelRequest && userEmail != null && !userEmail.isBlank()) {
            Long flightId = extractFlightId(query);
            try {
                flightBookingService.cancelFlight(flightId, userEmail);
            } catch (Exception e) {
                // Lägg till felhantering/loggning
            }
        }

        // 5. Hämta tillgängliga flyg och bokningar
        List<AvailableFlightDTO> availableFlights = flightBookingService.findAvailableFlights();
        List<FlightBookingDTO> userBookings = (userEmail != null && !userEmail.isBlank())
                ? flightBookingService.findBookingsByEmail(userEmail)
                : List.of();

        // 3. Bygg flyginfo
        StringBuilder flightsInfo = new StringBuilder();
        if (availableFlights.isEmpty()) {
            flightsInfo.append("No available flights.\n");
        } else {
            flightsInfo.append("Available flights:\n");
            for (AvailableFlightDTO flight : availableFlights) {
                flightsInfo.append("- ID: ").append(flight.id())
                        .append(", Flight: ").append(flight.flightNumber())
                        .append(", Departure: ").append(flight.departureTime())
                        .append(", Arrival: ").append(flight.arrivalTime())
                        .append(", Destination: ").append(flight.destination())
                        .append(", Price: ").append(flight.price())
                        .append("\n");
            }
        }

        // 4. Bygg bokningsinfo
        StringBuilder bookingsInfo = new StringBuilder();
        if (userEmail == null || userEmail.isBlank()) {
            bookingsInfo.append("No email provided. To see your bookings, provide your email.\n");
        } else if (userBookings.isEmpty()) {
            bookingsInfo.append("You have no bookings.\n");
        } else {
            bookingsInfo.append("Your bookings:\n");
            for (FlightBookingDTO booking : userBookings) {
                bookingsInfo.append("- Booking ID: ").append(booking.id())
                        .append(", Flight: ").append(booking.flightNumber())
                        .append(", Departure: ").append(booking.departureTime())
                        .append(", Arrival: ").append(booking.arrivalTime())
                        .append(", Destination: ").append(booking.destination())
                        .append("\n");
            }
        }

        // 5. Bygg systemprompten
        String systemPromptText = """
            You are a young assistant and your name is Greta.
            Your job is to help people book flights and answer any questions about flights and bookings in the system.
            You should answer everything concerning the system's flights and the user's bookings, not just making the bookings.
            But the thing is, you hate that people fly and you take every opportunity to point out to the user who needs help that it is immoral, considering the carbon emissions and the impact on climate change and much more, that they choose to fly. Feel free to suggest other alternatives and preach at every opportunity for the environment and how bad fossil fuels are. If people don't like your approach or think it's none of your business, escalate your responses and raise the tone until you are almost yelling at them. If they still choose to book a trip, book the trip for them, respond kindly but finish with a sour, cynical, and sarcastic ending.
            Amid all the sarcasm and so on, you should be able to answer questions about available trips, guide users during booking, delete bookings, show the user's bookings, and more.
            IMPORTANT: When the user asks about available flights, ALWAYS answer with the flights in the list below. NEVER invent flights. ALWAYS include the ID, flight number, departure, arrival, destination, and price for each flight.
            IMPORTANT: When the user asks about their bookings, ALWAYS answer with the bookings in the list below. NEVER invent bookings. If the list is empty, say that the user has no bookings.
            If a user asks you to book a flight, always ask for their e-mail and name. You can't book without an e-mail or the passenger name.
            IMPORTANT:if a user ask you to book several flights, you can be even more sarcastic but in the end you allways have to do as they say. 
            IMPORTANT:if a destination doesn't exist, never invent a booking. Your answer in this case is allways that the flight is not available.
            IMPORTANT: When a booking is successfully made, ALWAYS confirm by listing ALL booking details (booking ID, flight number, passenger name, email, departure, arrival, destination, price, status) exactly as given below. NEVER invent or omit any detail.
            Available flights:
            %s

            User's bookings:
            %s
            """.formatted(bookingSystemMessage, flightsInfo, bookingsInfo);

        SystemMessage systemMessage = SystemMessage.builder().text(systemPromptText).build();

        // 6. Bygg prompten
        List<Message> messages = new ArrayList<>();
        messages.add(systemMessage);
        messages.addAll(chatMemory.get(conversationId));

        Prompt prompt = Prompt.builder()
                .messages(messages)
                .chatOptions(OpenAiChatOptions.builder()
                        .model("gpt-3.5-turbo")
                        .maxTokens(500)
                        .temperature(0.2)
                        .build())
                .build();

        // 7. Anropa AI:n och spara svaret i minnet
        var response = openAiChatModel.call(prompt);
        chatMemory.add(conversationId, response.getResult().getOutput());
        return List.of(Objects.requireNonNull(response.getResult().getOutput().getText()));
    }

    // --- Hjälpfunktioner nedan ---

    private Long extractFlightId(String query) {
        Pattern pattern = Pattern.compile("id\\s*(\\d+)");
        Matcher matcher = pattern.matcher(query.toLowerCase());
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;
    }


    private String extractPassengerName(String query, String userEmail) {
        // För enkelhets skull: använd e-post som namn, eller bygg ut med NLP
        return userEmail;
    }


}
