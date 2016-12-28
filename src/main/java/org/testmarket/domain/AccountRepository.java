package org.testmarket.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    
    @Query(" select sum(acc.balance) from Account acc where acc.company.id like :companyId ")
    BigDecimal sumBalancetByCompany(@Param("companyId") String companyId);

    @Query(" select sum(acc.balance) from Account acc ")
    BigDecimal sumBalanceAll();
    

    Optional<Account> findOneById(String id);

    List<Account> findAllByCompanyIdOrderById(String companyID);
    
    @Modifying
    @Query(" update Account acc "
        + "set acc.balance = case when (acc.id like :sellerId) then (acc.balance - :delta) "
        + "else (acc.balance + :delta) end  "
        + "where  (acc.id like :sellerId)  or (acc.id like :bayerId) order by acc.id")   
    void updateBalances(@Param("sellerId") String sellerId, @Param("bayerId") String bayerId, @Param("delta") BigDecimal delta);


}