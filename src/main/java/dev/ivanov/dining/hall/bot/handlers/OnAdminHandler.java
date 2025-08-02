package dev.ivanov.dining.hall.bot.handlers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.dining.hall.bot.bots.BotContainer;
import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnAdminHandler implements Handler {
  private static Logger logger = LoggerFactory.getLogger(OnAdminHandler.class);
  private final StateService stateService;
  private final TextService textService;
  private final HandlerUtils handlerUtils;

  private BotContainer botContainer;

  @Autowired
  @Lazy
  public void setBotContainer(BotContainer botContainer) {
    this.botContainer = botContainer;
  }

  @Value("${app.bot.adminChatId}")
  private String adminChatId;


  @Override
  public SendMessage handle(Long chatId, Update update) {
    if (!isUserInAdminChat(update.getMessage().getFrom().getId())) 
      return new SendMessage(chatId.toString(), textService.getText("adminNotAllowed"));
    logger.trace("admin handle ({})", chatId);
    stateService.setState(chatId, BotStates.ADMIN_PANEL);
    List<List<Pair<String, String>>> buttons = List.of(
      List.of(Pair.of(textService.getText("downloadReviewsButton"), "downloadReviewsButton")),
      List.of(Pair.of(textService.getText("uploadMenuButton"), "uploadMenuButton"))
    );
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(buttons);
    SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("adminMenu"));
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }

  private boolean isUserInAdminChat(Long userId) {
    GetChatMember getChatMember = new GetChatMember();
    logger.trace(adminChatId);
    logger.trace(userId.toString());
    getChatMember.setChatId(adminChatId);
    getChatMember.setUserId(userId);
    try {
      ChatMember chatMember = botContainer.getAppBot().execute(getChatMember);
      String status = chatMember.getStatus();
      logger.trace(status);
      return !(status.equals("left") || status.equals("kicked"));
    } catch (TelegramApiException e) {
      logger.error("error processing admin check", e);
      return false;
    }
  }
  
}
