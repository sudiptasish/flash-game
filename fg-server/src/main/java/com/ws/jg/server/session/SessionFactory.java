package com.ws.jg.server.session;

import javax.websocket.Session;

import com.ws.jg.common.model.Player;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class SessionFactory {

    private static final SessionFactory _INSTANCE_ = new SessionFactory();
    
    private SessionFactory() {}
    
    /**
     * Return the singleton session factory instance.
     * @return SessionFactory;
     */
    public static SessionFactory getSessionFactory() {
        return _INSTANCE_;
    }
    
    /**
     * Create a new playing session.
     * 
     * @param session
     * @param playerId
     * @return PlayingSession
     */
    public PlayingSession createNew(Session session, Player player) {
        GamingSession gmSession = new GamingSession(session, player);
        return gmSession;
    }
}
