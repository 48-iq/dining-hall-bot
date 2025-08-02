package dev.ivanov.dining.hall.bot.bots;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.ivanov.dining.hall.bot.handlers.ExternalHandler;
import lombok.Getter;

@Component
public class BotContainer {

  @Getter
  private AppBot appBot;

  public BotContainer (
    @Value("${app.bot.username}") String username,
    @Value("${app.bot.token}") String token,
    ExecutorService telegramExecutorService,
    ExternalHandler externalHandler
  ) {
    this.appBot = new AppBot(username, token, telegramExecutorService, externalHandler);
  }

}
