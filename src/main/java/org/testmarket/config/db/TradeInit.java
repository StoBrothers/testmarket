package org.testmarket.config.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Account;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.Deal;
import org.testmarket.domain.DealRepository;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;
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

//    @Autowired(required=true)
//    private SessionFactory sessionFactory;

//    @Autowired(required=true)
//    ServiceRegistry registry;

    @Override
    protected void init() {
        List<Company> companys = companyRepository.findAll();

        Company cmpBuyer = companys.get(1);

        Company cmpSeller = companys.get(0);

        logger.info(" START " + cmpSeller.getId());
        
//        int oi = 2; 
//
//        while(oi > 0) {
//        oi--;
//
//        Map<String,Object> timeoutProperties = new HashMap<String,Object>();
//        timeoutProperties.put("javax.persistence.lock.timeout", 0);
//
////      Account ab = em.find(Account.class, 1,
////      LockModeType.PESSIMISTIC_WRITE, timeoutProperties);
////      logger.info( "lock mode: " + em.getLockMode(ab));
//
//        }
//
        
        
        
        Map<String, Object> map =  em.getProperties();
        for (String current : map.keySet()) {
            
           logger.info(current + "=" + map.get(current));
            
        }

        
        
        
        
        Thread r1 = new Thread(){
            public void run(){
                int i= 100;
                while(i > 0) {
                    i--;


                try{
                long resultCount = tradeService.change(FinType.AER, cmpSeller, cmpBuyer, 1, new BigDecimal(-1));
//                logger.info(" Bought " + resultCount);

                    }catch(Exception e) {
                        logger.error("Error aaa " + e.getClass().getName() + "thread sleep");
                       try {
                           Thread.sleep(100);
                           logger.error("Thread aaa wake up");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                       
                        
                    }
                }
                
                try{
                    Thread.sleep(1000);
                    logger.error("ВВВВВВВВВВВВ");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                    System.out.println(report.doReportFile());
            }
        };
        
        Thread r2 = new Thread(){
            public void run(){
                int i= 100;
                while(i > 0) {
                    i--;
                    
//                    Account ab = em.find(Account.class, 1,
//                        LockModeType.PESSIMISTIC_WRITE, timeoutProperties);
//                    logger.info( "lock mode: " + em.getLockMode(ab));
//                    logger.info(" Sold " + ab.getId());

                try {
                long resultCount = tradeService.change(FinType.AER, cmpBuyer, cmpSeller, 1, new BigDecimal(-3));
//                logger.info(" Sold " + resultCount);
                }catch(Exception e) {
                    logger.error("Erroror bbb " + e.getClass().getName() + " sleep");
                    
                    try {
                        Thread.sleep(100);
                        logger.error("Thread bbb wake up");

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

//                    e.printStackTrace();
//                    System.exit(0);

                }
                }
            }
        };

        
        Thread r3 = new Thread(){
            public void run(){
                int i= 100;
                while(i > 0) {
                    i--;

                    try {
                        long resultCount = tradeService.change(FinType.AER, cmpBuyer, cmpSeller, 1, new BigDecimal(1));
//                        logger.info(" Sold " + resultCount);
                        }catch(Exception e) {
                            logger.error("Erroror ccc " + e.getClass().getName());
//                            e.printStackTrace();
//                            System.exit(0);

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
        
        Thread r4 = new Thread(){
            public void run(){
                int i= 100;
                while(i > 0) {
                    i--;
 
                    try {
                        long resultCount = tradeService.change(FinType.AER, cmpBuyer, cmpSeller, 1, new BigDecimal(3));
//                        logger.info(" Sold " + resultCount);
                        }catch(Exception e) {
                            logger.error("Error ddd " + e.getClass().getName() + " sleep" );
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

        
       logger.info(" solding...... " );
//       r2.start(); 
//       r1.start();
       r3.start(); 
       r4.start();
    }

    @Autowired
    TradeService tradeService; 
    
    //Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init2() {

        // calculationBalance and Equity
        // 0 create deals
        // 1 lock account
        // 2 get prices info
        // calculate account equity and update acc
        // send message to company
        //

        List<Company> companys = companyRepository.findAll();

        Company cmpBuyer = companys.get(1);

        Company cmpSeller = companys.get(0);

        logger.info(" START " + cmpSeller.getId());
        
///#####        tradeService.change();
        
//
        Query q = em.createNamedQuery(FinancialInstrument.NQUERY_getForUpdate)
            .setParameter(FinancialInstrument.PARAM_companyId, cmpSeller.getId())
            .setParameter(FinancialInstrument.PARAM_type, FinType.AER.ordinal())
            .setLockMode(LockModeType.PESSIMISTIC_WRITE);
        
        
        FinancialInstrument fin = (FinancialInstrument) q.getSingleResult();
////
//        if (fin != null) {
//            fin.setCount((fin.getCount()+2));
//            logger.info("--->" + fin.toString());
//            return;
//        } else {
//        logger.info(" FINAL " );
//       // tr.rollback();
//        }
////        
        

//         EntityTransaction tr = em.getTransaction();
//         tr.begin();
         
//         FinancialInstrument  fin = change2(cmpSeller);
//         
//         if (fin != null) {
//         fin.setCount((fin.getCount()+2));
//         logger.info(fin.toString());
////         tr.commit();
//         } else {
//         logger.info(" FINAL " );
////         tr.rollback();
//         return;
//         }
//
        logger.info(cmpBuyer.toString());
        logger.info(cmpSeller.toString());

        List<Account> accsBuyer = cmpBuyer.getAccounts();

        List<Account> accsSeller = cmpSeller.getAccounts();

        Account accSeller = accsSeller.get(0);

        Account accBuyer = accsBuyer.get(9);

        // change(accBuyer, accSeller, FinType.AER, new BigDecimal(12), 10);
        // value

        List<Deal> deals = dealRepository.findAll();
        for (Deal current : deals) {
            logger.info(current.toString());
        }

        BigDecimal value = dealService.getAveragePriceFinInstrument(FinType.AER);
        if (value.compareTo(new BigDecimal(10)) != 0) {
            logger.error(
                "Right value is (10 * 10)/ 10 = 10.0 but we have: " + value.toString());
            System.exit(1);
        }
        ;

        logger.info("Right average value is 10.0 for FinType.AER");

    }


    /**
     * @param cmpSeller
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private FinancialInstrument change2(Company cmpSeller) {
        FinancialInstrument fin = finRepository.findOneForUpdateByCompanyIdAndType(FinType.AER,
         cmpSeller.getId());
        return fin;
    }

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public boolean change(Account buyer, Account sell, FinType type, BigDecimal price,
        long count) {

        SessionFactory sessionFactory = new Configuration().configure()
            .buildSessionFactory();

        
        // em.get .getTransaction().begin();

//        logger.info(sessionFactory.getCurrentSession().getCacheMode().name());
//
//        logger
//            .info(" -> " + sessionFactory.getCurrentSession().getStatistics().hashCode());

        HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);

        Account sellerLock = hibernateTemplate.get(Account.class, sell.getId(),
            LockMode.UPGRADE_NOWAIT);

        Account buyerLock = hibernateTemplate.get(Account.class, buyer.getId(),
            LockMode.UPGRADE_NOWAIT);

        sellerLock.setEquity(new BigDecimal(100));

//        sessionFactory.getCurrentSession().getCurrentLockMode(buyerLock);
//        Session newSession = sessionFactory.openSession();
//
//        Account testAccount = (Account) newSession.get(Account.class, sell.getId());// ,
//                                                                                    // LockOptions.NO_WAIT);

//        logger.info(testAccount.getEquity().toString());

        return true;
    }

}