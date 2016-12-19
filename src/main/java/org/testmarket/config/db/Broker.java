package org.testmarket.config.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;
import org.testmarket.service.TradeService;

public class Broker extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Broker.class);

    TradeService tradeService;

    Random random = new Random();

    private volatile List<Company> companies = null;


    
    
    /**
     * @param tradeService the tradeService to set
     */
    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * @param companies
     *            the companies to set
     */
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public Broker(String name) {
        super.setName(name);
        super.setPriority(MIN_PRIORITY);
    }

    @Override
    public void run() {

        int numberCmpSeller = 0;
        int numberCmpBuyer = 0;
        int typeSize = FinType.values().length;
        int compSize = companies.size();

        while (true) {

            numberCmpSeller = random.nextInt(compSize);
            numberCmpBuyer = random.nextInt(compSize);

            while (numberCmpBuyer == numberCmpSeller) { // getting differently companies
                numberCmpBuyer = random.nextInt(compSize);
            }

            Company cmpSeller = companies.get(numberCmpSeller);
            Company cmpBuyer = companies.get(numberCmpBuyer);

            int typeNumber = random.nextInt(typeSize);

            FinType type = FinType.values()[typeNumber];

            Double value = random.nextDouble();
            int highPart = random.nextInt(4);
            int signPart = random.nextInt(2);
            

            int countFin = random.nextInt(6);
            countFin++; // except 0 

            BigDecimal delta = BigDecimal.valueOf(value)
                .add(BigDecimal.valueOf(highPart));

            if (signPart == 1) {// define signum of value
                delta = delta.negate();
            }

//            logger.info("Seller: " + cmpSeller.getId() + " Buyer: "
//                + cmpBuyer.getId() + " type: " + type.name() + " delta: " + delta
//                + " count : " + countFin);

            try {
                long resultCount = tradeService.change(type, cmpSeller, cmpBuyer, countFin, delta);
            } catch (Exception e) {
                logger.error("Deal failed with error: " + this.getName() + " : " + e.getMessage() + " thread sleep");
                try {
                    Thread.sleep(100);
                    logger.error("Thread " + this.getName() + " wake up");
                } catch (InterruptedException e1) {
                    logger.error("Thread " + this.getName() + " waked up with exception: "
                        + e1.toString());
                }

            }
        }

    }

}
