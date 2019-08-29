package com.wolox.training.repositories;

import com.wolox.training.models.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

  Book findFirstByAuthor(String author);

  Optional<Book> findByIsbn(String isbn);

  @Query("SELECT T FROM Book T WHERE (T.publisher=:publisher or :publisher is null) and "
      + "(T.genre=:genre or :genre is null) and (T.year=:year or :year is null)")
  List<Book> findByPublisherAndGenreAndYear(@Param("publisher") String publisher,
      @Param("genre") String genre, @Param("year") String year);
}
