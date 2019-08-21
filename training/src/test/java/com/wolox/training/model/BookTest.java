package com.wolox.training.model;

import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private BookRepository bookRepository;

  public Book getBook(){
    Book book = new Book();
    book.setGenre("thriller");
    book.setIsbn("1245454");
    book.setPages(200);
    book.setPublisher("Amazon");
    book.setSubtitle("Amazon sub title");
    book.setYear("2019");
    book.setAuthor("wolox");
    book.setTitle("Front end basics");
    book.setImage("image/frotn.png");
    return book;
  }

  @Test
  public void saveBook() {
    Book book = getBook();
    Book bookSaved = this.entityManager.persist(book);
    Optional<Book> bookInDb = this.bookRepository.findById(bookSaved.getId());
    assertThat(bookSaved).isEqualTo(bookInDb.get());
  }

  @Test
  public void updateBokk() {
    Book book = getBook();
    Book bookSaved = this.entityManager.persist(book);
    String newGenre = "comedy";
    bookSaved.setGenre(newGenre);
    this.entityManager.merge(bookSaved);
    Optional<Book> bookInDb = this.bookRepository.findById(bookSaved.getId());
    assertThat(bookSaved.getGenre()).isEqualTo(bookInDb.get().getGenre());
  }

  @Test(expected = IllegalArgumentException.class)
  public void bookNotSave(){
    Book book = new Book();
    book.setImage(null);
    book.setGenre("comedy");
    this.bookRepository.save(book);
  }
}
