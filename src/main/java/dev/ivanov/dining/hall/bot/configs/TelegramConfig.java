package dev.ivanov.dining.hall.bot.configs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


import dev.ivanov.dining.hall.bot.bots.AppBot;

@Configuration
public class TelegramConfig {
  

  @Bean
  public TelegramBotsApi telegramBotsApi (
    AppBot clientTelegramBot
  ) throws TelegramApiException {
    TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
    botsApi.registerBot(clientTelegramBot);
    return botsApi;
  }

  

  @Bean
  public ExecutorService telegramExecutorService(
    @Value("${app.bot.threads}") int threads
  ) {
    return Executors.newFixedThreadPool(threads);
  }
}
