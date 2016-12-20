package org.testmarket.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Company entity.
 *
 * @author Sergey Stotskiy
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "company")
public class Company implements Serializable {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private String id; /* [\w]{5,10} */

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Account> accounts;

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
     * Get accounts
     * 
     * @return
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Set accounts
     * 
     * @param accounts
     */
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Company [companyID=" + id + ", Balance=" + accounts.size() + "]";
    }

}
