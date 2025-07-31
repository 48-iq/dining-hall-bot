package dev.ivanov.dining.hall.bot.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;


@Service
public class HandlerUtils {
  private static final Logger logger = LoggerFactory.getLogger(HandlerUtils.class);

  public InlineKeyboardMarkup getButtons(List<List<Pair<String, String>>> buttons) {
    logger.trace("get buttons");
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    for (List<Pair<String, String>> row : buttons) {
      List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
      for (Pair<String, String> pair : row) {
        logger.info(pair.toString());
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton();
        keyboardButton.setText(pair.getFirst());
        keyboardButton.setCallbackData(pair.getSecond());
        keyboardRow.add(keyboardButton);
      }
      keyboard.add(keyboardRow);
    }
    inlineKeyboardMarkup.setKeyboard(keyboard);
    return inlineKeyboardMarkup;
  }
}
