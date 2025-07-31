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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.dining.hall.bot.bots.AppBot;
import dev.ivanov.dining.hall.bot.entities.MenuRow;
import dev.ivanov.dining.hall.bot.services.MenuService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.services.XcelMenuReadService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnUploadMenuHandler implements Handler { 

  private final static Logger logger = LoggerFactory.getLogger(OnUploadMenuHandler.class);

  private AppBot appBot;

  private final XcelMenuReadService xcelMenuReadService;

  private final MenuService menuService;

  private final TextService textService;

  @Value("${app.bot.token}")
  private String token;

  @Autowired
  @Lazy
  public void setAppBot(AppBot appBot) {
    this.appBot = appBot;
  }

  @Override
  
  public SendMessage handle(Long chatId, Update update) {
    try {
      GetFile getFile = new GetFile();
      getFile.setFileId(update.getMessage().getDocument().getFileId());
      String filepath = appBot.execute(getFile).getFilePath();
      String fileDownloadUrl = "https://api.telegram.org/file/bot" + token + "/" + filepath;
      try (InputStream inputStream = URI.create(fileDownloadUrl).toURL().openStream()) {
        logger.trace("avaible bytes {}", inputStream.available());
        List<MenuRow> menuRows = xcelMenuReadService.readMenu(inputStream);
        menuService.updateMenu(menuRows);
        return new SendMessage(chatId.toString(), textService.getText("uploadMenuSuccess"));
      }
      } catch (TelegramApiException|IOException e) {
        logger.error("error processing menu update", e);
        return new SendMessage(chatId.toString(), textService.getText("uploadMenuError"));
    }
    
  }
  
}
