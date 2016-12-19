package org.testmarket.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Company repository.
 * 
 * @author Sergey Stotskiy
 */
public interface CompanyRepository extends JpaRepository<Company, String> {

    Optional<Company> findOneById(String companyId);

//    Optional<Company> findOneById(Long id);
}