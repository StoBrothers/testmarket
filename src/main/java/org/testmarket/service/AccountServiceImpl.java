package org.testmarket.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Account;
import org.testmarket.domain.AccountRepository;
import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;
import org.testmarket.domain.FinancialInstrumentRepository;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory
        .getLogger(AccountServiceImpl.class);

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    DealService dealService;

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

    @Override
    public List<Account> findByCompanyId(String companyID) {
        return accountRepository.findAllByCompanyIdOrderById(companyID);
    }

    @Autowired
    FinancialInstrumentRepository finRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * @param count
     * @param soldFinCount
     * @param finSeller
     * @param finBuyer
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private long updateFinPositionsCount(long count, long soldFinCount,
        FinancialInstrument finSeller, FinancialInstrument finBuyer) {
        long sellerFinCount = finSeller.getCount();

        long buyerFinCount = finBuyer.getCount();

        if (sellerFinCount > count) {
            soldFinCount = count;
        }

        buyerFinCount += soldFinCount;
        sellerFinCount -= soldFinCount;

        finBuyer.setCount(buyerFinCount);
        finSeller.setCount(sellerFinCount);

        finRepository.save(finBuyer);
        finRepository.save(finSeller);

        return soldFinCount;
    }

    @Override
    @SuppressWarnings("finally")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, noRollbackFor = javax.persistence.RollbackException.class)
    public synchronized FinancialInstrument findResource(FinType type, Company seller) {
        FinancialInstrument finSeller = null;
        try {
            finSeller = finRepository.findOneForUpdateByCompanyIdAndType(type,
                seller.getId());
        } catch (Exception e) {
            logger.error(e.toString());
        } finally {
            return finSeller;
        }
    }
}
