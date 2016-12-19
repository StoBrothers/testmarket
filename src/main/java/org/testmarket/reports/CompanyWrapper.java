package org.testmarket.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testmarket.domain.FinType;

/**
 * 
 * CompanyWrapper for reporting
 *  
 * @author Sergey Stotskiy
 *
 */
public class CompanyWrapper {

    private String id;

    private BigDecimal balance;

    private BigDecimal equity;

    private final static String _N = "\n";
    private final static String _T = "\t";

    private Map<FinType, Long> finInstruments = new HashMap<FinType, Long>();

    private List<AccountWrapper> accounts = new ArrayList<>(10);

    public CompanyWrapper(String id) {
        this.id = id;
    }

    /** Get id
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * Get balance 
     * @return
     */
    public BigDecimal getBalance() {
        return balance;
    }
    /**
     * Set balance
     * @param balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    /**
     * Get equity
     * @return
     */
    public BigDecimal getEquity() {
        return equity;
    }
    /**
     * Set equity
     * @param equity
     */
    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }
    /**
     * Get accounts
     * @return
     */
    public List<AccountWrapper> getAccounts() {
        return accounts;
    }
    
    /**
     * Add accountWraper
     * @param account
     */
    public void addAccount(AccountWrapper account) {
        this.accounts.add(account);
    }

    /**
     * Get finInstruments
     * @return the finInstruments
     */
    public Map<FinType, Long> getFinInstruments() {
        return finInstruments;
    }
    /**
     * Add finInstrument with recalculate count value = (old count + new count)
     * @param type 
     * @param count add count
     */
    public void addFinInstrument(FinType type, long count) {
        Long value = finInstruments.getOrDefault(type, new Long(0));
        finInstruments.put(type, value + count);
    }

    /**
     * toString with formatting
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Company : "); sb.append(this.id);sb.append(_N);sb.append(_T);
        sb.append("Balance : ");sb.append(this.balance);sb.append(_N);sb.append(_T);
        sb.append("Equity : ");sb.append(this.equity);sb.append(_N);sb.append(_T);
        sb.append("Positions : ");
        for (Map.Entry<FinType, Long> position : finInstruments.entrySet()) {
            sb.append(position.getKey());sb.append(":");sb.append(position.getValue());sb.append("; ");
        }

        sb.append(_N);
        for (AccountWrapper aw : getAccounts()) {
            sb.append(aw.toString());
        }
        return sb.toString();
    }

}
