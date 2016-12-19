package org.testmarket.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Account repository.
 *
 * @author Sergey Stotskiy
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(" select acc from Account acc where acc.id like :accountId order by acc.id")
    List<Account> findByStartAccountID(@Param("accountId") String accountId);

    Optional<Account> findOneById(String id);

    List<Account> findAllByCompanyIdOrderById(String companyID);

}