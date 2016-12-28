package org.testmarket.service.statistic;

/**
 * Statistic service
 * 
 * @author Sergey Stotskiy
 *
 */
public interface StatisticService {

    /**
     * Call when started Test
     */
    void startTest();
    
    /**
     *  Count of finished deals  
     * @param count
     */
    void addFnishedDeals(long count);
    
    /**
     * Add one rollback
     */
    void addRolbacks();
    
}
