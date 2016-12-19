package org.testmarket.service;

import java.util.Optional;

import org.testmarket.domain.Company;

public interface CompanyService {

    Company getOrCreateCompany(String companyID) throws Exception;
    
    Optional<Company> findOneById(String id); 

}
