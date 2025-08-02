package dev.ivanov.dining.hall.bot.handlers;

import java.util.List;

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
public class OnHelpHandler implements Handler {

  private final TextService textService;

  private final StateService stateService;

  private final HandlerUtils handlerUtils;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    stateService.setState(chatId, BotStates.MAIN_MENU);
    SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("help"));
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
      )
    );

    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }
  
}
