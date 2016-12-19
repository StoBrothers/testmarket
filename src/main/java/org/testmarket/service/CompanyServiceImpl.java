package org.testmarket.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
/**
 * Company service.
 * 
 * @author Sergey Stotskiy
 *
 */
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    private static Pattern pattern = Pattern.compile("[\\w]{5,10}");

    /**
     * Create one Company
     *
     */
    @Override
    @Transactional
    public Company getOrCreateCompanyId(String companyID) throws Exception {

        Optional<Company> companyOpt = companyRepository.findOneById(companyID);

        if (companyOpt.isPresent()) {
            return companyOpt.get();
        }

        Matcher matcher = pattern.matcher(companyID);
        if (!matcher.find()) {
            throw new Exception(
                String.format("Not valid Id: %s for create company", companyID));
        }
        ;

        Company company = new Company();
        company.setId(companyID);
        companyRepository.save(company);
        return company;
    }

    public Optional<Company> findOneById(String id) {
        return companyRepository.findOneById(id);
    }
}
