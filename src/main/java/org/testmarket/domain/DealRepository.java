package org.testmarket.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Deals repository.
 *
 * @author Sergey Stotskiy
 *
 */
public interface DealRepository extends JpaRepository<Deal, Long> {

}