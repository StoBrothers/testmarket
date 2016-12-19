package org.testmarket.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "company")
public class Company implements Serializable {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;   /* [\w]{5,10} */

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    private BigDecimal Balance;

    @Transient
    private BigDecimal Equity;

    /**
     * Type of FinancialInstrument, FI count
     */
    // private Map<String, AtomicLong> countOfFinInstruments = new ConcurrentHashMap<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Account> accounts;

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public BigDecimal getEquity() {
        return Equity;
    }

    public void setEquity(BigDecimal equity) {
        Equity = equity;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Company [companyID=" + id + ", Balance=" + Balance
            + ", Equity=" + Equity + ", accounts=" + accounts.size() + "]";
    }

    
}
