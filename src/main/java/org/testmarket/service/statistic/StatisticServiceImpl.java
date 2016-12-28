package org.testmarket.service.statistic;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.testmarket.domain.AccountRepository;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrumentRepository;
import org.testmarket.reports.ReportService;

/**
 *
 * Service to get statistic of tests.
 *
 * @author Sergey Stotskiy
 *
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    private static final Logger logger = LoggerFactory
        .getLogger(StatisticServiceImpl.class);
    /**
     * Start stage
     */
    private StatisticStages currentStage;// = StatisticStages.CHECK_PESSIMISTICK_LOCK_APPROACH;

    /**
     * Count of executed threads in finished stage
     */
    private AtomicLong finishedThreads = new AtomicLong(0);

    /**
     * Count of finished deals in finished stage
     */
    private AtomicLong finishedDeals = new AtomicLong(0);
    /**
     * Count of rollbacks in finished stage
     */
    private AtomicLong countStageRollbacks = new AtomicLong(0);
    /**
     * Start time of stage
     */
    private long startTime;
    /**
     * End time of stage
     */
    private long endTime;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    FinancialInstrumentRepository finRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BrokerTest brokerTest;

    
    @Override
    /**
     * Set start time
     */
    public synchronized void startTest() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public synchronized void addFnishedDeals(long count) {
        this.endTime = System.currentTimeMillis();
        this.finishedThreads.incrementAndGet();
        this.finishedDeals.addAndGet(count);
    }

    @Override
    public synchronized void addRolbacks() {
        this.countStageRollbacks.addAndGet(1);
    }

    @PostConstruct
    private void init() {
        this.currentStage = StatisticStages.CHECK_PESSIMISTICK_LOCK_APPROACH;
    }

    @Scheduled(fixedDelay = 5000)
    public void checkTime() {

        switch (this.currentStage) {
            case CHECK_PESSIMISTICK_LOCK_APPROACH: {
                pessimistickLockTestResult(); //check results and switch on next stage
                break;
            }
            case CHECK_BROKERS: {
                brokerTest1Result(); //check results and switch on next stage
                break;
            }
            case UNLIMITED_DEALS: {
                break;
            }
            default:
                break;
        }

    }
    /**
     * Check first pessimistick lock test 
     */
    private void pessimistickLockTestResult() {
        if (this.finishedThreads.get() == 4) {// that finished this stage
            try {
                testResultExecuter();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            initState();
            this.currentStage = StatisticStages.CHECK_BROKERS;
            logger.info("Stage started: " + this.currentStage.name());
            brokerTest.brokersTest1();
        }
    }

    /**
     * Reset statistics parameters 
     */
    private void initState() {
        this.finishedThreads.set(0);
        this.countStageRollbacks.set(0);
        this.finishedDeals.set(0);
    }
    
    /**
     * Check Test with broker threads
     */
    private void brokerTest1Result() {
        if (this.finishedThreads.get() == 4) {// that finished this stage
            try {
                testResultExecuter();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
            initState();
            this.currentStage = StatisticStages.UNLIMITED_DEALS;
            logger.info("Stage started: " + this.currentStage.name());
            brokerTest.brokersTest2();
        }
    }

    /**
     * 
     * Check results after execution every stage 
     * @throws Exception
     *
     */
    private void testResultExecuter() throws Exception {

        logger.info("Stage finished: " + this.currentStage.name());

        // calculation time tacked at this stage
        Long time = (this.endTime - this.startTime);
        // count of deals in this stage
        Long countDeals = this.finishedDeals.get();
        // calculation how many time tacked one deal in ms
        double dealsTakedTime = 0;// if deals is 0 call Exception and exit :)
        // calculation how many time tacked one deal in ms
        dealsTakedTime = (time.doubleValue() / countDeals.doubleValue()); 

        if (dealsTakedTime == 0) {
            throw new Exception(" Count finished deals is wrong");
        }
        // get count of rollbacks
        Long countRollbacks = this.countStageRollbacks.get();

        countRollbacks = (countRollbacks * 100);

        double rollbacksPercent = (countRollbacks.doubleValue()/countDeals.doubleValue());// percent of rollbacks to deals
        
        logger.info("Stage tacked time: " + time + " countDeals: " + countDeals + " one deal tacked time(ms) : " + 
        dealsTakedTime + " countRollbacks: " + countRollbacks/100 + " rollbacksPercent: " + rollbacksPercent);

        showCompaniesStatistic();
        
        checkCountFinInstruments();
        
        checkSystemBalnce();

        try {// generate report file
            logger.info("Report file: " + reportService.doReportFile());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Check balance in system. 
     * Amount balance must be 40 000
     * @throws Exception
     */
    private void checkSystemBalnce() throws Exception {
        // calculation balance in system
        BigDecimal allBalance = accountRepository.sumBalanceAll();
        logger.info("Entry balance : " + allBalance);

        BigDecimal allValue = BigDecimal.valueOf(40000); // system balance 

        allValue.setScale(1, RoundingMode.HALF_UP);
        allBalance.setScale(1, RoundingMode.HALF_UP);

        long firstValue = allValue.multiply(BigDecimal.valueOf(10)).longValue();

        long secondValue = allBalance.multiply(BigDecimal.valueOf(10)).longValue();

        if (firstValue != secondValue) { // allBalance.compareTo(allValue) != 0) {
            throw new Exception(" Amount Balance is wrong : " + allBalance + " - " + allValue);
        }
    }

    /**
     * Check count fin instruments.
     * Amount of fin instruments every type is 4 000 
     * @throws Exception
     */
    private void checkCountFinInstruments() throws Exception {
        for (FinType type : FinType.values()) {
            long countType = 0;

            countType = finRepository.sumCountByType(type);

            logger.info("Count " + type + " : " + finRepository.sumCountByType(type));

            if (countType != 4000) {// check count of fin instruments existed  in system (4000)  
                throw new Exception(" Count " + type + " finInstruments is wrong");
            }

        }
    }

    /**
     * Show company fin instruments 
     */
    private void showCompaniesStatistic() {
        List<Company> companyList = companyRepository.findAll();
        for (Company company : companyList) {
            logger.info("Company: " + company.getId() + " PIN:"
                + finRepository.sumCountByCompanyIdAndType(FinType.PIN, company.getId())
                + " IKA:"
                + finRepository.sumCountByCompanyIdAndType(FinType.IKA, company.getId())
                + " SSO: "
                + finRepository.sumCountByCompanyIdAndType(FinType.SSO, company.getId())
                + " EPM: "
                + finRepository.sumCountByCompanyIdAndType(FinType.IKA, company.getId())
                + " RTM: "
                + finRepository.sumCountByCompanyIdAndType(FinType.RTM, company.getId())
                + " EMC: "
                + finRepository.sumCountByCompanyIdAndType(FinType.EMC, company.getId())
                + " KLM: "
                + finRepository.sumCountByCompanyIdAndType(FinType.KLM, company.getId())
                + " SBB: "
                + finRepository.sumCountByCompanyIdAndType(FinType.SBB, company.getId())
                + " AER: "
                + finRepository.sumCountByCompanyIdAndType(FinType.AER, company.getId())
                + " DTB: "
                + finRepository.sumCountByCompanyIdAndType(FinType.DTB, company.getId()));
        }
    }

}
