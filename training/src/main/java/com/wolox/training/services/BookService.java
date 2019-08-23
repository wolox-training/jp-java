package com.wolox.training.services;

import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.models.Book;
import java.util.List;

public interface BookService {

  List<Book> getAllBook();

  void saveBook(Book bookInputDto);

  Book findBookById(Long id) throws BookNotFoundException;

  void updateBook(Long id, Book bookInputDto) throws BookNotFoundException;

  void deleteBook(Long id) throws BookNotFoundException;

  Book findBookByIsbn(String isbn);
}
