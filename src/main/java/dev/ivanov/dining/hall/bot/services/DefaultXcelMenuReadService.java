package dev.ivanov.dining.hall.bot.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import dev.ivanov.dining.hall.bot.entities.MenuRow;

@Service
public class DefaultXcelMenuReadService implements XcelMenuReadService {
  private static final Logger logger = LoggerFactory.getLogger(DefaultXcelMenuReadService.class);

  @Override
  public List<MenuRow> readMenu(InputStream file) {
    List<MenuRow> menuRows = new ArrayList<>();
    try (ReadableWorkbook workbook = new ReadableWorkbook(file)) {
    
      
      Sheet sheet = workbook.getFirstSheet();
      List<Row> rows = sheet.read();

      LocalDate date = null;
      String type = null;
      String name = null;
      String price = null;
      Integer order = 1;
      for (int i = 0; i < rows.size(); i++) {
        Row row = rows.get(i);
        if (row == null || i == 0) continue;
        date = row.getCellAsDate(0).map(d -> d.toLocalDate()).orElse(date);
        type = row.getCellAsString(1).map(t -> t.isEmpty() ? null : t).orElse(type);
        name = row.getCellAsString(2).map(t -> t.isEmpty() ? null : t).orElse(name);
        price = row.getCellAsString(3).map(t -> t.isEmpty() ? null : t).orElse(price);
        logger.trace("date {}, type {}, name {}, price {}", date, type, name, price);
        menuRows.add(
          MenuRow.builder()
            .date(date)
            .type(type)
            .name(name)
            .price(price)
            .number(order++)
            .build()
          );
      }
      
    } catch (Exception e) {
      logger.error("error reading file", e);
      throw new RuntimeException(e);
    }
    return menuRows;
  }
  
}
