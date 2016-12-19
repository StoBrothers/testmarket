package org.testmarket.service;

import org.testmarket.domain.Account;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;
/**
 * FinancialInstrument service.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface FinancialInstrumentService {
    /**
     * Get or create new fin instrument
     * @param symbolID
     * @param account
     * @param type
     * @return
     * @throws Exception
     */
    FinancialInstrument getOrCreateFinInstrument(String symbolID, Account account, FinType type) throws Exception; 

}
