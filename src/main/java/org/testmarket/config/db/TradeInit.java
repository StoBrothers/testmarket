package org.testmarket.config.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

/**
 * Create Companies with accounts and fin instruments
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


    @Override
    protected void init() {

        List<Company> companys = companyRepository.findAll();

        Company cmpBuyer = companys.get(1);

        Company cmpSeller = companys.get(0);

        logger.info(" START " + cmpSeller.getId());

        Map<String, Object> map = em.getProperties();
        for (String current : map.keySet()) {

            logger.info(current + "=" + map.get(current));

        }

        Thread r1 = new Thread() {
            @Override
            public void run() {
                int i = 100;
                while (i > 0) {
                    i--;

                    try {
                        long resultCount = tradeService.change(FinType.AER, cmpSeller, cmpBuyer, 1, new BigDecimal(-1));

                    } catch (Exception e) {
                        logger
                            .error("Error r1 " + e.getClass().getName() + "thread sleep");
                        try {
                            Thread.sleep(100);
                            logger.error("Thread aaa wake up");
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    }
                }

            }
        };

        Thread r2 = new Thread() {
            @Override
            public void run() {
                int i = 100;
                while (i > 0) {
                    i--;

                    try {
                        long resultCount = tradeService.change(FinType.AER, cmpBuyer,
                            cmpSeller, 1, new BigDecimal(-3));
                    } catch (Exception e) {
                        logger.error("Erroror bbb " + e.getClass().getName() + " sleep");

                        try {
                            Thread.sleep(100);
                            logger.error("Thread r2 wake up");

                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        };

        Thread r3 = new Thread() {
            @Override
            public void run() {
                int i = 100;
                while (i > 0) {
                    i--;

                    try {
                        long resultCount = tradeService.change(FinType.AER, cmpBuyer,
                            cmpSeller, 1, new BigDecimal(1));
                    } catch (Exception e) {
                        logger.error("Error r3 " + e.getClass().getName());

                        try {
                            Thread.sleep(100);
                            logger.error("Thread ccc wake up");
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        };

        Thread r4 = new Thread() {
            @Override
            public void run() {
                int i = 100;
                while (i > 0) {
                    i--;

                    try {
                        long resultCount = tradeService.change(FinType.AER, cmpBuyer,
                            cmpSeller, 1, new BigDecimal(3));
                    } catch (Exception e) {
                        logger.error("Error ddd " + e.getClass().getName() + " sleep");
                        try {
                            Thread.sleep(100);
                            logger.error("Thread ddd wake up");
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }
        };

        logger.info(" Solding started................. ");
        r2.start();
        r1.start();
        r3.start();
        r4.start();
    }


}