package org.testmarket.service;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

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
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.DealRepository;
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
    DealRepository dealRepository;

    @Autowired
    DealService dealService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    FinancialInstrumentRepository finRepository;

    @Autowired
    AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    AccountService accountService;

    
    @Transactional  //(noRollbackFor = {PessimisticLockingFailureException.class, TransactionSystemException.class, javax.persistence.RollbackException.class})
    public long changeWithAttemps(FinType type, Company seller, Company buyer, long count, BigDecimal delta) {
        long resultCount = 0;
        long retry = 0;
        do {
            try {
                resultCount = change(type, seller, buyer, count, delta);
            } catch (PessimisticLockingFailureException | TransactionSystemException | javax.persistence.RollbackException e) {
                retry++;
                logger.error("Error " + e.getMessage());
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
        BigDecimal delta) {

        long soldFinCount = 0;

        logger.info(" START change seller: " + seller.getId() + " buyer: " + buyer.getId()
            + "count: " + count);

        FinancialInstrument finSeller, finBuyer;
        
        //for except deadlock 
        if(seller.getId().compareTo(buyer.getId()) > 0 ){
            finSeller = lockResources(type, seller, true);
            finBuyer = lockResources(type, buyer, false);
        } else {
            finBuyer = lockResources(type, buyer, false);
            finSeller = lockResources(type, seller, true);
        }
        
        if ((finSeller != null) && (finSeller.getCount() > 0)) {
            logger.debug("Seller count " + finSeller.getCount());
        } else {
            return soldFinCount;
        }

        if ((finBuyer != null) && (finBuyer.getCount() > 0)) {
            logger.debug("Buyer count " + finBuyer.getCount());
        } else {
            return soldFinCount;
        }


        Account accSeller = finSeller.getAccount();
        Account accBuyer = finBuyer.getAccount();

        // update fin positions count
        soldFinCount = updateFinPositionsCount(count, soldFinCount, finSeller, finBuyer);

        logger.debug(" Start account change: " + accSeller.getBalance() + " buyer count: "
            + accBuyer.getBalance() + " count: " + soldFinCount);

        // update account balance
        updateAccountsBalanceAndDeal(type, soldFinCount, accSeller, accBuyer, delta);

        logger.info(" Finished account change: " + accSeller.getBalance()
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

        long buyerFinCount = finBuyer.getCount();

        if (sellerFinCount > count) {
            soldFinCount = count;
        } else {
            soldFinCount = sellerFinCount;
        }

        buyerFinCount += soldFinCount;
        sellerFinCount -= soldFinCount;

        finBuyer.setCount(buyerFinCount);
        finSeller.setCount(sellerFinCount);

        finRepository.save(finBuyer);
        finRepository.save(finSeller);

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
        Account accSeller, Account accBauyer, BigDecimal delta) {

        BigDecimal price = dealService.getAveragePriceFinInstrument(type);

        price = price.add(delta); // add delta

        BigDecimal amount = price.multiply(BigDecimal.valueOf(soldFinCount));

        BigDecimal balanceBauer = accBauyer.getBalance();

        BigDecimal balanceSeller = accSeller.getBalance();

        accSeller.setBalance(balanceSeller.add(amount));

        accBauyer.setBalance(balanceBauer.add(amount.negate()));

        accountRepository.save(accBauyer);
        accountRepository.save(accSeller);

        dealService.addDeal(type, price, amount, soldFinCount, accBauyer, accSeller);

    }

    /**
     * Try to lock fin instrument and linked account for bought
     *
     * @param type
     * @param company
     * @param countNotNull if true that finding fin instrument with count > 0
     * @return FinancialInstrument if ok and exception is locking is finished with exception
     */
    private FinancialInstrument lockResources(FinType type, Company company,
        boolean countNotNull) {
        FinancialInstrument finInstrument = null;

        if (countNotNull) {
            finInstrument = finRepository.findOneForUpdateByCompanyIdAndTypeAndCount(type,
                company.getId());
        } else {
            finInstrument = finRepository.findOneForUpdateByCompanyIdAndType(type,
                company.getId());
        }
        Account accBuyer = finInstrument.getAccount();
        em.lock(accBuyer, LockModeType.PESSIMISTIC_WRITE);
        logger.debug("Locked: " + finInstrument.getId());
        return finInstrument;
    }
}
