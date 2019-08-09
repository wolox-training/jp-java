package com.wolox.training.services;

import com.wolox.training.dto.BookInputDto;
import com.wolox.training.dto.BookResponseDto;
import com.wolox.training.exceptions.BookException;
import java.util.List;

public interface BookService {

  List<BookResponseDto> getAllBook();

  void saveBook(BookInputDto bookInputDto);

  BookResponseDto findBookById(Long id) throws BookException;

  void updateBook(Long id, BookInputDto bookInputDto) throws BookException;

  void deleteBook(Long id) throws BookException;
}
