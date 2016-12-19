package org.testmarket.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Account;
import org.testmarket.domain.AccountRepository;
import org.testmarket.domain.Company;
/**
 * Account service.
 * 
 * @author Sergey Stotskiy
 *
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    private static Pattern pattern = Pattern.compile("[U,M,D]\\d{4,7}");

    /**
     * Create one Account
     *
     * @throws Exception
     *
     */
    @Override
    @Transactional
    public Account getOrCreateAccount(String accountID, Company company)
        throws Exception {

        Optional<Account> value = accountRepository.findOneById(accountID);

        if (value.isPresent()) {
            return value.get();
        }

        Matcher matcher = pattern.matcher(accountID);
        if (!matcher.find()) {
            throw new Exception(
                String.format("Not valid accountID: %s for create account", accountID));
        }
        ;

        Account account = new Account();
        account.setId(accountID);
        account.setCompany(company);
        accountRepository.save(account);
        return account;
    }

}
