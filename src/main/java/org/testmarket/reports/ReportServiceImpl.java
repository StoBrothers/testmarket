package org.testmarket.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testmarket.domain.Account;
import org.testmarket.domain.Company;
import org.testmarket.domain.CompanyRepository;
import org.testmarket.domain.FinancialInstrument;
import org.testmarket.service.DealService;

/**
 * Report service for prepare reports.
 *
 * @author Sergey Stotskiy
 *
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    CompanyRepository companyRepository;

    AtomicInteger version = new AtomicInteger(0);

    @Autowired
    DealService dealService;

    @Override
    public String doReportFile() throws IOException {

        // file will be created in folder where was started application
        File file = new File("result_" + version.getAndIncrement() + ".txt");

        try {
            // overwrite old file if it existed
            file.createNewFile();

            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write(doReport());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error(" File could't to create: " + e.getMessage());
            throw e;
        }
        return file.getAbsolutePath();

    }

    /**
     * Do report
     */
    @Override
    public String doReport() {
        List<CompanyWrapper> companyList = calculationAndPrepareReportData();
        StringBuilder sb = new StringBuilder();
        for (CompanyWrapper companyReport : companyList) {
            sb.append(companyReport.toString());
        }
        return sb.toString();
    }

    private List<CompanyWrapper> calculationAndPrepareReportData() {

        List<Company> companyList = companyRepository.findAll();

        List<CompanyWrapper> companyWrapperList = new ArrayList<>(companyList.size());

        for (Company company : companyList) {

            CompanyWrapper companyReport = new CompanyWrapper(company.getId());

            BigDecimal compEquity = new BigDecimal(0);
            BigDecimal compBalance = new BigDecimal(0);
            List<Account> accounts = company.getAccounts();

            for (Account account : accounts) {
                AccountWrapper accountReport = new AccountWrapper(account);
                List<FinancialInstrument> finInstruments = account.getFinInstruments();
                BigDecimal equity = new BigDecimal(0);
                for (FinancialInstrument instrumet : finInstruments) {
                    accountReport.addFinInstrument(instrumet.getType(),
                        instrumet.getCount());
                    companyReport.addFinInstrument(instrumet.getType(),
                        instrumet.getCount());

                    BigDecimal finCount = BigDecimal.valueOf(instrumet.getCount());
                    BigDecimal avgValue = dealService
                        .getAveragePriceFinInstrument(instrumet.getType());
                    finCount = finCount.multiply(avgValue);
                    equity = equity.add(finCount); // add cost of current fin instrument
                }

                accountReport.setEquity(equity); // set account level equity

                compEquity = compEquity.add(equity);// update company level equity
                compBalance = compBalance.add(account.getBalance()); // update company level balance
                companyReport.addAccount(accountReport);
            }

            companyReport.setBalance(compBalance);
            companyReport.setEquity(compEquity);
            companyWrapperList.add(companyReport);
        }

        return companyWrapperList;
    }

    /**
     * Do report for accounts
     *
     */
    @Override
    public String doReportAccounts(List<Account> accounts) {
        List<AccountWrapper> accountList = calculationAccounts(accounts);
        StringBuilder sb = new StringBuilder();
        for (AccountWrapper accountReport : accountList) {
            sb.append(accountReport.toString());
        }
        return sb.toString();
    }

    /**
     * Calculate accounts information for reporting
     * 
     * @param accounts
     *            accounts for reporting
     * @return AccountWrapper list
     */
    private List<AccountWrapper> calculationAccounts(List<Account> accounts) {
        // Snapshot of prices of finInstruments
        List<AccountWrapper> accountsWrapperList = new ArrayList<>(accounts.size());

        for (Account account : accounts) {
            AccountWrapper accountReport = new AccountWrapper(account);
            List<FinancialInstrument> finInstruments = account.getFinInstruments();
            BigDecimal equity = new BigDecimal(0);
            for (FinancialInstrument instrumet : finInstruments) {
                accountReport.addFinInstrument(instrumet.getType(), instrumet.getCount());

                BigDecimal finCount = BigDecimal.valueOf(instrumet.getCount());
                BigDecimal avgValue = dealService
                    .getAveragePriceFinInstrument(instrumet.getType());
                finCount = finCount.multiply(avgValue);
                equity = equity.add(finCount);
            }

            accountReport.setEquity(equity);
            accountsWrapperList.add(accountReport);
        }

        return accountsWrapperList;
    }

}
