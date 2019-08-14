package com.wolox.training.models;

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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

  @Id
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
  @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "BOOK_SEQ")
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

  @ManyToMany(mappedBy = "books")
  private List<User> user;

  public void setGenre(String genre) {
    Preconditions.checkNotNull(genre,
        "the genre " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.genre = genre;
  }

  public void setAuthor(String author) {
    Preconditions.checkNotNull(genre,
        "the author " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.author = author;
  }

  public void setImage(String image) {
    Preconditions.checkNotNull(genre,
        "the image " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.image = image;
  }

  public void setTitle(String title) {
    Preconditions.checkNotNull(genre,
        "the title " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.title = title;
  }

  public void setSubtitle(String subtitle) {
    Preconditions.checkNotNull(genre,
        "the subtitle " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.subtitle = subtitle;
  }

  public void setPublisher(String publisher) {
    Preconditions.checkNotNull(genre,
        "the publisher " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.publisher = publisher;
  }

  public void setYear(String year) {
    Preconditions.checkNotNull(genre,
        "the year " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.year = year;
  }

  public void setPages(Integer pages) {
    Preconditions.checkNotNull(genre,
        "the pages " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.pages = pages;
  }

  public void setIsbn(String isbn) {
    Preconditions.checkNotNull(genre,
        "the isbn " + AppConstants.PRECONDITION_BOOK_MESSAGE_NULL);
    this.isbn = isbn;
  }
}
