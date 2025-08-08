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
  
  private Handler onStartHandler;
  
  private Handler onHelpHandler;
  
  private Handler onDefaultHandler;

  private Handler onMainMenuHandler;
  
  private Handler onAdminHandler;

  private Handler onMenuUploadButtonHandler;

  private Handler onMenuUploadHandler;

  private Handler onTodayMenuButtonHandler;

  private Handler onTomorrowMenuHandler;

  private Handler onNewReviewButtonHandler;

  private Handler onReviewInputHandler;

  private Handler onDownloadReviewsButtonHandler;

  private Handler onWeekMenuHandler;
  
  private Handler onDayMenuHandler;

  public DefaultExternalHandler(
    StateService stateService, 
    @Qualifier("onHelpHandler") Handler onHelpHandler,
    @Qualifier("onStartHandler") Handler onStartHandler, 
    @Qualifier("onMainMenuHandler") Handler onMainMenuHandler,
    @Qualifier("onAdminHandler") Handler onAdminHandler,
    @Qualifier("onUploadMenuHandler") Handler onMenuUploadHandler,
    @Qualifier("onUploadMenuButtonHandler") Handler onMenuUploadButtonHandler,
    @Qualifier("onTodayMenuButtonHandler") Handler onTodayMenuButtonHandler,
    @Qualifier("onTomorrowMenuHandler") Handler onTomorrowMenuHandler,
    @Qualifier("onNewReviewButtonHandler") Handler onNewReviewButtonHandler,
    @Qualifier("onDownloadReviewsButtonHandler") Handler onDownloadReviewsButtonHandler,
    @Qualifier("onReviewInputHandler") Handler onReviewInputHandler,
    @Qualifier("onWeekMenuHandler") Handler onWeekMenuHandler,
    @Qualifier("onDayMenuHandler") Handler onDayMenuHandler,
    @Qualifier("onDefaultHandler") Handler onDefaultHandler) {
    
    this.onMainMenuHandler = onMainMenuHandler;
    this.onWeekMenuHandler = onWeekMenuHandler;
    this.onNewReviewButtonHandler = onNewReviewButtonHandler;
    this.onTodayMenuButtonHandler = onTodayMenuButtonHandler;
    this.onTomorrowMenuHandler = onTomorrowMenuHandler;
    this.stateService = stateService;
    this.onStartHandler = onStartHandler;
    this.onHelpHandler = onHelpHandler;
    this.onAdminHandler = onAdminHandler;
    this.onMenuUploadButtonHandler = onMenuUploadButtonHandler;
    this.onDownloadReviewsButtonHandler = onDownloadReviewsButtonHandler;
    this.onMenuUploadHandler = onMenuUploadHandler;
    this.onDefaultHandler = onDefaultHandler;
    this.onReviewInputHandler = onReviewInputHandler;
    this.onDayMenuHandler = onDayMenuHandler;

  }

  @Override
  public SendMessage handle(Update update) {

    if (update.hasMessage() && update.getMessage().hasText()) {
      Long chatId = update.getMessage().getChatId();
      String text = update.getMessage().getText();

      //handle inputs
      if (stateService.getState(chatId).equals(BotStates.NEW_REVIEW)) {
        return onReviewInputHandler.handle(chatId, update);
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

      // if (callbackType.startsWith("rb")) {
      //   return onReviewsHandler.handle(chatId, update);
      // }

      if (callbackType.startsWith("wd:")) {
        return onDayMenuHandler.handle(chatId, update);
      }

      //handle buttons
      switch (callbackType) {
        case "startButton":
          return onMainMenuHandler.handle(chatId, update);
        // case "reviewsButton":
        //   return onReviewsHandler.handle(chatId, update);
        case "mainMenuButton":
          return onMainMenuHandler.handle(chatId, update); 
        case "todayMenuButton":
          return onTodayMenuButtonHandler.handle(chatId, update);
        case "tomorrowMenuButton":
          return onTomorrowMenuHandler.handle(chatId, update);
        case "newReviewButton":
          return onNewReviewButtonHandler.handle(chatId, update);
        case "downloadReviewsButton":
          return onDownloadReviewsButtonHandler.handle(chatId, update);
        case "uploadMenuButton":
          return onMenuUploadButtonHandler.handle(chatId, update);
        case "weekMenuButton":
          return onWeekMenuHandler.handle(chatId, update);
      }
    } 
    // handle fiels
    else if (update.hasMessage() && update.getMessage().hasDocument()) {
      Long chatId = update.getMessage().getChatId();
      logger.info("hasDocument, state is {}", stateService.getState(chatId));
      if (stateService.getState(chatId).equals(BotStates.UPLOAD_MENU)) {
        logger.info("UPLOAD_MENU");
        return onMenuUploadHandler.handle(chatId, update);
      }
    }
    Long chatId = null;
    if (update.hasMessage()) {
      chatId = update.getMessage().getChatId();
    }
    else if (update.hasCallbackQuery()) {
      chatId = update.getCallbackQuery().getMessage().getChatId();
    }
    return onDefaultHandler.handle(chatId, update);
    
  }

}
