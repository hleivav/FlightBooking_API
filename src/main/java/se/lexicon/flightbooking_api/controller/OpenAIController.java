package se.lexicon.flightbooking_api.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.lexicon.flightbooking_api.service.OpenAIService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    /**
     * Chat endpoint.
     *
     * @param conversationId Unikt id för chatten/sessionen
     * @param question       Användarens fråga/meddelande
     * @param userEmail      (Valfri) e-postadress för att visa bokningar
     * @return AI-svar som lista av strängar
     */
    @GetMapping("/messages")
    public List<String> chat(
            @NotNull
            @NotBlank
            @Size(min = 1, max = 2000, message = "Question can not exceed 2000 characters")
            @RequestParam String conversationId,
            @RequestParam String question,
            @RequestParam(required = false) String userEmail
    ) {
        // Om e-post är angiven, skicka med den
        if (userEmail != null && !userEmail.isBlank()) {
            return openAIService.chatWithMemory(conversationId, question, userEmail);
        }
        // Annars, anropa utan e-post
        return openAIService.chatWithMemory(conversationId, question);
    }
}
