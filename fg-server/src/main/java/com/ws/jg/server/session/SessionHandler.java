package com.ws.jg.server.session;

import java.util.Collection;

import com.ws.jg.common.msg.Message;

/**
 * 
 * @author Sudiptasish Chanda
 */
public interface SessionHandler {
    
    int MAX_SESSION = 5;
    
    /**
     * Return the number of sessions associated with the table.
     * @return Session[]
     */
    Collection<PlayingSession> availableSessions();
    
    /**
     * Return the total session count.
     * @return int
     */
    int sessionCount();
    
    /**
     * Clear the current session handler. 
     */
    void close();
    
    /**
     * Add this new session object to existing list.
     * @param session
     */
    void addPlayingSession(PlayingSession session);
    
    /**
     * Find and return the session (for this table) as identified by this id.
     * 
     * @param table
     * @param sessionId
     * @return GamingSession
     */
    PlayingSession findBySession(String sessionId);
    
    /**
     * Find and return the session (for this table) as identified by this player id.
     * 
     * @param table
     * @param userId
     * @return
     */
    PlayingSession findByPlayer(String playerId);
    
    /**
     * Remove the session identified by this id from the underlying session store.
     * @param sessionId
     */
    void removeSession(String sessionId);
    
    /**
     * Broadcast the new player arrival info to all connected sessions/players.
     * 
     * @param session   Indicates the session which initially sent a message
     *                  that resulted a broadcast.
     */
    void broadcastPlayerArrival(PlayingSession session);
    
    /**
     * Broadcast the new player exit info to all connected sessions/players.
     * 
     * @param session   Indicates the session which initially sent a message
     *                  that resulted a broadcast.
     * @param playerId  Indicates the player that has just exited the room.
     * @param chairId   Chair to be freed up.                
     */
    void broadcastPlayerExited(PlayingSession session, String playerId, int chairId);
    
    /**
     * Broadcast the error message to all connected sessions/players.
     * @param session
     * @param error
     */
    void broadcastGameError(PlayingSession session, Throwable error);
    
    /**
     * Broadcast the game start signal to all connected sessions/players.
     * @param gameId
     * @param session
     */
    void broadcastGameStart(String gameId, PlayingSession session);

    /**
     * Broadcast the current bid details to all connected sessions/players.
     * @param message
     */
    void broadcastCurrentBid(PlayingSession session, Message message);
    
    /**
     * Broadcast the game completion message to all.
     * @param gameId
     * @param session
     * @param winningChairId
     */
    void broadcastGameComplete(String gameId, PlayingSession session, int winningChairId);
}
