package dev.ivanov.dining.hall.bot.bots;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.ivanov.dining.hall.bot.handlers.ExternalHandler;

public final class AppBot extends TelegramLongPollingBot {

  private Logger logger = LoggerFactory.getLogger(AppBot.class);

  private final ExecutorService telegramExecutorService;

  private final String username;

  private final ExternalHandler externalHandler;

  public AppBot(
    @Value("${app.bot.username}") String username,
    @Value("${app.bot.token}") String token,
    ExecutorService telegramExecutorService,
    ExternalHandler externalHandler
  ) {
    super(new DefaultBotOptions(), token);
    this.onRegister();
    this.telegramExecutorService = telegramExecutorService;
    this.username = username;
    this.externalHandler = externalHandler;
  }

  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public void onUpdateReceived(Update update) {
      telegramExecutorService.submit(() -> {
        try {
          execute(externalHandler.handle(update));
        } catch (Exception e) {
          logger.error("error processing update", e);
        }
      });
  }
}
