package com.wolox.training.services.impl;

import com.wolox.training.dto.BookInputDto;
import com.wolox.training.dto.BookResponseDto;
import com.wolox.training.exceptions.BookException;
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
  public List<BookResponseDto> getAllBook() {
    return this.bookRepository.findAll().parallelStream().map(BookResponseDto::new).collect(
        Collectors.toList());
  }

  @Override
  public void saveBook(BookInputDto bookInputDto) {

    log.info("saving book..., {}", bookInputDto);

    Book book = new Book();
    book.setAuthor(bookInputDto.getAuthor());
    book.setGenre(bookInputDto.getGenre());
    book.setImage(bookInputDto.getImage());
    book.setIsbn(bookInputDto.getIsbn());
    book.setPages(bookInputDto.getPages());
    book.setPublisher(bookInputDto.getPublisher());
    book.setSubtitle(bookInputDto.getSubtitle());
    book.setTitle(bookInputDto.getTitle());
    book.setYear(book.getYear());

    this.bookRepository.save(book);
  }

  @Override
  public BookResponseDto findBookById(@Header("id") Long id) throws BookException {
    Optional<Book> book = this.bookRepository.findById(id);
    if (book.isPresent()) {
      return new BookResponseDto(book.get());
    } else {
      throw new BookException("Book not found", 404);
    }
  }

  @Override
  public void updateBook(@Header("id") Long id, BookInputDto bookInputDto) throws BookException {
    Optional<Book> book = this.bookRepository.findById(id);
    if (book.isPresent()) {
      Book bookUpdate = book.get();
      bookUpdate.setYear(bookInputDto.getYear());
      bookUpdate.setTitle(bookInputDto.getTitle());
      bookUpdate.setSubtitle(bookInputDto.getSubtitle());
      bookUpdate.setPublisher(bookInputDto.getPublisher());
      bookUpdate.setPages(bookInputDto.getPages());
      bookUpdate.setIsbn(bookInputDto.getIsbn());
      bookUpdate.setGenre(bookInputDto.getGenre());
      bookUpdate.setImage(bookInputDto.getImage());
      this.bookRepository.save(bookUpdate);
    } else {
      throw new BookException("Book not found", 404);
    }


  }

  @Override
  public void deleteBook(@Header("id") Long id) throws BookException {
    Optional<Book> book = this.bookRepository.findById(id);
    if (book.isPresent()) {
      this.bookRepository.delete(book.get());
    } else {
      throw new BookException("Book not found", 404);
    }
  }
}
