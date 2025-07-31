package dev.ivanov.dining.hall.bot.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.states.BotStates;

@Component
public class DefaultExternalHandler implements ExternalHandler{
  private static final Logger logger = LoggerFactory.getLogger(DefaultExternalHandler.class);
  
  private final StateService stateService;
  
  private static final String START_COMMAND = "/start";
  
  private static final String HELP_COMMAND = "/help";
  
  private static final String MAIN_MENU_COMMAND = "/menu";

  private static final String ADMIN_COMMAND = "/admin";
  
  private final Handler onStartHandler;
  
  private final Handler onHelpHandler;
  
  private final Handler onDefaultHandler;

  private final Handler onMainMenuHandler;
  
  private final Handler onAdminHandler;

  private final Handler onMenuUploadButtonHandler;

  private final Handler onMenuUploadHandler;

  public DefaultExternalHandler(
    StateService stateService, 
    @Qualifier("onHelpHandler") Handler onHelpHandler,
    @Qualifier("onStartHandler") Handler onStartHandler, 
    @Qualifier("onMainMenuHandler") Handler onMainMenuHandler,
    @Qualifier("onAdminHandler") Handler onAdminHandler,
    @Qualifier("onUploadMenuHandler") Handler onMenuUploadHandler,
    @Qualifier("onUploadMenuButtonHandler") Handler onMenuUploadButtonHandler,
    @Qualifier("onDefaultHandler") Handler onDefaultHandler) {
    
    this.onMainMenuHandler = onMainMenuHandler;
    this.stateService = stateService;
    this.onStartHandler = onStartHandler;
    this.onHelpHandler = onHelpHandler;
    this.onAdminHandler = onAdminHandler;
    this.onMenuUploadButtonHandler = onMenuUploadButtonHandler;
    this.onMenuUploadHandler = onMenuUploadHandler;
    this.onDefaultHandler = onDefaultHandler;

  }

  @Override
  public SendMessage handle(Update update) {

    if (update.hasMessage() && update.getMessage().hasText()) {
      Long chatId = update.getMessage().getChatId();
      String text = update.getMessage().getText();

      //handle inputs
      if (stateService.getState(chatId).equals(BotStates.USER_CREATE_REVIEW)) {
        
      }

      
      
      //handle commands
      switch (text) {
        case START_COMMAND:
          logger.info("START_COMMAND");
          return onStartHandler.handle(chatId, update);
        case HELP_COMMAND:
          return onHelpHandler.handle(chatId, update);
        case ADMIN_COMMAND:
          return onAdminHandler.handle(chatId, update);
        case MAIN_MENU_COMMAND:
          return onMainMenuHandler.handle(chatId, update);
      }
    }
    else if (update.hasCallbackQuery()) {
      String callbackType = update.getCallbackQuery().getData();
      Long chatId = update.getCallbackQuery().getMessage().getChatId();

      //handle buttons
      switch (callbackType) {
        case "startButton":
          return onMainMenuHandler.handle(chatId, update);
        case "reviewsButton":
        case "todayMenuButton":
        case "tomorrowMenuButton":
        case "weekMenuButton":
        case "newReviewButton":
        case "sendNewReviewButton":
        case "downloadReviewsButton":
        case "uploadMenuButton":
          return onMenuUploadButtonHandler.handle(chatId, update);
      }
    } 
    else if (update.hasMessage() && update.getMessage().hasDocument()) {
      Long chatId = update.getMessage().getChatId();
      logger.info("hasDocument, state is {}", stateService.getState(chatId));
      if (stateService.getState(chatId).equals(BotStates.UPLOAD_MENU)) {
        logger.info("UPLOAD_MENU");
        return onMenuUploadHandler.handle(chatId, update);
      }
    }
    return onDefaultHandler.handle(update.getMessage().getChatId(), update);
    
  }

}
