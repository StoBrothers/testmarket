package org.testmarket.service;

import org.testmarket.domain.Account;
import org.testmarket.domain.Company;

/**
 * Account service.
 * 
 * @author Sergey Stotskiy
 *
 */

public interface AccountService {
    /**
     * Get account over id or create new
     * 
     * @param accountID
     * @param company
     * @return
     * @throws Exception
     */
    Account getOrCreateAccount(String accountID, Company company) throws Exception;
    


}
