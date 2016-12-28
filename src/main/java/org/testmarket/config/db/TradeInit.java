package org.testmarket.config.db;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.DealRepository;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrumentRepository;
import org.testmarket.reports.ReportService;
import org.testmarket.service.AccountService;
import org.testmarket.service.DealService;
import org.testmarket.service.FinancialInstrumentService;
import org.testmarket.service.TradeService;
import org.testmarket.service.statistic.StatisticService;

/**
 * This module contains test: Starting 4 concurrency threads for 2 companies. This threads try to
 * sell and to buy finInstruments only AER type. We can to observe rollback state and failed
 * transaction.
 *
 * @author Sergey Stotskiy
 *
 */

@Component("tradeInit")
@DependsOn({ "dealsInit" })
public class TradeInit extends AbstractInit {

    private static final Logger logger = LoggerFactory.getLogger(TradeInit.class);

    @Autowired
    DealRepository dealRepository;

    @Autowired
    DealService dealService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    FinancialInstrumentRepository finRepository;

    @Autowired
    FinancialInstrumentService finService;

    @Autowired
    ReportService report;

    @Autowired
    AccountService accountService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    TradeService tradeService;

    @Autowired
    StatisticService statisticService;

    @Override
    protected void init() {

        testMultiThreadsDeals();
    }

    /**
     * Starting 4 concurrency threads for 2 companies. This threads try to sell and to buy
     * finInstruments only AER type. We can to observe rollback state and failed transaction.
     * Application don't try to execute failed deals and app only skiped this deals in this test.
     */
    private void testMultiThreadsDeals() {

        List<Company> companys = companyRepository.findAll();

        Company cmpBuyer = companys.get(1);

        Company cmpSeller = companys.get(0);

        logger.info(" Those companies : " + cmpSeller.getId() + " , " + cmpBuyer.getId()
            + " will be participate in trades ");

        final int countDeals = 1000;

        Thread r1 = new Thread() {
            @Override
            public void run() {

                for (int i = 0; i < countDeals; i++) { // count of try make deal
                    try {
                        tradeService.changeWithAttemps(FinType.AER, cmpSeller, cmpBuyer,
                            1, new BigDecimal(-3));
                    } catch (Exception e) {
                        statisticService.addRolbacks();
                        logger.error("Thread 1 " + e.getMessage() + e.getMessage()
                            + e.getClass().getName());
                    }
                } //
                statisticService.addFnishedDeals(countDeals);
            }
        };

        Thread r2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < countDeals; i++) { // count of try make deal
                    try {
                        tradeService.changeWithAttemps(FinType.AER, cmpSeller, cmpBuyer,
                            1, new BigDecimal(3));
                    } catch (Exception e) {
                        statisticService.addRolbacks();
                        logger.error("Thread 2 " + e.getMessage() + e.getMessage()
                            + e.getClass().getName());
                    }
                }
                statisticService.addFnishedDeals(countDeals);
            }
        };

        Thread r3 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < countDeals; i++) { // count of try make deal
                    try {
                        tradeService.changeWithAttemps(FinType.AER, cmpBuyer, cmpSeller,
                            1, new BigDecimal(-4));
                    } catch (Exception e) {
                        statisticService.addRolbacks();
                        logger.error("trads 3 " + e.getMessage() + e.getClass().getName()
                            + e.getMessage() + e.getClass().getName());
                    }
                }
                statisticService.addFnishedDeals(countDeals);
            }
        };

        Thread r4 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < countDeals; i++) { // count of try make deal
                    try {
                        tradeService.changeWithAttemps(FinType.AER, cmpBuyer, cmpSeller,
                            1, new BigDecimal(4));
                    } catch (Exception e) {
                        statisticService.addRolbacks();
                        logger.error("trads 4 " + e.getMessage() + e.getClass().getName()
                            + e.getMessage() + e.getClass().getName());
                    }
                }
                statisticService.addFnishedDeals(countDeals);
            }
        };

        logger.info(" Trading started................. ");
        statisticService.startTest();// start statistic service
        r2.start();
        r1.start();
        r3.start();
        r4.start();

    }

}