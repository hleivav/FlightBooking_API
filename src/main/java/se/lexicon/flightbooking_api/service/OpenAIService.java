package se.lexicon.flightbooking_api.service;

import reactor.core.publisher.Flux;

import java.util.List;

public interface OpenAIService {

    List<String> chatWithMemory(String conversationId, String query, String userEmail);
    List<String> chatWithMemory(String conversationId, String query);
}
