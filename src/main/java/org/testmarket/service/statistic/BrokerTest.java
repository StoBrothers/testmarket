package org.testmarket.service.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.service.TradeService;

/**
 * Service to start Brokers tests
 * 
 * @author Sergey Stotskiy
 *
 */
@Service("brokerTests")
public class BrokerTest {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TradeService tradeService;

    @Autowired
    StatisticService statisticService;

    public void brokersTest1() {

        Broker broker1 = new Broker("Broker1");
        broker1.setCompanies(companyRepository.findAll());
        broker1.setTradeService(tradeService);
        broker1.setCountDeals(10000);
        broker1.setStatisticService(statisticService);
        broker1.setPriority(Thread.NORM_PRIORITY);

        Broker broker2 = new Broker("Broker2");
        broker2.setCompanies(companyRepository.findAll());
        broker2.setTradeService(tradeService);
        broker2.setCountDeals(10000);
        broker2.setStatisticService(statisticService);
        broker2.setPriority(Thread.NORM_PRIORITY);

        Broker broker3 = new Broker("Broker3");
        broker3.setCompanies(companyRepository.findAll());
        broker3.setTradeService(tradeService);
        broker3.setCountDeals(10000);
        broker3.setStatisticService(statisticService);
        broker3.setPriority(Thread.NORM_PRIORITY);

        Broker broker4 = new Broker("Broker4");
        broker4.setCompanies(companyRepository.findAll());
        broker4.setTradeService(tradeService);
        broker4.setCountDeals(10000);
        broker4.setStatisticService(statisticService);
        broker4.setPriority(Thread.NORM_PRIORITY);

        statisticService.startTest();
        broker1.start();
        broker2.start();
        broker3.start();
        broker4.start();
    }

    public void brokersTest2() {
        statisticService.startTest();
    }

}
