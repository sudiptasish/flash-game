package com.ws.jg.server.session;

import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.msg.Message;

/**
 * 
 * @author Sudiptasish Chanda
 */
public interface PlayingSession {
    
    /**
     * Return the unique id associated with the current session.
     * @return String
     */
    String getId();

    /**
     * Player, who has initiated this session.
     * @return Player
     */
    Player getPlayer();
    
    /**
     * Return the time (in milliseconds from epoch) when this
     * session was initiated.
     * 
     * @return long
     */
    long startTime();
    
    /**
     * Return the duration (in milliseconds) this user is connected
     * to the table.
     * 
     * @return long
     */
    long duration();
    
    /**
     * Check to see if the current user session is still active or not.
     * @return boolean
     */
    boolean isActive();
    
    /**
     * Broadcast the message to the other sessions.
     * @param msg
     */
    void broadcast(Message msg);
    
    /**
     * Assign the cards to this playing session.
     * @param cards
     */
    void assign(Card[] cards);
    
    /**
     * Return the cards being held by this session.
     * @return Card[]
     */
    Card[] getCards();
}
