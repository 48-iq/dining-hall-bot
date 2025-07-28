package dev.ivanov.dining.hall.bot.bots;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.dining.hall.bot.handlers.Handler;

@Component
public class AppBot extends TelegramLongPollingBot {

  private Logger logger = LoggerFactory.getLogger(AppBot.class);

  private final ExecutorService telegramExecutorService;

  private final String username;

  private final Handler textHandler;

  public AppBot(
    @Value("${app.bot.username}") String username,
    @Value("${app.bot.token}") String token,
    @Autowired ExecutorService telegramExecutorService,
    @Qualifier("textHandler") 
    @Autowired Handler textHandler
  ) {
    super(token);
    this.telegramExecutorService = telegramExecutorService;
    this.username = username;
    this.textHandler = textHandler;
  }

  @Override
  public String getBotUsername() {
    return username;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      telegramExecutorService.submit(() -> {
        try {
          execute(textHandler.handle(update));
        } catch (TelegramApiException e) {
          logger.error("Error sending message", e);
        }
      });
    }
    
    else if (update.hasCallbackQuery()) {

    }

    else {
      logger.info("Received unknown update: {}", update);

    }
  }
}
