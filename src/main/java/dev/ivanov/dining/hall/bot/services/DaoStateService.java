package dev.ivanov.dining.hall.bot.services;

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
    return botStateRepository.findById(chatId)
      .orElseThrow(() -> new IllegalStateException("BotState not found for chatId: " + chatId))
      .getState();
  }

  @Override
  @Transactional
  public void setState(Long chatId, BotStates state) {
    BotState botState = botStateRepository.findById(chatId)
      .orElse(
        BotState.builder()
          .chatId(chatId)
          .state(BotStates.MAIN_MENU)
          .reviewPage(0)
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
    botState.setReviewPage(0);
    botStateRepository.save(botState);
    logger.info("resetState for chat: {}", chatId);
    
    
  }

  @Override
  public Integer getReviewPage(Long chatId) {
    return botStateRepository.findById(chatId)
    .orElseThrow(() -> new IllegalArgumentException("BotState not found for chatId: " + chatId))
    .getReviewPage();
  }

  @Override
  @Transactional
  public void setReviewPage(Long chatId, Integer reviewPage) {
    BotState botState = botStateRepository.findById(chatId)
    .orElseThrow(() -> new IllegalArgumentException("BotState not found for chatId: " + chatId));
    botState.setReviewPage(reviewPage);
    botStateRepository.save(botState);
  }
  
}
