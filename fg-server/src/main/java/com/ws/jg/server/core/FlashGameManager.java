package com.ws.jg.server.core;

import javax.websocket.Session;

import com.ws.jg.common.client.PlayerMgmtClient;
import com.ws.jg.common.client.TableMgmtClient;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.model.TableStat;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Logger;
import com.ws.jg.server.exec.MessageExecutor;
import com.ws.jg.server.exec.Packet;
import com.ws.jg.server.session.PlayingSession;
import com.ws.jg.server.session.SessionHandler;
import com.ws.jg.server.session.TableSessionHandler;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class FlashGameManager extends GameManager {

    // Default Session handler.
    // This session handler is responsible for broadcasting messages
    // to other active session(s) connected to the same table and
    // perform several other tasks.
    private final SessionHandler handler = new TableSessionHandler();
    
    public FlashGameManager(GameTable table) {
        super(table);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.GameManager#getSessionHandler()
     */
    @Override
    public final SessionHandler getSessionHandler() {
        return this.handler;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.GameManager#start(java.lang.Object)
     */
    @Override
    public void start(Object arg) {
        // Do other initialization if required.
        if (isStarted()) {
            throw new IllegalStateException(
                    String.format("Trying to start the game manager for table %s again"
                            , getTable().getId()));
        }
        setStarted(true);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.GameManager#process(com.ws.jg.server.msg.Message, javax.websocket.Session)
     */
    @Override
    public void process(Message message, Session session) {
        if (message.getGameId() != null && message.getGameId().trim().length() > 0) {
            if (!message.getGameId().equals(getTable().getCurrentGameId())) {
                throw new IllegalStateException(
                        String.format("Game Id [%s] sent by client does not match with current game manager's game Id [%s]"
                                , message.getGameId()
                                , getTable().getCurrentGameId()));
            }
        }
        Player player = PlayerMgmtClient.get().findThisPlayer(session);
        Logger.log(String.format(
                "Received a new message [%s] from player [%s] for table [%s]. Sesion: %s"
                , message
                , player.getId()
                , getTable().getId()
                , session.getId()));
        
        PlayingSession gmSession = findAndValidate(session);
        message.setCurrentPlayer(gmSession.getPlayer().getId());
        
        MessageExecutor executor = execMapping.get(message.getType());
        executor.execute(new Packet(gmSession, message));
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.GameManager#exit(javax.websocket.Session)
     */
    @Override
    public void exit(Session session) {
        Player player = PlayerMgmtClient.get().findThisPlayer(session);
        Logger.log(String.format(
                "Player [%s] is existing from table [%s]. Sesion: %s"
                , player.getId()
                , getTable().getId()
                , session.getId()));
        
        PlayingSession gmSession = findAndValidate(session);
        
        MessageExecutor executor = execMapping.get(Message.G_TYPE.EXIT);
        executor.execute(new Packet(gmSession, (Message)null));
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.GameManager#error(java.lang.Throwable, javax.websocket.Session)
     */
    @Override
    public void error(Throwable error, Session session) {
        Player player = PlayerMgmtClient.get().findThisPlayer(session);
        if (player == null) {
            return;
        }
        Logger.log(String.format(
                "Player [%s] on table [%s] has encountered error. Sesion: %s"
                , player.getId()
                , getTable().getId()
                , session.getId()));
        
        PlayingSession gmSession = findAndValidate(session);
        
        MessageExecutor executor = execMapping.get(Message.G_TYPE.ERROR);
        executor.execute(new Packet(gmSession, error));
    }
    
    /**
     * Validate the player session to see if it is associated with the current game manager.
     * If so, then return the PlayingSession.
     * @param session
     * @return PlayingSession
     */
    private PlayingSession findAndValidate(Session session) {
        PlayingSession gmSession = getSessionHandler().findBySession(session.getId());
        if (gmSession == null) {
            throw new IllegalArgumentException(
                    String.format("Session %s is not found associated to table %s"
                            , session.getId()
                            , getTable().getId()));
        }
        return gmSession;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.GameManager#dispose()
     */
    @Override
    public void dispose() {
        getTable().removeAll();
        getTable().setTableStat(TableStat.WAITING_FOR_PLAYERS);
        
        // Publish the latest table status to the remote service.
        TableMgmtClient.get().updateTable(getTable());
    }
}
