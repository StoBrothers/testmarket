package org.testmarket.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testmarket.domain.Account;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;
import org.testmarket.service.DealService;


@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    CompanyRepository companyRepository;

    AtomicInteger version = new AtomicInteger(0);

    @Autowired
    DealService dealService;

    
    public String doReportFile() {

        File file = new File ("result_" + version.getAndIncrement() + ".txt");

        try {

        file.createNewFile();
        
        Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(doReport());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
     return file.getAbsolutePath();        
         
    }
    
    public String doReport() {
        List<CompanyWrapper> companyList = calculationAndPrepareReportData();
        StringBuilder sb = new StringBuilder();
        for (CompanyWrapper companyReport : companyList) {
            sb.append(companyReport.toString());
        }
        return sb.toString();
    }

    
    
    private List<CompanyWrapper> calculationAndPrepareReportData() {
        // Snapshot of prices of finInstruments 
        Map<FinType, BigDecimal> avgPrices = new HashMap<>();
        
        for (FinType type : FinType.values()) {
            avgPrices.put(type, dealService.getAveragePriceFinInstrument(type));
        }
        
        List<Company> companyList = companyRepository.findAll();
        
        List<CompanyWrapper> companyWrapperList = new ArrayList<>(companyList.size());
        
        for (Company company : companyList) {
            
            CompanyWrapper companyReport = new CompanyWrapper(company.getId());

            BigDecimal compEquity = new BigDecimal(0);
            BigDecimal compBalance = new BigDecimal(0); 
            List<Account> accounts =  company.getAccounts();
            
            for (Account account : accounts) {
                AccountWrapper accountReport = new AccountWrapper(account);
                List <FinancialInstrument> finInstruments = account.getFinInstruments();
                BigDecimal equity = new BigDecimal(0); 
                for (FinancialInstrument instrumet : finInstruments) {
                    accountReport.addFinInstrument(instrumet.getType(), instrumet.getCount());
                    companyReport.addFinInstrument(instrumet.getType(), instrumet.getCount());
                    
                    BigDecimal finCount =  BigDecimal.valueOf(instrumet.getCount());
                    BigDecimal avgValue =  avgPrices.get(instrumet.getType());
                    finCount = finCount.multiply(avgValue);
                    equity = equity.add(finCount);
                }
                
                accountReport.setEquity(equity);
                
                compEquity = compEquity.add(equity);
                compBalance = compBalance.add(account.getBalance());
                companyReport.addAccount(accountReport);
            }
            
            companyReport.setBalance(compBalance);
            companyReport.setEquity(compEquity);
            companyWrapperList.add(companyReport);
        }
        
        return companyWrapperList;
    }
    
    public String doAccounts(List<Account> accounts) {
        List<AccountWrapper> accountList = calculationAccounts(accounts);
        StringBuilder sb = new StringBuilder();
        for (AccountWrapper accountReport : accountList) {
            sb.append(accountReport.toString());
        }
        return sb.toString();
    }

    
    private List<AccountWrapper> calculationAccounts(List<Account> accounts) {
        // Snapshot of prices of finInstruments 
        Map<FinType, BigDecimal> avgPrices = new HashMap<>();
        
        for (FinType type : FinType.values()) {
            avgPrices.put(type, dealService.getAveragePriceFinInstrument(type));
        }
        
        List<AccountWrapper> accountsWrapperList = new ArrayList<>(accounts.size());
            
            for (Account account : accounts) {
                AccountWrapper accountReport = new AccountWrapper(account);
                List <FinancialInstrument> finInstruments = account.getFinInstruments();
                BigDecimal equity = new BigDecimal(0); 
                for (FinancialInstrument instrumet : finInstruments) {
                    accountReport.addFinInstrument(instrumet.getType(), instrumet.getCount());
                    
                    BigDecimal finCount =  BigDecimal.valueOf(instrumet.getCount());
                    BigDecimal avgValue =  avgPrices.get(instrumet.getType());
                    finCount = finCount.multiply(avgValue);
                    equity = equity.add(finCount);
                }
                
                accountReport.setEquity(equity);
                accountsWrapperList.add(accountReport);
            }
            
        
        return accountsWrapperList;
    }

    
    
}
