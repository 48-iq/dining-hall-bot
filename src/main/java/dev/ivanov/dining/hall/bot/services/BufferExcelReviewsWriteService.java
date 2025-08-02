package dev.ivanov.dining.hall.bot.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.ivanov.dining.hall.bot.entities.Review;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BufferExcelReviewsWriteService implements ExcelReviewsWriteService {

  private final static Logger logger = LoggerFactory.getLogger(BufferExcelReviewsWriteService.class);

  private final ReviewService reviewService;

  @Override
  public InputStream writeReviews() {
    logger.info("get rerviews in xcel format");
    try (
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ) {
      try (
        Workbook workbook = new Workbook(outputStream, "sber-dining-hall-bot", "1.0")
      ) {
        Worksheet worksheet = workbook.newWorksheet("отзывы");
        worksheet.value(0, 0, "дата");
        worksheet.value(0, 1, "имя пользователя");
        worksheet.value(0, 2, "отзыв");
        
        List<Review> reviews = reviewService.getAllReviews();
  
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  
        for (int i = 0; i < reviews.size(); i++) {
          int r = i + 1;
          Review review = reviews.get(i);
          worksheet.value(r, 0, review.getDate().format(formatter));
          worksheet.value(r, 1, review.getUsername());
          worksheet.value(r, 2, review.getText());
        }
      } 
      byte[] buffer = outputStream.toByteArray();
      return new ByteArrayInputStream(buffer);
    } catch(IOException e) {
      logger.error("error processing menu update", e);
      throw new RuntimeException(e);
    }
  }
  
}
