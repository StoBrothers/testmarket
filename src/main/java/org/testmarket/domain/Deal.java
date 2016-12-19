package org.testmarket.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@SuppressWarnings("serial")
@Entity
public class Deal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false, updatable = false)
    private long id;
    /**
     * Financial type 
     */
    private FinType type;
    /**
     * Price of one unit
     */
    private BigDecimal price;
    /**
     * Amount price
     */
    private BigDecimal amount;
    /**
     * Count of units
     */
    private long count;
    /**
     * Average price of unit
     */
    private BigDecimal averagePrice;
    
    @ManyToOne
    private Account buyer;
    
    @ManyToOne
    private Account seller;

    public Deal() {
        
    }
    
    public Deal(FinType type, BigDecimal price, BigDecimal amount, long count, Account buyer, Account seller, BigDecimal averagePrice){
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.count = count;
        this.buyer = buyer;
        this.seller = seller;
        this.averagePrice = averagePrice; 
    }
    
    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * @return the type
     */
    public FinType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(FinType type) {
        this.type = type;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the averagePrice
     */
    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    /**
     * @param averagePrice the averagePrice to set
     */
    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    /**
     * @return the buyer
     */
    public Account getBuyer() {
        return buyer;
    }

    /**
     * @param buyer the buyer to set
     */
    public void setBuyer(Account buyer) {
        this.buyer = buyer;
    }

    /**
     * @return the seller
     */
    public Account getSeller() {
        return seller;
    }

    /**
     * @param seller the seller to set
     */
    public void setSeller(Account seller) {
        this.seller = seller;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Deal [id=" + id + ", type=" + type + ", price=" + price + ", amount="
            + amount + ", count=" + count + ", averagePrice=" + averagePrice + ", buyer="
            + buyer.getId() + ", seller=" + seller.getId() + "]";
    }


    

}
