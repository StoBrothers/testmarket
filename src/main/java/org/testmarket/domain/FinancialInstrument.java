package org.testmarket.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * FinancialInstrument entity.
 * 
 * @author Sergey Stotskiy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "fininstrument")
public class FinancialInstrument implements Serializable {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id;

    private long count = 100;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, updatable = false)
    private FinType type;

    private String companyId;

    
    /**
     * Get id
     * 
     * @return the symbolID
     */
    public String getId() {
        return id;
    }

    /**
     * Set id
     * @param symbolID the symbolID to set
     */
    public void setId(String symbolID) {
        this.id = symbolID;
    }


    /**
     * Get companyId
     * @return
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Set companyId
     * @param company_id the company_id to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * Get type
     * @return the type
     */
    public FinType getType() {
        return type;
    }

    /**
     * Set type
     * @param type the type to set
     */
    public void setType(FinType type) {
        this.type = type;
    }

    /**
     * Get count
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * Set count
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * Get account
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Set account
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[symbolID=" + id + ", count=" + count + ", type=" + type.name()
            + ", account=" + account.getId() + " companyId: " + companyId + "]";
    }

}
