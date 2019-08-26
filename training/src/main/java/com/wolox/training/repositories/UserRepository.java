package com.wolox.training.repositories;

import com.wolox.training.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findFirstByUsername(String userName);
}
