package dev.ivanov.dining.hall.bot.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.ivanov.dining.hall.bot.entities.BotState;
import dev.ivanov.dining.hall.bot.repositories.BotStateRepository;
import dev.ivanov.dining.hall.bot.states.BotStates;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DaoStateService implements StateService {

  private static Logger logger = LoggerFactory.getLogger(DaoStateService.class);

  private final BotStateRepository botStateRepository;

  @Override
  public BotStates getState(Long chatId) {
    Optional<BotState> botState = botStateRepository.findById(chatId);
    if (botState.isPresent()) {
      return botState.get().getState();
    }
    return BotStates.START;
  }

  @Override
  @Transactional
  public void setState(Long chatId, BotStates state) {
    BotState botState = botStateRepository.findById(chatId)
      .orElse(
        BotState.builder()
          .chatId(chatId)
          .state(BotStates.MAIN_MENU)
        .build()
      );
    logger.info("setState for chat: {} - state: {}", chatId, state);
    botState.setState(state);
    botStateRepository.save(botState);
  }

  @Override
  @Transactional
  public void resetState(Long chatId) {
    BotState botState = botStateRepository.findById(chatId)
      .orElseThrow(() -> new IllegalArgumentException("BotState not found for chatId: " + chatId));
    botState.setState(BotStates.MAIN_MENU);
    botStateRepository.save(botState);
    logger.info("resetState for chat: {}", chatId);
  }
}
