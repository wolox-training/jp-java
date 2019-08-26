package com.wolox.training.dto;

import com.wolox.training.models.Book;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

  private String ISBN;
  private String title;
  private String subtitle;
  private String publishers;
  private String publish_date;
  private Integer number_of_pages;
  private List<String> authors;

  public BookDto(Book book) {
    this.ISBN = book.getIsbn();
    this.title = book.getTitle();
    this.subtitle = book.getSubtitle();
    this.publish_date = book.getYear();
    this.number_of_pages = book.getPages();
    this.publishers = book.getPublisher();
  }
}
