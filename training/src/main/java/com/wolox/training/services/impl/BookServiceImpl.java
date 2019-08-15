package com.wolox.training.services.impl;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.services.BookService;
import java.util.List;
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
    return this.bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404));
  }

  @Override
  public void updateBook(@Header("id") Long id, Book bookInput) throws BookNotFoundException {
    Book book = this.bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404));
    book.setYear(bookInput.getYear());
    book.setTitle(bookInput.getTitle());
    book.setSubtitle(bookInput.getSubtitle());
    book.setPublisher(bookInput.getPublisher());
    book.setPages(bookInput.getPages());
    book.setIsbn(bookInput.getIsbn());
    book.setGenre(bookInput.getGenre());
    book.setImage(bookInput.getImage());
    this.bookRepository.save(book);
  }

  @Override
  public void deleteBook(@Header("id") Long id) throws BookNotFoundException {
    Book book = this.bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404));
    this.bookRepository.delete(book);
  }
}
