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
import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnStartHandler implements Handler {

  private static final Logger logger = LoggerFactory.getLogger(OnStartHandler.class);

  private final TextService textService;

  private final StateService stateService;

  private final HandlerUtils handlerUtils;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("start handle ({})", chatId);
    stateService.setState(chatId, BotStates.MAIN_MENU);
    List<List<Pair<String, String>>> buttons = List.of(
      List.of(Pair.of(textService.getText("startButton"), "startButton"))
    );
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(buttons);
    SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("start"));
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    logger.info(sendMessage.toString());
    return sendMessage;
  }
  
}
