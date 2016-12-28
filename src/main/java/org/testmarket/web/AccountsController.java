package org.testmarket.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.testmarket.domain.Account;
import org.testmarket.domain.AccountRepository;
import org.testmarket.reports.ReportService;

/**
 * Show accounts.
 * 
 * @author Sergey Stotskiy
 *
 */
@RestController
public class AccountsController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ReportService reportService;

    
    @RequestMapping(path = "/rest/accounts/{id}", method = RequestMethod.GET)
    public String findAccounts(@PathVariable("id") String id) {
        List<Account> accounts = null;
        if ((id == null) || (id.length() == 0)) {
            accounts = accountRepository.findAll();
        } else {
        accounts = accountRepository.findByStartAccountID(id+"%");
        }
        
        if ((accounts == null) || (accounts.size() == 0)) {
            return  "Not results"; 
        }

        return reportService.doReportAccounts(accounts);
    }
    
    
    @RequestMapping(path = "/rest/accounts/", method = RequestMethod.GET)
    public String findAccounts() {

        Sort sort = new Sort("id");
        List<Account> accounts = accountRepository.findAll(sort);
        
        if ((accounts == null) || (accounts.size() == 0)) {
            return  "Not results"; 
        }

        return reportService.doReportAccounts(accounts);
    }


}

