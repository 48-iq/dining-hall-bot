package dev.ivanov.dining.hall.bot.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import dev.ivanov.dining.hall.bot.services.TextService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnMainMenuHandler implements Handler {
  private static Logger logger = LoggerFactory.getLogger(OnMainMenuHandler.class);

  private final TextService textService;

  private final HandlerUtils handlerUtils;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("main menu handle ({})", chatId);
    List<List<Pair<String,String>> > buttons = List.of(
      List.of(Pair.of(textService.getText("todayMenuButton"), "todayMenuButton")),
      List.of(Pair.of(textService.getText("tomorrowMenuButton"), "tomorrowMenuButton")),
      List.of(Pair.of(textService.getText("reviewsButton"), "reviewsButton"))
    );
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(buttons);
    SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("mainMenu"));
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }
  
}
