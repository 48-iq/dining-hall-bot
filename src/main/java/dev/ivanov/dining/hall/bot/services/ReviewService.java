package dev.ivanov.dining.hall.bot.services;

import dev.ivanov.dining.hall.bot.dto.ReviewsPageDto;

public interface ReviewService {
  public void addReview(String review, String username);
  public String clearReviews();
  public ReviewsPageDto getReviews(Integer page);  
}
