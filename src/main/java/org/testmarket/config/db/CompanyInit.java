package org.testmarket.config.db;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.testmarket.domain.Account;
import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;
import org.testmarket.service.AccountService;
import org.testmarket.service.CompanyService;
import org.testmarket.service.FinancialInstrumentService;

/**
 * Create Companies with accounts  and fin instruments
 *   
 * @author Sergey Stotskiy
 *
 */

@Component("companyInit")
@DependsOn({ "applicationProperties" })
public class CompanyInit extends AbstractInit {
    
    private static final Logger logger = LoggerFactory.getLogger(CompanyInit.class);

    @Autowired
    CompanyService companyService;

    @Autowired
    FinancialInstrumentService finService;

    @Autowired
    AccountService accountService;
    
    private int accountNumber = 0;


    @Override
    protected void init() {

        try {
            create("YANDEX");
            create("IBM123");

        } catch (Exception e) {
            logger.error(" Can't to create companies " + e.getCause());
            System.exit(1);
        }

        // calculationBalance and Equity
        // 0 create deals 
        // 1 lock account
        // 2 get prices info
        // calculate account equity and update acc
        // send message to company
        // 
        
        Company yandCompany = checkCreatedCompany("YANDEX");

        logger.info(" YandexCompany " + yandCompany.toString());
        
        List<Account> accounts = yandCompany.getAccounts(); //
        
        for (Account account : accounts) {
            logger.info(" Account: " + account.toString());
        }
    }

    /**
     * Get and check company
     * 
     * @return
     */
    private Company checkCreatedCompany(final String companyName) {

        Optional<Company>  value  = companyService.findOneById(companyName);
        
        Company company = null;

        if (value.isPresent()) {
            company = value.get(); 
        }
        return company;
    }
    

    
    /**
     * Create accaounts and fin instruments for company 
     * 
     * @param companyPrefix
     * @throws Exception
     */
    public void create(String companyPrefix) throws Exception {

        Company company = companyService.getOrCreateCompany(companyPrefix);


        char finPrefix = companyPrefix.charAt(0);

        Account account = null;
        int i = 3;
        while (i > 0) {
            account = accountService
                .getOrCreateAccount("U" + String.format("%04d", accountNumber), company);
            accountNumber++;
            i--;
            createFin(accountNumber, finPrefix, account, 0, 10);
        }

        i = 4;
        while (i > 0) {
            account = accountService.getOrCreateAccount("M" + String.format("%04d", accountNumber),
                company);
            accountNumber++;
            i--;
            createFin(accountNumber, finPrefix, account, 0, 10);
        }

        i = 3;
        while (i > 0) {
            account = accountService.getOrCreateAccount("D" + String.format("%04d", accountNumber),
                company);
            accountNumber++;
            i--;
            createFin(accountNumber, finPrefix, account, 0, 10);
        }
    }

    /**
     * Create Fin instrument
     * @param accountNumber 
     * @param finPrefix
     * @param account
     * @param finNumber start number value 
     * @param count 
     * @throws Exception
     */
    private void createFin(int accountNumber, char finPrefix, Account account,
        int finNumber, int count) throws Exception {

        FinType[] types = FinType.values();

        count = types.length;
        
        while (count > 0) {

            StringBuilder builder = new StringBuilder();
            builder.append(finPrefix);
            builder.append(String.format("%03d", accountNumber));
            builder.append(String.format("%03d", finNumber));
            
            finService.getOrCreateFinInstrument(
                builder.toString(),
                account, types[--count]);
                finNumber++;
                
        }
    }
}
