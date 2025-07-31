package dev.ivanov.dining.hall.bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnHelpHandler implements Handler {

  private final TextService textService;

  private final StateService stateService;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    stateService.setState(chatId, BotStates.MAIN_MENU);
    return new SendMessage(chatId.toString(), textService.getText("help"));
  }
  
}
