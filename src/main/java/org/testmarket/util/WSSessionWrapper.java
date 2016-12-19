package org.testmarket.util;

import java.io.IOException;
import java.util.Date;

import org.joda.time.LocalDateTime;
import org.springframework.web.socket.WebSocketSession;

/**
 * Wrapper for WebSocket Session contains unique token, 
 * expirationTime for this token, reference to active WebSocketSession.
 * 
 * @author Sergey Stotskiy
 *
 */
public class WSSessionWrapper {

    private WebSocketSession session;

    private LocalDateTime expirationTime;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public WSSessionWrapper(String token, Date expirationDate, WebSocketSession session) {
        this.token = token;
        this.session = session;
        this.expirationTime = new LocalDateTime(expirationDate);
        this.session = session;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationDateTime(Date expirationTime) {
        this.expirationTime = new LocalDateTime(expirationTime);
    }

    public WebSocketSession getSession() {
        return session;
    }

    /**
     * Return a unique session identifier.
     */
    public String getId() {
        return session.getId();
    }

    /**
     * Close the WebSocket connection with status 1000, i.e. equivalent to:
     * 
     * <pre class="code">
     * session.close(CloseStatus.NORMAL);
     */
    public void close() throws IOException {
        session.close();
    }

    @Override
    public String toString() {
        return "WSSessionWrapper [session=" + session.getId() + " -  " + session
            + ", expirationTime=" + expirationTime + "]";
    }

}
