package org.testmarket.reports;

import java.io.IOException;
import java.util.List;

import org.testmarket.domain.Account;
/**
 * Report service for prepare reports.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface ReportService {
    /**
     * Prepare company report 
     * @return name of saved file 
     */
    String doReport();
    /**
     * Prepare company report and save it to file 
     * @return formatted string
     */
    String doReportFile() throws IOException; 

    /**
     * Get accounts report  
     * @param accounts
     * @return formatted string
     */
    String doReportAccounts(List<Account> accounts);
}
