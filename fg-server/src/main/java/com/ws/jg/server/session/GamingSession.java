package com.ws.jg.server.session;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.msg.Message;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class GamingSession implements PlayingSession {
    
    // Player information, this session is related to.
    private final Player player;
    
    // Underlying "websocket" session.
    private final Session session;

    private long startTime = 0L;
    
    private Card[] cards = new Card[3];
    
    public GamingSession(Session session, Player player) {
        this.session = session;
        this.player = player;
        this.startTime = System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.GamingSession#getId()
     */
    @Override
    public final String getId() {
        return session.getId();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.GamingSession#getPlayer()
     */
    @Override
    public final Player getPlayer() {
        return player;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.GamingSession#startTime()
     */
    @Override
    public final long startTime() {
        return startTime;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.GamingSession#duration()
     */
    @Override
    public final long duration() {
        return System.currentTimeMillis() - startTime;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.GamingSession#isActive()
     */
    @Override
    public final boolean isActive() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.PlayingSession#assign(com.ws.jg.common.model.Card[])
     */
    @Override
    public void assign(Card[] cards) {
        this.cards = cards;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.PlayingSession#getCards()
     */
    @Override
    public Card[] getCards() {
        return this.cards;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.GamingSession#broadcast(com.ws.jg.server.msg.Message)
     */
    @Override
    public void broadcast(Message msg) {
        try {
            if (session.isOpen()) {
                this.session.getBasicRemote().sendObject(msg);
            }
        }
        catch (IOException | EncodeException e) {
            throw new RuntimeException(e);
        }
    }
}
