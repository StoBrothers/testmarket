package org.testmarket.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.testmarket.domain.User;

/**
 * Wrapper for authenticated User.
 * 
 * @author Sergey Stotskiy
 */
@SuppressWarnings("serial")
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true,
            AuthorityUtils.createAuthorityList("ADMIN", "USER"));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    /**
     * Get logon name.
     * 
     * @return
     * @see User#getLogonName()
     */
    public String getLogonName() {
        return user.getEmail();
    }

}
