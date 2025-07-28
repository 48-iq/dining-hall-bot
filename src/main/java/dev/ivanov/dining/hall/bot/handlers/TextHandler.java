package dev.ivanov.dining.hall.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.ivanov.dining.hall.bot.services.TextService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TextHandler implements Handler {

  private final TextService textService;

  @Override
  public SendMessage handle(Update update) {
    if (!update.hasMessage() || !update.getMessage().hasText()) {
      throw new IllegalArgumentException("Update has no text message");
    }
    String text = update.getMessage().getText().trim();
    switch (text) {
      case "/start":
        return onStart(update);
      case "/help":
        return onHelp(update);
      default:
        return onDefault(update);
    }
  }

  private void checkText(Update update) {
    if (!update.hasMessage() || !update.getMessage().hasText()) {
      throw new IllegalArgumentException("Update has no text message");
    }
  }

  private SendMessage onDefault(Update update) {
    checkText(update);
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText(textService.getText("default"));
    return sendMessage;
  }

  private SendMessage onStart(Update update) {
    checkText(update);
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText(textService.getText("start"));
    return sendMessage;
  }
  
  private SendMessage onHelp(Update update) {
    checkText(update);
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(update.getMessage().getChatId());
    sendMessage.setText(textService.getText("help"));
    return sendMessage;
  }
}
