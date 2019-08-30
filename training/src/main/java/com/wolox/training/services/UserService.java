package com.wolox.training.services;

import com.wolox.training.exceptions.BookAlreadyOwnedException;
import com.wolox.training.exceptions.BookNotFoundException;
import com.wolox.training.exceptions.UserNotFoundException;
import com.wolox.training.models.User;
import java.util.List;

public interface UserService {

  List<User> getAllUser();

  void saveUser(User userInput);

  User findUserById(Long id) throws UserNotFoundException;

  void updateUser(Long id, User userInput) throws UserNotFoundException;

  void deleteUser(Long id) throws UserNotFoundException;

  void deleteBook(Long idUser, Long idBook) throws UserNotFoundException, BookNotFoundException;

  void addBook(Long idUser, Long idBook)
      throws UserNotFoundException, BookNotFoundException, BookAlreadyOwnedException;

  List<User> getUserByBirthdateAndName(String before, String after, String name);
}
