package com.wolox.training.models;

import com.google.common.base.Preconditions;
import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
  @SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ")
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

  public void setUsername(String username) {
    Preconditions.checkNotNull(username, "the username for a book cannot be null and void" );
    this.username = username;
  }

  public void setName(String name) {
    Preconditions.checkNotNull(name, "the name for a book cannot be null and void" );
    this.name = name;
  }

  public void setBirthdate(LocalDate birthdate) {
    Preconditions.checkNotNull(birthdate, "the birthdate for a book cannot be null and void" );
    this.birthdate = birthdate;
  }
}
