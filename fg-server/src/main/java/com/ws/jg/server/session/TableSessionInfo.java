package com.ws.jg.server.session;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class TableSessionInfo {

    // Map to hold the number of sessions (players) connected to this table.
    private ConcurrentMap<String, PlayingSession> sessionMapping = new ConcurrentHashMap<>();
    
    public TableSessionInfo() {}
    
    /**
     * Add this new session to the map, possibly because of
     * player on-boarding.
     * @param session
     */
    public void put(PlayingSession session) {
        sessionMapping.put(session.getId(), session);
    }
    
    /**
     * Remove this session from the map, possibly because of
     * player off-boarding.
     * @param sessionId
     */
    public void remove(String sessionId) {
        sessionMapping.remove(sessionId);
    }
    
    /**
     * Find an existing playing session as identified by this id.
     * @param sessionId
     * @return PlayingSession
     */
    public PlayingSession findBySession(String sessionId) {
        return sessionMapping.get(sessionId);
    }
    
    /**
     * Session and player has one-to-one relationship. A session
     * encapsulates the player details (it's id, name, etc).
     * This API will return the session that was initiated by the
     * player as identified by this playerId.
     * 
     * If no such session found, then this API will return null.
     * 
     * @param playerId
     * @return PlayingSession
     */
    public PlayingSession findByPlayer(String playerId) {
        for (PlayingSession gmSession : sessionMapping.values()) {
            if (gmSession.getPlayer().getId().equals(playerId)) {
                return gmSession;
            }
        }
        return null;
    }
    
    /**
     * Return the total number of sessions connected to the table. 
     * @return int
     */
    public final int count() {
        return sessionMapping.size();
    }
    
    /**
     * Return the list of session objects for this table.
     * @return Collection
     */
    public final Collection<PlayingSession> getSessions() {
        return sessionMapping.values();
    }
    
    /**
     * Flush all the session info.
     */
    public void flush() {
        sessionMapping.clear();
    }
}
