package org.testmarket.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * It is tokens storage where stored all tokens.
 *
 * @author Sergey Stotskiy
 */
@SuppressWarnings("serial")
@Entity
public class WSToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(nullable = false)
    private String principalName;

    public WSToken(String token, Date expirationDate, User user, String principalName) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
        this.active = true;
        this.principalName = principalName;
    }

    public WSToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public String toString() {
        return "WSToken [id=" + id + ", user=" + user.getFullName() + " principalName="
            + getPrincipalName() + ", token=" + token + ", active=" + active
            + ", expirationDate=" + expirationDate + "]";
    }
}
