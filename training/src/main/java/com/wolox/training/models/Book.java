package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import com.google.common.base.Preconditions;
import com.wolox.training.utils.AppConstants;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book {

  @Id
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
  @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "book_id_seq", allocationSize = 1)
  private Long id;

  @Column
  private String genre;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private String image;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String subtitle;

  @Column(nullable = false)
  private String publisher;

  @Column(nullable = false)
  private String year;

  @Column(nullable = false)
  private Integer pages;

  @Column(nullable = false)
  private String isbn;

  @JsonIgnore
  @ManyToMany(mappedBy = "books")
  private List<User> user;

  public Book() {
    this.user = new ArrayList<>();
  }
  public void setGenre(String genre) {
    Preconditions.checkArgument(genre != null && !genre.isEmpty(),
        "the genre " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.genre = genre;
  }

  public void setAuthor(String author) {
    Preconditions.checkArgument(author != null && !author.isEmpty(),
        "the author " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.author = author;
  }

  public void setImage(String image) {
    Preconditions.checkArgument(image != null && !image.isEmpty(),
        "the image " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.image = image;
  }

  public void setTitle(String title) {
    Preconditions.checkArgument(title != null && !title.isEmpty(),
        "the title " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    Preconditions.checkArgument(subtitle != null && !subtitle.isEmpty(),
        "the subtitle " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.subtitle = subtitle;
  }

  public void setPublisher(String publisher) {
    Preconditions.checkArgument(publisher != null && !publisher.isEmpty(),
        "the publisher " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.publisher = publisher;
  }

  public void setYear(String year) {
    Preconditions.checkArgument(year != null && !year.isEmpty(),
        "the year " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.year = year;
  }

  public void setPages(Integer pages) {
    Preconditions.checkArgument(pages != null && pages > 0,
        "the pages " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.pages = pages;
  }

  public void setIsbn(String isbn) {
    Preconditions.checkArgument(isbn != null && !isbn.isEmpty(),
        "the isbn " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.isbn = isbn;
  }
}
