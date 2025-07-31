package dev.ivanov.dining.hall.bot.entities;


import dev.ivanov.dining.hall.bot.states.BotStates;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bot_states")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BotState {

  @Id
  private Long chatId;

  @Enumerated(EnumType.STRING)
  private BotStates state;

  private Integer reviewPage;

}
