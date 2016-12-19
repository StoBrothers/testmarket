package org.testmarket.service;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
@Service("tradeSrvice")
public class TradeSrviceImpl implements TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeSrviceImpl.class);

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

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW, rollbackFor = org.springframework.dao.PessimisticLockingFailureException.class)
    public long change(FinType type, Company seller, Company buyer, long count,
        BigDecimal delta) {

        long soldFinCount = 0;

        logger.debug(" START change seller: " + seller.getId() + " buyer: "
            + buyer.getId() + "count: " + count);

        FinancialInstrument finSeller = lockResourcesWithCount(type, seller);

        if ((finSeller != null) && (finSeller.getCount() > 0)) {
            logger.debug("Seller count " + finSeller.getCount());
        } else {
            return soldFinCount;
        }

        FinancialInstrument finBuyer = lockResources(type, buyer);

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
    @Transactional 
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

    /**
     * Update account financial information and information about deal
     * 
     * @param type
     * @param soldFinCount
     * @param accSeller
     * @param accBauyer
     */
    @Transactional
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
     * @param seller
     * @return FinancialInstrument if ok and exception is locking is finished with exception
     */
    @SuppressWarnings("finally")
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, noRollbackFor = org.springframework.dao.PessimisticLockingFailureException.class)
    private FinancialInstrument lockResources(FinType type, Company seller) {
        FinancialInstrument finBuyer = null;
        try {
            finBuyer = finRepository.findOneForUpdateByCompanyIdAndType(type,
                seller.getId());
            Account accBuyer = finBuyer.getAccount();
            em.lock(accBuyer, LockModeType.PESSIMISTIC_WRITE);
            logger.info("Locked: " + finBuyer.getId());
        } catch (Exception e) {
            logger.error("Lock is impossible: " + e.getClass().getName());
        } finally {
            return finBuyer;
        }
    }

    /**
     * Try to lock fin instrument and linked account for sell
     *
     * @param type
     * @param seller
     * @return FinancialInstrument if ok and exception is locking is finished with exception
     */
    @SuppressWarnings("finally")
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, noRollbackFor = org.springframework.dao.PessimisticLockingFailureException.class)
    private FinancialInstrument lockResourcesWithCount(FinType type, Company seller) {
        FinancialInstrument finSeller = null;
        try {
            finSeller = finRepository.findOneForUpdateByCompanyIdAndTypeAndCount(type,
                seller.getId());
            Account accSeller = finSeller.getAccount();
            em.lock(accSeller, LockModeType.PESSIMISTIC_WRITE);
            logger.info("Locked: " + finSeller.getId());
        } catch (Exception e) {
            logger.error("Lock is impossible: " + e.getClass().getName());
        } finally {
            return finSeller;
        }
    }

}
