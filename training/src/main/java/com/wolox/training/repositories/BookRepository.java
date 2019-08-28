package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

  Book findFirstByAuthor(String author);

  Optional<Book> findByIsbn(String isbn);

  List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);
}
