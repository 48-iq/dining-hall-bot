package dev.ivanov.dining.hall.bot.handlers;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.dining.hall.bot.bots.BotContainer;
import dev.ivanov.dining.hall.bot.entities.MenuRow;
import dev.ivanov.dining.hall.bot.services.MenuService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.services.ExcelMenuReadService;
import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnUploadMenuHandler implements Handler { 

  private final static Logger logger = LoggerFactory.getLogger(OnUploadMenuHandler.class);

  
  private final ExcelMenuReadService xcelMenuReadService;

  private final HandlerUtils handlerUtils;
  
  private final MenuService menuService;
  
  private final TextService textService;

  private BotContainer botContainer;

  @Autowired
  @Lazy
  public void setBotContainer(BotContainer botContainer) {
    this.botContainer = botContainer;
  }
  
  @Value("${app.bot.token}")
  private String token;

  @Override
  
  public SendMessage handle(Long chatId, Update update) {
    try {
      GetFile getFile = new GetFile();
      getFile.setFileId(update.getMessage().getDocument().getFileId());
      String filepath = botContainer.getAppBot().execute(getFile).getFilePath();
      String fileDownloadUrl = "https://api.telegram.org/file/bot" + token + "/" + filepath;
      try (InputStream inputStream = URI.create(fileDownloadUrl).toURL().openStream()) {
        logger.trace("avaible bytes {}", inputStream.available());
        List<MenuRow> menuRows = xcelMenuReadService.readMenu(inputStream);
        menuService.updateMenu(menuRows);
        InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
          List.of(
            List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
          )
        );
        SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("uploadMenuSuccess"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
      }
      } catch (TelegramApiException|IOException e) {
        logger.error("error processing menu update", e);
        InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
          List.of(
            List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
          )
        );
        SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("uploadMenuSuccess"));
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
    
  }
  
}
