package org.testmarket.service;

import java.math.BigDecimal;

import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;

/**
 * Service for trades
 *
 * @author Sergey Stotskiy
 */
public interface TradeService {
    
    /**
     * General method for selling process 
     *    
     * @param type
     * @param seller company seller
     * @param buyer company buyer 
     * @param count count finInstruments
     * @param delta delta of price can be negative or positive 
     * 
     * @return Count of sold fin instruments
     */
    long change(FinType type, Company seller, Company buyer, long count, BigDecimal delta);


    /**
     * Change with unlimited attempts to execute change method and calculation average value
     * 
     * @param type
     * @param seller company seller
     * @param buyer company buyer 
     * @param count count finInstruments
     * @param delta delta of price can be negative or positive 
     * 
     * @return Count of sold fin instruments
     */
    long changeWithAttemps(FinType type, Company seller, Company buyer, long count, BigDecimal delta);

    
}
