package dev.ivanov.dining.hall.bot.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.ivanov.dining.hall.bot.entities.MenuRow;
import dev.ivanov.dining.hall.bot.repositories.MenuRowRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DaoMenuService implements MenuService {

  private static final Logger logger = LoggerFactory.getLogger(DaoMenuService.class);

  private final MenuRowRepository menuRowRepository;

  private final TextService textService;

  @Override
  @Transactional
  public void updateMenu(List<MenuRow> menu) {
    logger.trace("updateMenu");
    menuRowRepository.deleteAll();
    menuRowRepository.saveAll(menu);
  }

  @Override
  @Transactional
  public String getMenuByDate(LocalDate date) {
    logger.trace("getMenuByDate");
    List<MenuRow> menuRows = menuRowRepository.findByDate(date);
    return convert(menuRows, getDayRussianName(date.getDayOfWeek()));
  }

  @Override
  public String getTodayMenu() {
    logger.trace("getTodayMenu");
    LocalDate today = LocalDate.now();
    List<MenuRow> menuRows = menuRowRepository.findByDate(today);
    return convert(menuRows, getDayRussianName(today.getDayOfWeek()));
  }

  private String convert(List<MenuRow> menuRows, String dayName) {
    menuRows.sort((m1, m2) -> m1.getNumber().compareTo(m2.getNumber()));
    StringBuilder menuBuilder = new StringBuilder();
    menuBuilder
      .append("<b>" + dayName + "</b>")
      .append("\n\n");
    for (int i = 0; i < menuRows.size(); i++) {
      MenuRow menuRow = menuRows.get(i);
      if (i == 0 || !menuRow.getType().equals(menuRows.get(i - 1).getType())) {
        menuBuilder
        .append("\n")
        .append(
          "<b>" + menuRow.getType() + "</b>"
        )
        .append("\n");
      }
      menuBuilder
      .append(
        menuRow.getName() + " - " + menuRow.getPrice()
      ).append("\n");
    }
    if (menuRows.isEmpty()) 
      menuBuilder.append("\n<b>" + textService.getText("emptyMenu") + "</b>\n");
    return menuBuilder.toString();
  } 

  @Override
  public String getTomorrowMenu() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    List<MenuRow> menuRows = menuRowRepository.findByDate(tomorrow);
    return convert(menuRows, getDayRussianName(tomorrow.getDayOfWeek()));
  }
  

  private String getDayRussianName(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY:
        return "Понедельник";
      case TUESDAY:
        return "Вторник";
      case WEDNESDAY:
        return "Среда";
      case THURSDAY:
        return "Четверг";
      case FRIDAY:
        return "Пятница";
      case SATURDAY:
        return "Суббота";
      case SUNDAY:
        return "Воскресенье";
      default:
        return "";
    }
  }
}
