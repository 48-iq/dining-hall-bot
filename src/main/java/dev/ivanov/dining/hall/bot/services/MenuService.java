package dev.ivanov.dining.hall.bot.services;

import java.time.LocalDate;
import java.util.List;

import dev.ivanov.dining.hall.bot.entities.MenuRow;

public interface MenuService {
  void updateMenu(List<MenuRow> menu);
  String getTodayMenu();
  String getTomorrowMenu();
  String getMenuByDate(LocalDate date);
}
