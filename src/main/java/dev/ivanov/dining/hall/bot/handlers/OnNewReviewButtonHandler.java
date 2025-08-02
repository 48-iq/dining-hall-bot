package dev.ivanov.dining.hall.bot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnNewReviewButtonHandler implements Handler {
  
  private final static Logger logger = LoggerFactory.getLogger(OnNewReviewButtonHandler.class);

  private final TextService textService;

  private final StateService stateService;
  
  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("OnNewReviewButtonHandler handle ({})", chatId);
    stateService.setState(chatId, BotStates.NEW_REVIEW);
    String text = textService.getText("newReview");
    return new SendMessage(chatId.toString(), text);
  }
  
}
