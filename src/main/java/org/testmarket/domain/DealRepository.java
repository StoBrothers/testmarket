package org.testmarket.domain;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Deals repository.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface DealRepository extends JpaRepository<Deal, Long> {

    @Query("select avg(values.price) " + "from #{#entityName} values  "
        + "where values.id in (select report.id from #{#entityName} report where report.type = ?1 order by report.id desc) "
        + "and rownum < 11 ")

    /**
     * Get average price of finance instrument over 10 last deals with this FinType.
     * 
     * @param type FinType
     * @return averageValue
     */
    BigDecimal getAvgValueByType(FinType type);
}