package dev.ivanov.dining.hall.bot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ivanov.dining.hall.bot.entities.WeekMenu;

@Repository
public interface WeekMenuRepository extends JpaRepository<WeekMenu, String>{
  
}
