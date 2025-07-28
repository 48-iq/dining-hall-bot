package dev.ivanov.dining.hall.bot.entities;

import dev.ivanov.dining.hall.bot.states.BotStates;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bot_states")
public class BotState {

  @Id
  private String chatId;

  @Enumerated(EnumType.STRING)
  private BotStates state;
}
