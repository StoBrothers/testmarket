package org.testmarket.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
public class Account implements Serializable {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    private BigDecimal balance = BigDecimal.valueOf(1000);

    @Transient
    private BigDecimal equity = null;

    // /**
    // * Type of FinancialInstrument, FI count
    // */

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<FinancialInstrument> finInstruments;

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance
     *            the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the equity
     */
    public BigDecimal getEquity() {
        return equity;
    }

    /**
     * @param equity
     *            the equity to set
     */
    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company
     *            the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * @return the finInstruments
     */
    public List<FinancialInstrument> getFinInstruments() {
        return finInstruments;
    }

    /**
     * @param finInstruments
     *            the finInstruments to set
     */
    public void setFinInstruments(List<FinancialInstrument> finInstruments) {
        this.finInstruments = finInstruments;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Account [accountID=" + id + ", balance=" + balance + ", equity=" + equity
            + ", company=" + company.getId() + ", finInstruments="
            + toString(finInstruments) + "]";
    }

    // TODO delete to utility or transform over lambda or delete
    public String toString(List<FinancialInstrument> array) {
        StringBuilder buider = new StringBuilder();
        for (FinancialInstrument current : array) {
            buider.append(current.toString());
        }
        return buider.toString();
    }

}
