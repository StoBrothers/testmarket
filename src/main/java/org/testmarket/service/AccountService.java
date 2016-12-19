package org.testmarket.service;

import java.util.List;

import org.testmarket.domain.Account;
import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;

public interface AccountService {
    
    Account getOrCreateAccount(String accountID, Company company) throws Exception;

    List <Account> findByCompanyId(String companyId);
    
    FinancialInstrument findResource(FinType type, Company seller);



}
