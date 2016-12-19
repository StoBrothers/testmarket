package org.testmarket.service;

import org.testmarket.domain.Account;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;

public interface FinancialInstrumentService {

    FinancialInstrument getOrCreateFinInstrument(String symbolID, Account account, FinType type) throws Exception; 

     void change ();
    
}
