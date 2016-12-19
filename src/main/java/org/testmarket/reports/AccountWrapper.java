package org.testmarket.reports;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.testmarket.domain.Account;
import org.testmarket.domain.FinType;

public class AccountWrapper {
    
    private final static String _N = "\n";
    
    private final static String _T = "\t";

    
    private Account account;
    
    private BigDecimal equity = null;

    /**
     * @return the equity
     */
    public BigDecimal getEquity() {
        return equity;
    }

    /**
     * @param equity the equity to set
     */
    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }


    private Map<FinType, Long> finInstruments = new HashMap<FinType, Long>();
  
    public AccountWrapper (Account account) {
        this.account = account;
    }
  
    /**
     * @return the finInstruments
     */
    public Map<FinType, Long> getFinInstruments() {
        return finInstruments;
    }
   
    
    public void addFinInstrument(FinType type, long count) {
        finInstruments.put(type, count);
    }
    
    
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Account : "); sb.append(account.getId()); sb.append(_N);
        sb.append(_T);sb.append("Balance : "); sb.append(account.getBalance());sb.append(_N);
        sb.append(_T);sb.append("Equity : "); sb.append(this.equity);sb.append(_N);
        sb.append(_T);sb.append("Positions : ");

        for (Map.Entry<FinType, Long> position  : finInstruments.entrySet()) {
             sb.append(position.getKey()); sb.append(":"); sb.append(position.getValue()); sb.append("; ");  
        }
        sb.append(_N);
        return sb.toString();
    }

    
}
