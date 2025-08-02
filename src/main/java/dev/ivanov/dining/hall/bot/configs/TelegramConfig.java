package dev.ivanov.dining.hall.bot.configs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import dev.ivanov.dining.hall.bot.bots.BotContainer;

@Configuration
public class TelegramConfig {
  

  @Bean
  @Scope("singleton")
  public TelegramBotsApi telegramBotsApi (
    BotContainer botContainer
  ) throws TelegramApiException {
    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
    botsApi.registerBot(botContainer.getAppBot());
    return botsApi;
  }

  

  @Bean
  public ExecutorService telegramExecutorService(
    @Value("${app.bot.threads}") int threads
  ) {
    return Executors.newFixedThreadPool(threads);
  }

  @Bean
  public RestTemplate restTemplate(
    RestTemplateBuilder builder
  ) {
    return builder
      .build();
  }
}
