package com.wolox.training.services.impl;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.BookService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

  @Autowired
  private BookRepository bookRepository;

  @Override
  public List<Book> getAllBook() {
    return this.bookRepository.findAll();
  }

  @Override
  public void saveBook(Book bookInput) {

    log.info("saving book..., {}", bookInput);
    this.bookRepository.save(bookInput);
  }

  @Override
  public Book findBookById(@Header("id") Long id) throws BookNotFoundException {
    Optional<Book> book = this.bookRepository.findById(id);
    if (book.isPresent()) {
      return book.get();
    } else {
      throw new BookNotFoundException("Book not found", 404);
    }
  }

  @Override
  public void updateBook(@Header("id") Long id, Book bookInput) throws BookNotFoundException {
    Optional<Book> book = this.bookRepository.findById(id);
    if (book.isPresent()) {
      Book bookUpdate = book.get();
      bookUpdate.setYear(bookInput.getYear());
      bookUpdate.setTitle(bookInput.getTitle());
      bookUpdate.setSubtitle(bookInput.getSubtitle());
      bookUpdate.setPublisher(bookInput.getPublisher());
      bookUpdate.setPages(bookInput.getPages());
      bookUpdate.setIsbn(bookInput.getIsbn());
      bookUpdate.setGenre(bookInput.getGenre());
      bookUpdate.setImage(bookInput.getImage());
      this.bookRepository.save(bookUpdate);
    } else {
      throw new BookNotFoundException("Book not found", 404);
    }
  }

  @Override
  public void deleteBook(@Header("id") Long id) throws BookNotFoundException {
    Optional<Book> book = this.bookRepository.findById(id);
    if (book.isPresent()) {
      this.bookRepository.delete(book.get());
    } else {
      throw new BookNotFoundException("Book not found", 404);
    }
  }
}
