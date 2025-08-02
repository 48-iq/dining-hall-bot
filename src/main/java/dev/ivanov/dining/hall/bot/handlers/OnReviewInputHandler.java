package dev.ivanov.dining.hall.bot.handlers;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import dev.ivanov.dining.hall.bot.services.ReviewService;
import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnReviewInputHandler implements Handler {
  
  private static final Logger logger = LoggerFactory.getLogger(OnReviewInputHandler.class);

  private final TextService textService;

  private final HandlerUtils handlerUtils;

  private final StateService stateService;

  private final ReviewService reviewService;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("OnReviewInputHandler handle ({})", chatId);
    if (!stateService.getState(chatId).equals(BotStates.NEW_REVIEW)) 
      throw new IllegalArgumentException("Unexpected state: " + stateService.getState(chatId));
    if (!update.hasMessage() || !update.getMessage().hasText()) 
      throw new IllegalArgumentException("Update has no message");
    String text = update.getMessage().getText();
    String username = update.getMessage().getFrom().getUserName();
    reviewService.addReview(text, username);

    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
      )
    );

    SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("newReviewSent"));
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }
  
}
