package dev.ivanov.dining.hall.bot.handlers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class OnDayMenuHandler implements Handler {

  private static final Logger logger = LoggerFactory.getLogger(OnDayMenuHandler.class);

  private final MenuService menuService;

  private final TextService textService;

  private final HandlerUtils handlerUtils;

  private final StateService stateService;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("OnDayMenuHandler handle ({})", chatId);

    
    if (!update.hasCallbackQuery()) 
    throw new IllegalArgumentException("Update has no callback query");
    
    String data = update.getCallbackQuery().getData();
    if (!data.startsWith("wd:"))
    throw new IllegalArgumentException("Wrong data format");
    
    stateService.setState(chatId, BotStates.DAY_MENU);


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String dateString = data.split(":")[1];
    LocalDate date = LocalDate.parse(dateString, formatter);

    String menu = menuService.getMenuByDate(date);

    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId.toString());
    sendMessage.setText(menu);  
    sendMessage.enableHtml(true);

    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
      )
    );
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);

    return sendMessage;
  }
  
}
