package dev.ivanov.dining.hall.bot.services;

import dev.ivanov.dining.hall.bot.states.BotStates;

public interface StateService {
  BotStates getState(Long chatId);

  void setState(Long chatId, BotStates state);

  void resetState(Long chatId);
}
