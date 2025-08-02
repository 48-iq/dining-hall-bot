package dev.ivanov.dining.hall.bot.services;

import java.util.List;

import dev.ivanov.dining.hall.bot.dto.ReviewsPageDto;
import dev.ivanov.dining.hall.bot.entities.Review;

public interface ReviewService {
  public void addReview(String review, String username);
  public void clearReviews();
  public ReviewsPageDto getReviews(Integer page);  
  public List<Review> getAllReviews();
}
