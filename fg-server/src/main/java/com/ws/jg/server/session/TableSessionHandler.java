package com.ws.jg.server.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.msg.GameMessage;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class TableSessionHandler implements SessionHandler {
    
    private final TableSessionInfo sessionInfo = new TableSessionInfo();
    
    public TableSessionHandler() {}

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#getAvailableSessions()
     */
    @Override
    public Collection<PlayingSession> availableSessions() {
        return sessionInfo.getSessions();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#sessionCount()
     */
    @Override
    public int sessionCount() {
        return sessionInfo.count();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#addPlayingSession(com.ws.jg.server.session.PlayingSession)
     */
    @Override
    public void addPlayingSession(PlayingSession session) {
        sessionInfo.put(session);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#findBySession(java.lang.String)
     */
    @Override
    public PlayingSession findBySession(String sessionId) {
        return sessionInfo.findBySession(sessionId);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#findByPlayer(java.lang.String)
     */
    @Override
    public PlayingSession findByPlayer(String playerId) {
        return sessionInfo.findByPlayer(playerId);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#removeSession(java.lang.String)
     */
    @Override
    public void removeSession(String sessionId) {
        sessionInfo.remove(sessionId);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#broadcastPlayerArrival(com.ws.jg.server.session.PlayingSession)
     */
    @Override
    public void broadcastPlayerArrival(PlayingSession session) {
        GameMessage boardMsg = new GameMessage();
        boardMsg.setType(Message.G_TYPE.ON_BOARD);
        
        for (PlayingSession gs : availableSessions()) {
            boardMsg.addPlayer(gs.getPlayer());
        }
        broadcastToAll(boardMsg);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#broadcastPlayerExited(com.ws.jg.server.session.PlayingSession)
     */
    @Override
    public void broadcastPlayerExited(PlayingSession session, String playerId, int chairId) {
        GameMessage boardMsg = new GameMessage();
        boardMsg.setType(Message.G_TYPE.EXIT);
        boardMsg.setCurrentPlayer(playerId);
        boardMsg.setExitChairId(chairId);
        
        for (PlayingSession gs : availableSessions()) {
            boardMsg.addPlayer(gs.getPlayer());
        }
        broadcastToAll(boardMsg);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#broadcastGameStart(java.lang.String, com.ws.jg.server.session.PlayingSession)
     */
    @Override
    public void broadcastGameStart(String gameId, PlayingSession session) {
        List<Player> players = new ArrayList<>();
        
        for (PlayingSession gs : availableSessions()) {
            players.add(gs.getPlayer());
        }
        
        for (PlayingSession gs : availableSessions()) {
            GameMessage startMsg = new GameMessage();
            startMsg.setType(Message.G_TYPE.START);
            startMsg.setGameId(gameId);            
            startMsg.setPlayers(players);
            //startMsg.setCurrentPlayer(session.getPlayer().getId());
            startMsg.setCards(gs.getCards());
            
            broadcast(gs, startMsg);
        }
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#broadcastGameComplete(java.lang.String, com.ws.jg.server.session.PlayingSession, int)
     */
    @Override
    public void broadcastGameComplete(String gameId, PlayingSession session, int winningChairId) {
        List<Player> players = new ArrayList<>();
        Card[][] cards = new Card[availableSessions().size()][];
        
        int i = 0;
        for (PlayingSession gs : availableSessions()) {
            players.add(gs.getPlayer());
            cards[i ++] = gs.getCards();
        }
        GameMessage completeMsg = new GameMessage();
        completeMsg.setType(Message.G_TYPE.COMPLETE);
        completeMsg.setGameId(gameId);            
        completeMsg.setPlayers(players);
        completeMsg.setCurrentPlayer(session.getPlayer().getId());
        completeMsg.setWinningChair(winningChairId);
        completeMsg.setAllCards(cards);
        
        broadcastToAll(completeMsg);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#broadcastCurrentBid(com.ws.jg.server.session.PlayingSession, com.ws.jg.server.msg.Message)
     */
    @Override
    public void broadcastCurrentBid(PlayingSession session, Message message) {
        List<Player> players = new ArrayList<>();
        
        for (PlayingSession gs : availableSessions()) {
            players.add(gs.getPlayer());
        }
        ((GameMessage)message).setPlayers(players);
        broadcastToAll(message);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#broadcastGameError(com.ws.jg.server.session.PlayingSession, java.lang.Throwable)
     */
    @Override
    public void broadcastGameError(PlayingSession session, Throwable error) {
        GameMessage boardMsg = new GameMessage();
        boardMsg.setType(Message.G_TYPE.ERROR);
        boardMsg.setError(error.getMessage());
        
        for (PlayingSession gs : availableSessions()) {
            boardMsg.addPlayer(gs.getPlayer());
        }
        broadcastToAll(boardMsg);
    }
    
    /**
     * Here the actual message broadcasting takes place.
     * 
     * @param session   Current session which initiated this callback.
     * @param message   Final message to be broadcasted.
     */
    private void broadcastToAll(Message message) {
        for (PlayingSession peer : availableSessions()) {
            broadcast(peer, message);
        }
    }
    
    /**
     * Broadcast the message to the specific client as identified by the session.
     * @param peer
     * @param message
     */
    private void broadcast(PlayingSession peer, Message message) {
        if (peer.isActive()) {
            peer.broadcast(message);
            Logger.log(
                    String.format("Player [%s] is active. Sending message: [%s]"
                            , peer.getPlayer().getId()
                            , message));
        }
        else {
            Logger.log(
                    String.format("Player [%s] is no longer active. Skipping message: [%s]"
                            , peer.getPlayer().getId()
                            , message));
        }
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.session.SessionHandler#close()
     */
    @Override
    public void close() {
        sessionInfo.flush();
    }
}