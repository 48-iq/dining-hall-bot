package dev.ivanov.dining.hall.bot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ivanov.dining.hall.bot.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
}
