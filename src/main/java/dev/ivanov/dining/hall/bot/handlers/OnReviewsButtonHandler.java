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
public class OnReviewsButtonHandler implements Handler {

  private static final Logger logger = LoggerFactory.getLogger(OnReviewsButtonHandler.class);

  private final StateService stateService;

  private final TextService textService;

  private final HandlerUtils handlerUtils;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("reviews handle ({})", chatId);
    stateService.setState(chatId, BotStates.USER_REVIEWS);
    if (!update.hasCallbackQuery())
      throw new IllegalArgumentException("Update has no callback query");
    Integer page = 0;
    if (!update.getCallbackQuery().getData().equals("riviewsButton")) {
      String type = update.getCallbackQuery().getData().split(":")[0];
      if (!type.equals("rb")) 
        throw new IllegalArgumentException("Invalid callback data: " + update.getCallbackQuery().getData());
      page = Integer.parseInt(update.getCallbackQuery().getData().split(":")[1]);
    }
    
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(Pair.of("", ""))
      )
    );
    SendMessage sendMessage = new SendMessage(chatId.toString(), "reviews"); //TODO change text
    return sendMessage;
  }
  
}
