package dev.ivanov.dining.hall.bot.entities;

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
@Table(name = "meal_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealType {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "mealType")
  private List<Position> positions;

  @ManyToOne
  @JoinColumn(name = "day_menu_id")
  private DayMenu dayMenu;

}
