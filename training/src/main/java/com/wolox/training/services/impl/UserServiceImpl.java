package com.wolox.training.services.impl;

import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.Book;
import com.wolox.training.models.User;
import com.wolox.training.repositories.BookRepository;
import com.wolox.training.repositories.UserRepository;
import com.wolox.training.services.UserService;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
  public User findUserById(@Header("id") Long id) throws UserNotFoundException {
    return this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404));
  }

  @Override
  public void updateUser(@Header("id") Long id, User userInput) throws UserNotFoundException {
    User user = this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404));
    user.setBirthdate(userInput.getBirthdate());
    user.setName(userInput.getName());
    user.setUsername(userInput.getUsername());
    this.userRepository.save(user);
  }

  @Override
  public void deleteUser(@Header("id") Long id) throws UserNotFoundException {
    User user = this.userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404));
    this.userRepository.delete(user);
  }

  @Override
  public void deleteBook(@Header("iduser") Long idUser,@Header("idbook") Long idBook)
      throws UserNotFoundException, BookNotFoundException {
    User user = this.userRepository.findById(idUser)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404));
    Book book = this.bookRepository.findById(idBook)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404));
    user.removeBook(book);
    this.userRepository.save(user);
  }

  @Override
  public void addBook(@Header("iduser") Long idUser, @Header("idbook") Long idBook)
      throws UserNotFoundException, BookNotFoundException, BookAlreadyOwnedException {
    User user = this.userRepository.findById(idUser)
        .orElseThrow(() -> new UserNotFoundException("User not found", 404));
    Book book = this.bookRepository.findById(idBook)
        .orElseThrow(() -> new BookNotFoundException("Book not found", 404));
    user.addBook(book);
    this.userRepository.save(user);
  }

  @Override
  public List<User> getUserByBirthdateAndName(@Header("before") String before,
      @Header("after") String after, @Header("name") String name) {
    return this.userRepository.findByBirthdateAndNameIgnoreCaseSensative(LocalDate.parse(before),
        LocalDate.parse(after), name);
  }
}
