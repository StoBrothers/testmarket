package org.testmarket.service;

import org.testmarket.util.AuthenticationMsg;

public interface AuthenticationService {
    
    
    /**
     * Handle Authentication message.
     * @param message input/output message
     * @param username user name 
     * @param simpSessionId session id
     * @return message
     */
    public AuthenticationMsg handleMessage(AuthenticationMsg message, String username, String simpSessionId);


}
