package org.testmarket.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.User;
import org.testmarket.domain.UserRepository;
import org.testmarket.domain.WSToken;
import org.testmarket.domain.WSTokenRepository;
import org.testmarket.util.ApplicationProperties;
import org.testmarket.util.AuthenticationMsg;
import org.testmarket.util.WSAuthenticationException;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.LocalDateTime;


/**
 *
 * Authentication service. 
 * 
 * @author Sergey Stotskiy
 *
 */

@Service("authenticationService") 
public class AuthenticationServiceImpl implements AuthenticationService {
	
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthenticationServiceImpl.class);

	
    private static final String PASSWORD = "password";

    private static final String EMAIL = "email";

    private static final String LOGIN_CUSTOMER = "LOGIN_CUSTOMER";

    /** headers of message with errors **/
    private static final String CUSTOMER_ERROR = "CUSTOMER_ERROR";
    /** headers of successful message **/
    private static final String CUSTOMER_API_TOKEN = "CUSTOMER_API_TOKEN";

    private static final String API_TOKEN = "api_token";

    private static final String API_TOKEN_EXPIRATION_DATE = "api_token_expiration_date";

    private static final String ERROR_CODE = "error_code";

    private static final String ERROR_DESCRIPTION = "error_description";

    private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CurrentUserDetailsService currentUserDetailsService;
    
    @Autowired
    private WSTokenRepository tokenRepository;
    
    @Autowired
    private SessionHandler sessionHandler;

    
    /**
     * Handle Authentication message.
     * @param message input/output message
     * @param username user name 
     * @param simpSessionId session id
     * @return message
     */
    @Transactional
    public AuthenticationMsg handleMessage(AuthenticationMsg message, String username, String simpSessionId) {

        if (!LOGIN_CUSTOMER.equalsIgnoreCase(message.getType())) {
            LOGGER.error("Wrong type of message:%s.", message.getType());
            throw new WSAuthenticationException(
                String.format("Wrong type of message:%s.", message.getType()));
        }

        Map<String, String> dataMap = message.getData();

        if (!checkDataSection(dataMap)) {
            LOGGER.error("Wrong data section of message.");
            throw new WSAuthenticationException("Wrong data section of message.");
        }

        User user = authentificateUser(dataMap);
        dataMap.clear();

        if (user == null) {
            message.setType(CUSTOMER_ERROR);
            dataMap.put(ERROR_DESCRIPTION, "Customer not found");
            dataMap.put(ERROR_CODE, "customer.notFound");
        } else {
            message.setType(CUSTOMER_API_TOKEN);
            String uuid = UUID.randomUUID().toString();
            Date date = getExpirationDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            sessionHandler.addNewToken(uuid, date, simpSessionId);// update
                                                                  // active
                                                                  // sockets

            WSToken token = new WSToken(uuid, date, user, simpSessionId);
            updateActiveToken(simpSessionId, token);

            dataMap.put(API_TOKEN, String.valueOf(uuid));
            dataMap.put(API_TOKEN_EXPIRATION_DATE, dateFormat.format(date));
        }
        return message;
    }

    
    
    /**
     * Check data section
     * 
     * @param dataMap
     * @return
     */
    private boolean checkDataSection(Map<String, String> dataMap) {
    	return  (!(dataMap == null || dataMap.isEmpty() || dataMap.get(EMAIL) == null
            || dataMap.get(PASSWORD) == null || dataMap.get(EMAIL).length() == 0
            || dataMap.get(PASSWORD).length() == 0));
    }
	
    /**
     * Authentication new User.
     * 
     * @param logonName
     *            userName
     * @param password
     *            password
     * @return status Authentication process
     */
    private User authentificateUser(Map<String, String> dataMap) {
    	
    	String password = dataMap.get(PASSWORD);
        String logonName = dataMap.get(EMAIL);
        

        Optional<User> user = userRepository.findOneByEmail(logonName);

        if (!user.isPresent() || !user.get().getPassword().equalsIgnoreCase(password)) {
            LOGGER.info("User is not uthentificated.");
            return null;
        }

        // Get context for anonymous user SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();

        // Authentication process for new User
        CurrentUser currentUser = null;

        try {
            currentUser = (CurrentUser) currentUserDetailsService
                .loadUserByUsername(logonName);
        } catch (UsernameNotFoundException uex) {
            LOGGER.info("User not loaded " + uex.getMessage());
            return null;
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            currentUser, AuthorityUtils.createAuthorityList("ADMIN", "USER"),
            currentUser.getAuthorities());

        context.setAuthentication(authentication); // set new authenticated User in security

        return user.get();
    }
    
    /**
     * Get expiration date for new token. It's (current time + plusSeconds from configuration file
     * (applications.yml)).
     * 
     * @return expiration date
     */
    private Date getExpirationDate() {

        int expirationSeconds = ApplicationProperties.getPlusseconds();
        LocalDateTime workDateTime = LocalDateTime.now();
        LocalDateTime expirationTime = workDateTime.plusSeconds(expirationSeconds);
        return expirationTime.toDate();
    }

    
    /**
     * Reset old token and Save new active token.
     * 
     * @param principalName
     */
    private void updateActiveToken(String simpSessionId, WSToken newToken) {
        Optional<WSToken> wsToken = tokenRepository
            .findOneByPrincipalNameAndActive(simpSessionId, true);
        if (wsToken.isPresent()) {
        WSToken token = wsToken.get();
        token.setActive(false);
        tokenRepository.save(token);
        }
        tokenRepository.saveAndFlush(newToken);
    }
    
}
