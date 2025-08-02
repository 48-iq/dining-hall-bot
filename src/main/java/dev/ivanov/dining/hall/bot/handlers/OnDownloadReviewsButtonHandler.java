package dev.ivanov.dining.hall.bot.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import dev.ivanov.dining.hall.bot.bots.BotContainer;
import dev.ivanov.dining.hall.bot.services.ExcelReviewsWriteService;
import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import dev.ivanov.dining.hall.bot.services.TextService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnDownloadReviewsButtonHandler implements Handler {

  private static final Logger logger = LoggerFactory.getLogger(OnDownloadReviewsButtonHandler.class);

  private final TextService textService;

  private final HandlerUtils handlerUtils;

  private final ExcelReviewsWriteService excelReviewsWriteService;

  private BotContainer botContainer;

  @Autowired
  @Lazy
  public void setBotContainer(BotContainer botContainer) {
    this.botContainer = botContainer;
  }


  @Override
  public SendMessage handle(Long chatId, Update update) {
    logger.trace("onDownloadMenuButtonHandler handle ({})", chatId);
    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      List.of(
        List.of(Pair.of(textService.getText("mainMenuButton"), "mainMenuButton"))
      )
    );
    sendReviews(chatId);
    SendMessage sendMessage = new SendMessage(chatId.toString(), textService.getText("downloadReviews"));
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }

  private void sendReviews(Long chatId) {
    try (InputStream inputStream = excelReviewsWriteService.writeReviews()) {
      SendDocument sendDocument = new SendDocument();
      InputFile inputFile = new InputFile();
      logger.trace("avaible bytes {}", inputStream.available());
      inputFile.setMedia(inputStream, "reviews.xlsx");
      sendDocument.setDocument(inputFile);
      sendDocument.setChatId(chatId);
      botContainer.getAppBot().execute(sendDocument);
    } catch (IOException|TelegramApiException e) {
      logger.error("error processing menu update", e);
      throw new RuntimeException(e);
    }
  }
  
}
