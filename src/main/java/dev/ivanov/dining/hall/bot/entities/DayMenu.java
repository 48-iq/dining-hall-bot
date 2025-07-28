package dev.ivanov.dining.hall.bot.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "day_menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayMenu {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String day; 

  @Column(nullable = false)
  private LocalDate date;

  @ManyToOne
  @JoinColumn(name = "week_menu_id")
  private WeekMenu weekMenu;

  @OneToMany(mappedBy = "dayMenu")
  private List<MealType> mealTypes;

}
