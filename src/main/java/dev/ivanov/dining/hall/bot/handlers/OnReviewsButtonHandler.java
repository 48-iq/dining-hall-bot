package dev.ivanov.dining.hall.bot.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import dev.ivanov.dining.hall.bot.dto.ReviewsPageDto;
import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import dev.ivanov.dining.hall.bot.services.ReviewService;
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

  private final ReviewService reviewService;

  @Override
  public SendMessage handle(Long chatId, Update update) {

    logger.trace("reviews handle ({})", chatId);

    stateService.setState(chatId, BotStates.REVIEWS);

    if (!update.hasCallbackQuery())
      throw new IllegalArgumentException("Update has no callback query");

    Integer page = 0;

    if (!update.getCallbackQuery().getData().equals("reviewsButton")) {
      String type = update.getCallbackQuery().getData().split(":")[0];
      if (!type.equals("rb")) 
        throw new IllegalArgumentException("Invalid callback data: " + update.getCallbackQuery().getData());
      page = Integer.parseInt(update.getCallbackQuery().getData().split(":")[1]);
    }

    String firstButtonText = "---";
    String firstButtonPayload = "rb:" + page;
    
    String secondButtonText= "---";
    String secondButtonPayload = "rb" + page;

    ReviewsPageDto reviewsPageDto = reviewService.getReviews(page);

    if (reviewsPageDto.getTotalPages() - 1 > page) {
      secondButtonText = ">";
      secondButtonPayload = "rb:" + (page + 1);
    }

    if (page > 0) {
      firstButtonText = "<";
      firstButtonPayload = "rb:" + (page - 1);
    }
    
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(
          Pair.of(firstButtonText, firstButtonPayload), 
          Pair.of(secondButtonText, secondButtonPayload)
        ),
        List.of(Pair.of(textService.getText("newReviewButton"), "newReviewButton")),
        List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
      )
    );
    
    SendMessage sendMessage = new SendMessage(chatId.toString(), reviewsPageDto.getText());
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    sendMessage.enableHtml(true);
    return sendMessage;
  }
  
}
