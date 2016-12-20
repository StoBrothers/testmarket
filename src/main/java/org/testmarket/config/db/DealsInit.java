package org.testmarket.config.db;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.testmarket.domain.Account;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.Deal;
import org.testmarket.domain.DealRepository;
import org.testmarket.domain.FinType;
import org.testmarket.service.DealService;

/**
 * Create Companies with accounts and fin instruments
 *
 * @author Sergey Stotskiy
 *
 */

@Component("dealsInit")
@DependsOn({ "companyInit" })
public class DealsInit extends AbstractInit {

    private static final Logger logger = LoggerFactory.getLogger(DealsInit.class);

    @Autowired
    DealRepository dealRepository;

    @Autowired
    DealService dealService;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    protected void init() {

        List<Company> companys = companyRepository.findAll();

        Company cmpBuyer = companys.get(1);

        Company cmpSeller = companys.get(0);

        logger.info(cmpBuyer.toString());
        logger.info(cmpSeller.toString());

        List<Account> accsBuyer = cmpBuyer.getAccounts();

        List<Account> accsSeller = cmpSeller.getAccounts();

        Account accSeller = accsSeller.get(0);

        Account accBuyer = accsBuyer.get(9);

        // this values is not participate in calculation average value
        createDeal(accSeller, accBuyer, FinType.AER, 3, new BigDecimal(100));
        // because it 's first values
        createDeal(accSeller, accBuyer, FinType.AER, 3, new BigDecimal(50));

        createDeals(accSeller, accBuyer, 10);// this 10 deals participate in calculation average
                                             // value

        dealService.addDeal(FinType.AER, new BigDecimal(10), new BigDecimal(100), 10,
            accBuyer, accSeller);

        List<Deal> deals = dealRepository.findAll();
        for (Deal current : deals) {
            logger.info("Deal: " +  current.toString());
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
     * @param accSeller
     * @param accBuyer
     */
    private void createDeals(Account accSeller, Account accBuyer, int countDeals) {

        FinType[] types = FinType.values();

        int count = types.length;

        while (count > 0) {
            createDeal(accSeller, accBuyer, types[--count], countDeals,
                new BigDecimal(10));
        }

    }

    private void createDeal(Account accSeller, Account accBuyer, FinType finType,
        int countDeals, BigDecimal price) {
        while (countDeals > 0) {
            Deal deal = new Deal(finType, price, new BigDecimal(100), 10, accBuyer,
                accBuyer, new BigDecimal(10));
            countDeals--;
            dealRepository.save(deal);

        }
    }

}
