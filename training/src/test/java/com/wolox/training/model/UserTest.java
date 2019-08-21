package com.wolox.training.model;

import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import java.time.LocalDate;
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
public class UserTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  public User getUserTest() {
    User user = new User();
    user.setName("Jhovanny");
    user.setUsername("jhovannywolox");
    user.setBirthdate(LocalDate.of(1986, 3, 19));
    return user;
  }

  @Test
  public void saveUser() {
    User user = getUserTest();
    User userSaved = entityManager.persist(user);
    Optional<User> userInDb = this.userRepository.findById(userSaved.getId());
    assertThat(userSaved).isEqualTo(userInDb.get());
  }

  @Test
  public void updateUser(){
    User user = getUserTest();
    User userSaved = entityManager.persist(user);
    String usernameModified = "jhovannywolox2019";
    userSaved.setUsername(usernameModified);
    entityManager.merge(userSaved);
    Optional<User> userInDb = this.userRepository.findById(userSaved.getId());
    assertThat(userSaved.getUsername()).isEqualTo(userInDb.get().getUsername());
  }

  @Test(expected = IllegalArgumentException.class)
  public void userNotSave() {
    User user = new User();
    user.setUsername("test");
    user.setName(null);
    user.setBirthdate(null);
    this.userRepository.save(user);
  }
}
