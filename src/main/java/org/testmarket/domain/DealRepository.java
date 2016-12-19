package org.testmarket.domain;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DealRepository extends JpaRepository<Deal, Long> {

//    Optional<FinancialInstrument> findOneById(Long id);

//    Optional<FinancialInstrument> findOneById(Long id);
    
    @Query("select avg(values.price) "
        + "from #{#entityName} values  "
        + "where values.id in (select report.id from #{#entityName} report where report.type = ?1 order by report.id desc) "
        + "and rownum < 11 ")
    
    /**
     * Get average price of finance instrument over 10 last deals with this FinType. 
     * @param type FinType
     * @return averageValue
     */
    BigDecimal getFinTypAvgValue(FinType type); 
}