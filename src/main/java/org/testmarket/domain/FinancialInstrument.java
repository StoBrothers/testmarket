package org.testmarket.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "fininstrument")
@NamedNativeQuery(name=FinancialInstrument.NQUERY_getForUpdate, 
query = "select fin.* from fininstrument fin  where  company_id =:company_id and type=:type and count > 0 and rownum < 2 for update ", resultClass=FinancialInstrument.class)
public class FinancialInstrument implements Serializable {

    public static final String NQUERY_getForUpdate = "getFinInstrumentForUpdate";
    
    public static final String PARAM_companyId = "company_id";
    
    public static final String PARAM_type = "type";
    
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
    

    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param company_id the company_id to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the type
     */
    public FinType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(FinType type) {
        this.type = type;
    }

    /**
     * @return the symbolID
     */
    public String getId() {
        return id;
    }

    /**
     * @param symbolID
     *            the symbolID to set
     */
    public void setId(String symbolID) {
        this.id = symbolID;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @param account
     *            the account to set
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
            + ", account=" + account.getId() + " companyId: "+ companyId + "]";
    }

}
