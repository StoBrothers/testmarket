package org.testmarket.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.testmarket.domain.User;
import org.testmarket.domain.UserRepository;

/**
 * Initialization Users.
 * 
 * @author Sergey Stotskiy
 */
@Component
@DependsOn({ "applicationProperties" })
public class UserInit extends AbstractInit {

    @Autowired
    UserRepository userRepository;

    @Override
    protected void init() {
        create("admin", "admin");
        // -------------------
        create("app1", "app1");
        create("app2", "app2");
        create("app3", "app3");
        create("app4", "app4");
        create("app5", "app5");
        create("app6", "app6");

    }

    /**
     * Create one User
     * 
     * @param email
     * @param password
     */
    private void create(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }
}
