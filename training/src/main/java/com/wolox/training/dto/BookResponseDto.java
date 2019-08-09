package com.wolox.training.dto;

import com.wolox.training.models.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

  private Long id;
  private String author;
  private String genre;
  private String image;
  private String isbn;
  private Integer pages;
  private String publisher;
  private String subtitle;
  private String title;
  private String year;

  public BookResponseDto(Book book) {
    this.id = book.getId();
    this.author = book.getAuthor();
    this.genre = book.getGenre();
    this.image = book.getImage();
    this.isbn = book.getIsbn();
    this.pages = book.getPages();
    this.publisher = book.getPublisher();
    this.subtitle = book.getSubtitle();
    this.title = book.getTitle();
    this.year = book.getYear();
  }
}
