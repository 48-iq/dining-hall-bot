package dev.ivanov.dining.hall.bot.handlers;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import dev.ivanov.dining.hall.bot.services.HandlerUtils;
import dev.ivanov.dining.hall.bot.services.StateService;
import dev.ivanov.dining.hall.bot.services.TextService;
import dev.ivanov.dining.hall.bot.states.BotStates;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OnWeekMenuHandler implements Handler {

  private final StateService stateService;

  private final HandlerUtils handlerUtils;

  private final TextService textService;

  @Override
  public SendMessage handle(Long chatId, Update update) {
    stateService.setState(chatId, BotStates.WEEK_MENU);
    LocalDate day = LocalDate.now();
    List<List<Pair<String, String>>> buttons = new ArrayList<>();

    // move back
    while (day.getDayOfWeek() != DayOfWeek.MONDAY) {
      day = day.minusDays(1);
    }


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    for (int i = 0; i < 5; i++) {
      buttons.add(
        List.of(
          Pair.of(getDayRussianName(day.getDayOfWeek()), "wd:" + formatter.format(day))
        )
      );
      day = day.plusDays(1);
    }

    buttons.add(
      List.of(
        Pair.of(textService.getText("mainMenuButton"), "mainMenuButton")
      )
    );

    InlineKeyboardMarkup inlineKeyboardMarkup = handlerUtils.getButtons(
      buttons
    );

    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId.toString());
    sendMessage.setText(textService.getText("weekMenu"));
    sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    return sendMessage;
  }

  private String getDayRussianName(DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> "Понедельник";
      case TUESDAY -> "Вторник";
      case WEDNESDAY -> "Среда";
      case THURSDAY -> "Четверг";
      case FRIDAY -> "Пятница";
      case SATURDAY -> "Суббота";
      case SUNDAY -> "Воскресенье";
    };
  }
  
}
