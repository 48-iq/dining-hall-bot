package dev.ivanov.dining.hall.bot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ivanov.dining.hall.bot.entities.MenuRow;
import java.util.List;
import java.time.LocalDate;


@Repository
public interface MenuRowRepository extends JpaRepository<MenuRow, String>{
  List<MenuRow> findByDate(LocalDate date);
}
