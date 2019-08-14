package com.wolox.training.services.impl;

import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import com.wolox.training.services.UserService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @Override
  public List<User> getAllUser() {
    return this.userRepository.findAll();
  }

  @Override
  public void saveUser(User userInput) {

    log.info("saving book..., {}", userInput);
    this.userRepository.save(userInput);
  }

  @Override
  public User findUserById(Long id) throws UserNotFoundException {
    Optional<User> user = Optional.ofNullable(this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404)));
      return user.get();
  }

  @Override
  public void updateUser(Long id, User userInput) throws UserNotFoundException {
    Optional<User> user = Optional.ofNullable(this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404)));
      User userUpdate = user.get();
      userUpdate.setBirthdate(userInput.getBirthdate());
      userUpdate.setName(userInput.getName());
      userUpdate.setUsername(userInput.getUsername());
      this.userRepository.save(userUpdate);
  }

  @Override
  public void deleteUser(Long id) throws UserNotFoundException {
    Optional<User> user = Optional.ofNullable(this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404)));
      this.userRepository.delete(user.get());
  }

  @Override
  public void deleteBook(Long idUser, Long idBook)
      throws UserNotFoundException, BookNotFoundException {
    Optional<User> user = Optional.ofNullable(this.userRepository.findById(idUser)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404)));
    Optional<Book> book = Optional.ofNullable(this.bookRepository.findById(idBook)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404)));
    user.get().removeBook(book.get());
    this.userRepository.save(user.get());
  }

  @Override
  public void addBook(Long idUser, Long idBook)
      throws UserNotFoundException, BookNotFoundException, BookAlreadyOwnedException {
    Optional<User> user = Optional.ofNullable(this.userRepository.findById(idUser)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404)));
    Optional<Book> book = Optional.ofNullable(this.bookRepository.findById(idBook)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404)));
    user.get().addBook(book.get());
    this.userRepository.save(user.get());
  }
}
