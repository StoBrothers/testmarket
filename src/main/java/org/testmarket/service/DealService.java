package org.testmarket.service;

import java.math.BigDecimal;

import org.testmarket.domain.Account;
import org.testmarket.domain.Deal;
import org.testmarket.domain.FinType;
/**
 * Deal service.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface DealService {
    /**
     * Get average price of finInstruments over 10 last deals 
     * @param type 
     * @return average price
     */
   BigDecimal getAveragePriceFinInstrument (FinType type);

   /**
    * Add new deal and recalculate new average price 
    * 
    * @param type
    * @param price new price
    * @param amount amount cost
    * @param count 
    * @param buyer 
    * @param seller
    * @return deal
    */
   Deal addDeal(FinType type, BigDecimal price, BigDecimal amount, long count, Account buyer, Account seller); 
}
