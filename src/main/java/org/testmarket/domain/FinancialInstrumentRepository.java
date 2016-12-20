package org.testmarket.domain;

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
public interface FinancialInstrumentRepository
    extends JpaRepository<FinancialInstrument, String> {

    Optional<FinancialInstrument> findOneById(String id);

    Optional<FinancialInstrument> findOneByAccountIdAndType(String id, FinType type);
    
    @Query(" select sum(fin.count) from FinancialInstrument fin "
        + "where fin.companyId = :companyId and fin.type = :type ") 

    Long sumCountByCompanyIdAndType(
        @Param("type") FinType type, @Param("companyId") String companyId);

    

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "-1"),
    @QueryHint(name = "javax.persistence.query.timeout", value = "-1") })

    @Query(" select fin from FinancialInstrument fin "
        + "where fin.companyId = :companyId and fin.type = :type and rownum <2 ") //

    FinancialInstrument findOneForUpdateByCompanyIdAndType(@Param("type") FinType type,
        @Param("companyId") String companyId);

    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "-1"),
    @QueryHint(name = "javax.persistence.query.timeout", value = "-1") })

    @Query(" select fin from FinancialInstrument fin "
        + "where fin.companyId = :companyId and fin.type = :type and fin.count > 0 and rownum <2 ") //

    FinancialInstrument findOneForUpdateByCompanyIdAndTypeAndCount(
        @Param("type") FinType type, @Param("companyId") String companyId);

}