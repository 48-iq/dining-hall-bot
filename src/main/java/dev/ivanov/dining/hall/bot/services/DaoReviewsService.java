package dev.ivanov.dining.hall.bot.services;

import org.springframework.stereotype.Service;

import dev.ivanov.dining.hall.bot.dto.ReviewsPageDto;

@Service
public class DaoReviewsService implements ReviewService {

  @Override
  public void addReview(String review, String username) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addReview'");
  }

  @Override
  public String clearReviews() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'clearReviews'");
  }

  @Override
  public ReviewsPageDto getReviews(Integer page) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getReviews'");
  }
  
}
