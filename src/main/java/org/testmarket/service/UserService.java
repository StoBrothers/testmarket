package org.testmarket.service;

import java.util.Collection;
import java.util.Optional;

import org.testmarket.domain.User;

/**
 * Service for work with user entity.
 * 
 * @author Sergey Stotskiy
 */
public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> updateUser(User user);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

}
