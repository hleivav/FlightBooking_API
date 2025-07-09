package se.lexicon.flightbooking_api.exception;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record ErrorResponse(
        int status,
        String[] errors,
        LocalDateTime dateTime
) {
    public ErrorResponse(int status, String[] errors) {
        this(status, errors, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
