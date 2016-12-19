package org.testmarket.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Module repository.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findOneByName(String name);

    Optional<Module> findOneById(Long id);

}
