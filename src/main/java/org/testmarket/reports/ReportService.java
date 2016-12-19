package org.testmarket.reports;

import java.util.List;

import org.testmarket.domain.Account;

public interface ReportService {

    String doReport();
    
    String doReportFile(); 
    
    String doAccounts(List<Account> accounts);
}
