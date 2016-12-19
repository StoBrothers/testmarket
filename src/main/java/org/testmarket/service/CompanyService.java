package org.testmarket.service;

import java.util.Optional;

import org.testmarket.domain.Company;
/**
 * Company service.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface CompanyService {
    /**
     * Get company or create new over id
     * @param companyID
     * @return
     * @throws Exception
     */
    Company getOrCreateCompanyId(String companyID) throws Exception;
    
    /**
     * Find one company by id
     * @param id
     * @return
     */
    Optional<Company> findOneById(String id); 

}
