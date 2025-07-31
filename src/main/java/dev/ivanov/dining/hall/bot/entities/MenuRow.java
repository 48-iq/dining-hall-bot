package dev.ivanov.dining.hall.bot.entities;



import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_rows")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MenuRow {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private LocalDate date;
  private String type;
  private String name;
  private String price;
  private Integer number;
}
