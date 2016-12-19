package org.testmarket.service;

import java.math.BigDecimal;

import org.testmarket.domain.Account;
import org.testmarket.domain.Deal;
import org.testmarket.domain.FinType;

public interface DealService {

   BigDecimal getAveragePriceFinInstrument (FinType type);
   
   Deal addDeal(FinType type, BigDecimal price, BigDecimal amount, long count, Account buyer, Account seller); 
}
