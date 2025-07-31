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
@Table(name = "reviews")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String text;

  private String usrename;

  private LocalDate date;
}
