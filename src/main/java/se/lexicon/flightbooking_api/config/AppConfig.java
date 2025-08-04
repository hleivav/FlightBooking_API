package se.lexicon.flightbooking_api.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("se.lexicon.*")
public class AppConfig {

    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder().maxMessages(10).build();
    }
}
