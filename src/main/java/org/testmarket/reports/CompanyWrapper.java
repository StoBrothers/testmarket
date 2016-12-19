package org.testmarket.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.testmarket.domain.Account;
import org.testmarket.domain.FinType;

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
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public List<AccountWrapper> getAccounts() {
        return accounts;
    }

    public void addAccount(AccountWrapper account) {
        this.accounts.add(account);
    }
    
    
    
    /**
     * @return the finInstruments
     */
    public Map<FinType, Long> getFinInstruments() {
        return finInstruments;
    }
   
    
    public void addFinInstrument(FinType type, long count) {
        Long value =finInstruments.getOrDefault(type, new Long(0));
        finInstruments.put(type, value + count);
    }

   public String toString () {
       StringBuilder sb = new StringBuilder();
       sb.append("Company : "); sb.append(this.id); sb.append(_N);
       sb.append(_T);sb.append("Balance : "); sb.append(this.balance);sb.append(_N);
       sb.append(_T);sb.append("Equity : "); sb.append(this.equity);sb.append(_N);
       sb.append(_T);sb.append("Positions : ");

       for (Map.Entry<FinType, Long> position  : finInstruments.entrySet()) {
            sb.append(position.getKey()); sb.append(":"); sb.append(position.getValue()); sb.append("; ");  
       }
       
       sb.append(_N);
       for (AccountWrapper aw : getAccounts()){
           sb.append(aw.toString());
       }
       return sb.toString();
   }

}
