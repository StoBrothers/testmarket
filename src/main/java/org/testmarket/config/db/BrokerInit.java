package org.testmarket.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.service.TradeService;

/**
 * This module contains unlimited in time simulation of trading. Two brokers has access to all
 * companies and they are making a deals.
 *
 * @author Sergey Stotskiy
 *
 */

@Component("brokerInit")
@DependsOn({ "tradeInit" })
public class BrokerInit extends AbstractInit {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TradeService tradeService;

    @Override
    protected void init() {
        Broker broker1 = new Broker("Broker1");
        broker1.setCompanies(companyRepository.findAll());
        broker1.setDaemon(true);
        broker1.setTradeService(tradeService);
        broker1.start();

        Broker broker2 = new Broker("Broker2");
        broker2.setCompanies(companyRepository.findAll());
        broker2.setDaemon(true);
        broker2.setTradeService(tradeService);
        broker2.start();
    }

}