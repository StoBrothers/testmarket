package org.testmarket.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User repository.
 * 
 * @author Sergey Stotskiy
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneById(Long id);

}