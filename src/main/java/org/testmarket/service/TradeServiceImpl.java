package org.testmarket.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Account;
import org.testmarket.domain.AccountRepository;
import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;
import org.testmarket.domain.FinancialInstrumentRepository;

/**
 * Service for trades
 *
 * @author Sergey Stotskiy
 */
@Service("tradeService")
public class TradeServiceImpl implements TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    DealService dealService;

    @Autowired
    FinancialInstrumentRepository finRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    @Transactional(noRollbackFor = { PessimisticLockingFailureException.class, 
        TransactionSystemException.class, javax.persistence.RollbackException.class })
    public long changeWithAttemps(FinType type, Company seller, Company buyer, long count,
        BigDecimal delta) {
        long resultCount = 0;
        long retry = 0;
        do {
            try {
                BigDecimal price = dealService.getAveragePriceFinInstrument(type);
                resultCount = change(type, seller, buyer, count, price.add(delta));
                BigDecimal amount = price.multiply(BigDecimal.valueOf(resultCount));
                // for calculation average value
                dealService.addDeal(type, price, amount, resultCount);
            } catch (PessimisticLockingFailureException | TransactionSystemException | javax.persistence.RollbackException e) {
                retry++; // so one next attempts to execute this deal
                logger.error("Error attempts:" + e.getMessage());
            }
        } while (resultCount == 0);

        if (retry != 0) {
            logger.info("Attempts was " + retry);
        }
        return resultCount;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public long change(FinType type, Company seller, Company buyer, long count,
        BigDecimal price) {

        long soldFinCount = 0;

        logger.debug(" START change seller: " + seller.getId() + " buyer: " + buyer.getId()  + "count: " + count);

        // Get resources and try to except deadlock
        FinancialInstrument[] finInstruments = getResources(type, seller, buyer);
        if (finInstruments == null) {
            return 0;
        }

        FinancialInstrument finSeller = finInstruments[0];
        FinancialInstrument finBuyer = finInstruments[1];

        Account accSeller = finSeller.getAccount();
        Account accBuyer = finBuyer.getAccount();

        // update fin positions count
        soldFinCount = updateFinPositionsCount(count, soldFinCount, finSeller, finBuyer);

        logger.debug(" Start account change: " + accSeller.getBalance() + " buyer count: "
            + accBuyer.getBalance() + " count: " + soldFinCount);

        // update account balance
        updateAccountsBalanceAndDeal(type, soldFinCount, accSeller, accBuyer, price);

        logger.debug(" Finished account change: " + accSeller.getBalance()
            + " buyer count: " + accBuyer.getBalance() + " count: " + soldFinCount);

        return soldFinCount;
    }

    /**
     * Update financialInstruments information after finishing deal
     *
     * @param count
     * @param soldFinCount
     * @param finSeller
     * @param finBuyer
     * @return
     */
    private long updateFinPositionsCount(long count, long soldFinCount,
        FinancialInstrument finSeller, FinancialInstrument finBuyer) {
        long sellerFinCount = finSeller.getCount();

        if (sellerFinCount > count) {
            soldFinCount = count;
        } else {
            soldFinCount = sellerFinCount;
        }

        finBuyer.addCount(soldFinCount);
        finSeller.addCount(-soldFinCount);

        Set<FinancialInstrument> finInstrumnets = new HashSet<>();
        finInstrumnets.add(finBuyer);
        finInstrumnets.add(finSeller);

        finRepository.save(finInstrumnets);
        return soldFinCount;
    }

    /**
     * Update account financial information and information about deal
     *
     * @param type
     * @param soldFinCount
     * @param accSeller
     * @param accBauyer
     */
    private void updateAccountsBalanceAndDeal(FinType type, long soldFinCount,
        Account accSeller, Account accBauyer, BigDecimal price) {

        BigDecimal amount = price.multiply(BigDecimal.valueOf(soldFinCount));

        accountRepository.updateBalances(accSeller.getId(), accBauyer.getId(), amount);
    }

    /**
     * Try to lock fin instrument and linked account for bought
     *
     * @param type
     * @param company
     * @param countNotNull
     *            if true that finding fin instrument with count > 0
     * @return FinancialInstrument if ok and exception is locking is finished with exception
     */
    private FinancialInstrument lockResources(FinType type, Company company,
        boolean countNotNull) {
        FinancialInstrument finInstrument = null;

        synchronized (company) {
            finInstrument = lockFinResource(type, company, countNotNull);
        }

        if (finInstrument == null) {
            return null;
        }

        return finInstrument;
    }

    /**
     * Lock fin resource 
     * 
     * @param type
     * @param company
     * @param countNotNull
     * @return
     */
    private FinancialInstrument lockFinResource(FinType type, Company company,
        boolean countNotNull) {
        FinancialInstrument finInstrument;
        if (countNotNull) {
            finInstrument = finRepository.findOneForUpdateByCompanyIdAndTypeAndCount(type,
                company.getId());
        } else {
            finInstrument = finRepository.findOneForUpdateByCompanyIdAndType(type,
                company.getId());
        }
        return finInstrument;
    }

    /**
     * Get resources
     *  
     * @param type
     * @param seller
     * @param buyer
     * @return [0] seller, [1] buyer
     */
    private FinancialInstrument[] getResources(FinType type, Company seller,
        Company buyer) {
        FinancialInstrument[] finInstruments = new FinancialInstrument[2];

        if (seller.getId().compareTo(buyer.getId()) > 0) {
            finInstruments[0] = lockResources(type, seller, true);
            if ((finInstruments[0] == null) || (finInstruments[0].getCount() <= 0)) {
                return null;
            }
            finInstruments[1] = lockResources(type, buyer, false);
            if (finInstruments[1] == null) {
                return null;
            }
        } else {
            finInstruments[1] = lockResources(type, buyer, false);
            if (finInstruments[1] == null) {
                return null;
            }
            finInstruments[0] = lockResources(type, seller, true);
            if ((finInstruments[0] == null) || (finInstruments[0].getCount() <= 0)) {
                return null;
            }
        }

        return finInstruments;
    }

}
