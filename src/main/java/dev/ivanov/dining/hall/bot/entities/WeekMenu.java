package dev.ivanov.dining.hall.bot.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "week_menus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeekMenu {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private LocalDate date;

  @OneToMany(mappedBy = "weekMenu")
  private List<DayMenu> dayMenus;

  
}
