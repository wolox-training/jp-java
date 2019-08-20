package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

  @Id
  @Setter(AccessLevel.NONE)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
  @SequenceGenerator(name = "USER_SEQ", sequenceName = "users_id_seq", allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthdate;

  public User() {
    this.books = new ArrayList<>();
  }

  @JsonIgnore
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  private List<Book> books;

  public List<Book> getBooks() {
    return (List<Book>) Collections.unmodifiableList(books);
  }

  public void setBooks(List<Book> books) {
    this.books = books;
  }

  public void addBook(Book book) throws BookAlreadyOwnedException, BookNotFoundException {

    if (book == null) {
      throw new BookNotFoundException("the book cannot be null", 404);
    }
    if (this.books.contains(book)) {
      throw new BookAlreadyOwnedException("the user has already added this book to his list", 404);
    }
    books.add(book);
  }

  public void removeBook(Book book) throws BookNotFoundException {
    if (book == null) {
      throw new BookNotFoundException("the book cannot be null", 404);
    }
    books.remove(book);
  }
}
