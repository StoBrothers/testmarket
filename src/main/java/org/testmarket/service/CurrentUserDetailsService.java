package org.testmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.testmarket.domain.User;

/**
 * Authentication user.
 * 
 * @author Sergey Stotskiy
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String logonName)
        throws UsernameNotFoundException {
        User user = userService.getUserByEmail(logonName)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with login=%s not found", logonName)));
        return new CurrentUser(user);
    }
}
