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

/**
 * Account entity
 *
 * @author Sergey Stotskiy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "account")
public class Account implements Serializable {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    /**
     * Default value 1000
     */
    private BigDecimal balance = BigDecimal.valueOf(1000);

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<FinancialInstrument> finInstruments;

    /**
     * Get id
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * 
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get balance
     * 
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Set balance
     * 
     * @param balance
     *            the balance to set
     * 
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Add balance
     * 
     * @param balance
     *            the balance to add
     * 
     *
     *            public synchronized void addBalance(BigDecimal balance) { this.balance =
     *            this.balance.add(balance); }
     */

    /**
     * Get company
     * 
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * Set the company
     * 
     * @param company
     *            the company to set
     * 
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Get finInstruments
     * 
     * @return the finInstruments
     */
    public List<FinancialInstrument> getFinInstruments() {
        return finInstruments;
    }

    /**
     * Set finInstruments
     * 
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
        return "Account [accountID=" + id + ", balance=" + balance + ", company="
            + company.getId() + ", finInstruments=" + toString(finInstruments) + "]";
    }

    private String toString(List<FinancialInstrument> array) {
        StringBuilder buider = new StringBuilder();
        for (FinancialInstrument current : array) {
            buider.append(current.toString());
        }
        return buider.toString();
    }

}
