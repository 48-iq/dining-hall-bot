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
import dev.ivanov.dining.hall.bot.services.MenuService;
import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnTomorrowMenuHandler implements Handler{
  private static final Logger logger = LoggerFactory.getLogger(OnTomorrowMenuHandler.class);

  private final StateService stateService;
  private final TextService textService;
  private final HandlerUtils handlerUtils;
  private final MenuService menuService;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("onTodayMenuButtonHandler handle ({})", chatId);
    
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
      )
    );

    stateService.setState(chatId, BotStates.DAY_MENU);
    String menu = menuService.getTomorrowMenu();

    SendMessage sendMessage = new SendMessage(chatId.toString(), menu);
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    sendMessage.enableHtml(true);
    return sendMessage;
  }
}
