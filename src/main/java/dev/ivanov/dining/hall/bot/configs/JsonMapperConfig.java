package dev.ivanov.dining.hall.bot.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JsonMapperConfig {
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
