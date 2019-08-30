package com.wolox.training.repositories;

import com.wolox.training.models.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  User findFirstByUsername(String userName);

  @Query("SELECT T FROM User T WHERE (T.birthdate <:before or cast(:before as date) is null) and "
      + "(T.birthdate>:after or cast(:after as date) is null) and (T.name like %:name% or :name is null)")
  List<User> findByBirthdateAndNameIgnoreCaseSensative(@Param("after") LocalDate after,
      @Param("before") LocalDate before, @Param("name") String name, Pageable pageable);
}
