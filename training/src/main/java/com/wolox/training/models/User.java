package com.wolox.training.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.utils.AppConstants;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
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

  public void setUsername(String username) {
    Preconditions.checkArgument(username != null && !username.isEmpty(),
        "the username " + AppConstants.PRECONDITION_USER_MESSAGE_NULL);
    this.username = username;
  }

  public void setName(String name) {
    Preconditions.checkArgument(name != null && !name.isEmpty(),
        "the name " + AppConstants.PRECONDITION_USER_MESSAGE_NULL);
    this.name = name;
  }

  public void setBirthdate(LocalDate birthdate) {
    Preconditions.checkArgument(name != null && !name.isEmpty(),
        "the birthdate " + AppConstants.PRECONDITION_USER_MESSAGE_NULL);
    this.birthdate = birthdate;
  }
}
