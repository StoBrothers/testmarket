package org.testmarket.service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.testmarket.domain.WSToken;
import org.testmarket.domain.WSTokenRepository;
import org.testmarket.util.ApplicationProperties;
import org.testmarket.util.WSSessionWrapper;

/**
 * SessionHandler is registering every websocket session and execute thread with checks websocket
 * sessions. This Thread every 5 seconds check expiration date for open sessions. If session is
 * expired thread forced to update status token to NOT active status in storage.
 * 
 * @author Sergey Stotskiy
 *
 */

@Service
public class SessionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);

    private final ScheduledExecutorService scheduler = Executors
        .newSingleThreadScheduledExecutor();
    /**
     * Map <Websocket number in string format, WebSocketSession>
     * Storage of opened sockets.   
     */
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * Map <Token, Wrapper for WebSocketSession> it's buffer for new received tokens. 
     * This tokens will be moved to tokenMap for to keep track of expiration time.   
     */
    private final Map<String, WSSessionWrapper> newcomersMap = new ConcurrentHashMap<>();

    /**
     * Map <Token, Wrapper for WebSocketSession> 
     * Map of active tokens for control of expiration time.   
     */
    private final Map<String, WSSessionWrapper> tokenMap = new ConcurrentHashMap<>();


    @Autowired
    private WSTokenRepository tokenRepository;

    public SessionHandler() {

        LOGGER.info("Service will work over " + ApplicationProperties.getInitialDelay()
            + " seconds  with period:  " + ApplicationProperties.getPeriod()
            + " seconds");

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                tokenMap.keySet().forEach(k -> {
                    LocalDateTime workDateTime = LocalDateTime.now();
                    WSSessionWrapper sessionWraper = tokenMap.get(k);
                    if (workDateTime.compareTo(sessionWraper.getExpirationTime()) > 0) {
                        // if token expired
                        Optional<WSToken> currentToken = tokenRepository
                            .findOneByToken(k);
                        if (currentToken.isPresent()) {// if token is not existed for this session
                            WSToken token = currentToken.get();
                            if (token.getActive()) {
                                token.setActive(false);
                                tokenRepository.save(token);
                            }
                            tokenMap.remove(k);
                        }
                    } else {// check if token reset state in DB
                        Optional<WSToken> currentToken = tokenRepository
                            .findOneByTokenAndActive(k, false);
                        if (currentToken.isPresent()) {// if token is not existed for this session
                            WSToken token = currentToken.get();
                            token.setActive(false);
                            tokenRepository.save(token);
                            tokenMap.remove(k);
                        }
                    }
                });

                newcomersMap.keySet().forEach(p -> {//moved from newcomers to working storage 
                    WSSessionWrapper sessionWraper = newcomersMap.remove(p);
                    tokenMap.put(p, sessionWraper);
                });

            }
        }, ApplicationProperties.getInitialDelay(), ApplicationProperties.getPeriod(),
            TimeUnit.SECONDS);

    }

    /**
     * Register WS session in session storage.
     * 
     * @param session
     */
    public void register(WebSocketSession session) {
        sessionMap.put(session.getId(), session);
    }

    /**
     * Add new token to newcomers storage.
     * 
     * @param token
     * @param expirationDate
     * @param simpSessionId
     */
    public void addNewToken(String token, Date expirationDate, String simpSessionId) {
        WebSocketSession currentSocket = null;
        if ((currentSocket = sessionMap.get(simpSessionId)) != null) {
            newcomersMap.put(token,
                new WSSessionWrapper(token, expirationDate, currentSocket));
        }
    }
    /**
     * Get working sessions.
     * @return
     */
    public Map<String, WSSessionWrapper> getSessions() {
        return tokenMap;
    }
}
