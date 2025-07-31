package dev.ivanov.dining.hall.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface ExternalHandler {
  SendMessage handle(Update update);
}
