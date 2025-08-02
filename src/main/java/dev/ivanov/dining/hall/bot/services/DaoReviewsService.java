package dev.ivanov.dining.hall.bot.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.ivanov.dining.hall.bot.dto.ReviewsPageDto;
import dev.ivanov.dining.hall.bot.entities.Review;
import dev.ivanov.dining.hall.bot.repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DaoReviewsService implements ReviewService {

  private static final Logger logger = LoggerFactory.getLogger(DaoReviewsService.class);

  private final ReviewRepository reviewRepository;

  private final TextService textService;

  @Value("${app.bot.reviewsPageSize}")
  private Integer pageSize;

  @Override
  @Transactional
  public void addReview(String review, String username) {
    logger.info("addReview - review: {} - username: {}", review, username);
    Review entity = Review.builder()
      .text(review)
      .username(username)
      .date(LocalDateTime.now())
      .build();
    reviewRepository.save(entity);
  }

  @Override
  @Transactional
  public void clearReviews() {
    logger.info("clear all reviews");
    reviewRepository.deleteAll();
  }

  @Override
  @Transactional
  public ReviewsPageDto getReviews(Integer page) {
    logger.info("getReviews - page: {}", page);
    PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by("date").descending());
    Page<Review> reviewsPage = reviewRepository.findAll(pageRequest);
    Integer totalPages = reviewsPage.getTotalPages();

    List<Review> reviews = reviewsPage.getContent();

    if (reviewsPage.isEmpty()) return ReviewsPageDto
      .builder()
      .text(textService.getText("emptyReviews"))
      .totalPages(totalPages)
      .build();

    StringBuilder textBuilder = new StringBuilder();
    textBuilder
      .append("<b>--" + (page + 1) + "--</b>\n\n");

    for (Review review : reviews) {
      textBuilder
        .append("*\n")
        .append("<b>" + review.getUsername() + "</b>\n")
        .append(review.getText() + "\n\n");
    }

    return ReviewsPageDto
      .builder()
      .text(textBuilder.toString())
      .totalPages(totalPages)
      .build();
  }

  @Override
  public List<Review> getAllReviews() {
    logger.trace("getAllReviews");
    return reviewRepository.findAll();
  }
  
}
