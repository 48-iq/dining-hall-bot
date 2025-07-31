package dev.ivanov.dining.hall.bot.services;

import java.io.InputStream;
import java.util.List;

import dev.ivanov.dining.hall.bot.entities.MenuRow;

public interface XcelMenuReadService {
  public List<MenuRow> readMenu(InputStream file);
}
