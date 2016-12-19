package org.testmarket.domain;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

/**
 * FinancialInstrument repository.
 * 
 * @author Sergey Stotskiy
 */
public interface FinancialInstrumentRepository extends JpaRepository<FinancialInstrument, String> {

    Optional<FinancialInstrument> findOneById(String id);

    Optional<FinancialInstrument> findOneByAccountIdAndType(String id, FinType type);

//    Optional<FinancialInstrument> findOneByAccountIdAndType(String id, FinType type);

    
//    Optional<FinancialInstrument> findOneById(Long id);
//    @Query("select fin from Company comp "
//        + "inner join comp.accounts acc "
//        + "inner join acc.finInstruments fin "
//        + "where comp.id = :company_id  and fin.type = :type and fin.count > 0  and rownum = 1 ")

    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHint(name=QueryHints.HINT_TIMEOUT)
    //"javax.persistence.query.timeout", 1000
    @Query(" select fin from FinancialInstrument fin "
        + "where fin.account in (:accounts) and fin.type = :type and rownum = 1")// 
    
    FinancialInstrument findOneForUpdateByAccountAndType(@Param("type") FinType type, @Param("accounts") List<Account> accounts);
    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
    @QueryHint(name="javax.persistence.lock.timeout", value = "-1"),
    @QueryHint(name="javax.persistence.query.timeout", value="-1")})
        
    @Query(" select fin from FinancialInstrument fin "
      + "where fin.companyId = :companyId and fin.type = :type and rownum <2 ")// 
    
  
  FinancialInstrument findOneForUpdateByCompanyIdAndType(@Param("type") FinType type, @Param("companyId") String companyId); 

    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
    @QueryHint(name="javax.persistence.lock.timeout", value = "-1"),
    @QueryHint(name="javax.persistence.query.timeout", value="-1")})
        
    @Query(" select fin from FinancialInstrument fin "
      + "where fin.companyId = :companyId and fin.type = :type and fin.count > 0 and rownum <2 ")// 
    
  
  FinancialInstrument findOneForUpdateByCompanyIdAndTypeAndCount(@Param("type") FinType type, @Param("companyId") String companyId); 

}