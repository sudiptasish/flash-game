package com.ws.jg.server.core;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import com.ws.jg.common.client.PlayerMgmtClient;
import com.ws.jg.common.client.TableMgmtClient;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.model.TableStat;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;
import com.ws.jg.common.util.Logger;
import com.ws.jg.server.exec.CompleteMsgExecutor;
import com.ws.jg.server.exec.ErrorMsgExecutor;
import com.ws.jg.server.exec.ExitMsgExecutor;
import com.ws.jg.server.exec.InGameMsgExecutor;
import com.ws.jg.server.exec.MessageExecutor;
import com.ws.jg.server.exec.OnBoardMsgExecutor;
import com.ws.jg.server.exec.Packet;
import com.ws.jg.server.exec.StartMsgExecutor;
import com.ws.jg.server.session.PlayingSession;
import com.ws.jg.server.session.SessionFactory;
import com.ws.jg.server.session.SessionHandler;

/**
 * 
 * @author Sudiptasish Chanda
 */
public abstract class GameManager {
    
    public static final String FLASH_GAME_MANAGER = "com.ws.jg.server.core.FlashGameManager";
    
    // Id of this game manager, currently the table Id.
    // Because it is expected that only one active game manager
    // will be there for a given table at any point of time.
    private final String id;
    private GameTable table;
    private boolean started = false;
    
    protected Map<Message.G_TYPE, MessageExecutor> execMapping = new HashMap<>();
    
    protected GameManager(GameTable table) {
        this.table = table;
        this.id = table.getId();
        initExecutors();
    }
    
    /**
     * Initialize the message executors.
     */
    private void initExecutors() {
        MessageExecutor inGameExec = new InGameMsgExecutor(this);
        
        execMapping.put(Message.G_TYPE.ON_BOARD, new OnBoardMsgExecutor(this));
        execMapping.put(Message.G_TYPE.START, new StartMsgExecutor(this));
        execMapping.put(Message.G_TYPE.BID, inGameExec);
        execMapping.put(Message.G_TYPE.PACK, inGameExec);
        execMapping.put(Message.G_TYPE.SHOW, inGameExec);        
        execMapping.put(Message.G_TYPE.COMPLETE, new CompleteMsgExecutor(this));      
        execMapping.put(Message.G_TYPE.EXIT, new ExitMsgExecutor(this));     
        execMapping.put(Message.G_TYPE.ERROR, new ErrorMsgExecutor(this));
    }

    /**
     * @return the id
     * (currently the table id this game manager is associated with)
     */
    public String getId() {
        return id;
    }

    /**
     * @return the table
     */
    public final GameTable getTable() {
        return table;
    }

    /**
     * @return the started
     */
    public final boolean isStarted() {
        return started;
    }
    
    /**
     * Freeze the table and change the table status to IN_GAME.
     * @param gameId
     */
    public void inGame(String gameId) {
        getTable().setFrozen(true);
        getTable().setTableStat(TableStat.IN_GAME);
        getTable().setCurrentGameId(gameId);
    }

    /**
     * @param started the started to set
     */
    public void setStarted(boolean started) {
        this.started = started;
    }
    
    /**
     * Return the session handlerfor this game manager.
     * @return SessionHandler
     */
    public abstract SessionHandler getSessionHandler();
    
    /**
     * On-board a new player.
     * @param session
     */
    public void onBoard(Session session) {
        Player player = PlayerMgmtClient.get().findThisPlayer(session);
        
        // It is possible that this player object has already have a chair,
        // which means (s)he is already part of an active game.
        // May be (s)he got disconnected due to some connectivity issue,
        // and trying to join again !
        Logger.log(String.format(
                "Received a new request [session: %s] from player [%s] to join to table [%s]"
                , session.getId()
                , player.getId()
                , getTable().getId()));
    
        session.setMaxIdleTimeout(Constants.SESSION_MAX_IDLE_TIMEOUT);
        
        // It is possible that this user has been disconnected.
        // Therefore find the current (stale) session against this
        // userId and replace it with the new session.
        boolean flag = isStaleSession(session, player);
        
        // In case the plaer already has a chair associated to it, remove the assignment,
        // only if there is NO stale session.
        if (!flag) {
            player.setChairId(-1);
        }
        
        // Fetch the latest table definition from the remote service.
        // This will also have the information about the number of
        // players currently associated with it.
        this.table = TableMgmtClient.get().getTable(getTable().getId());
        
        // Otherwise, add the user session to the global session store.
        if (!flag && getTable().getPlayerCount() == getTable().getMaxCapacity()) {
            throw new IllegalArgumentException(
                    String.format("No more player accepted for table %s", getTable().getId()));
        }
        if (!flag && getTable().isFrozen()) {
            throw new IllegalArgumentException(
                    String.format("A game has already been started for table %s", getTable().getId()));
        }
        // All validation successful!
        // Create a new playing session for our new player and invoke the executor.
        PlayingSession gmSession = SessionFactory.getSessionFactory().createNew(session, player);
        MessageExecutor executor = execMapping.get(Message.G_TYPE.ON_BOARD);
        executor.execute(new Packet(gmSession, (Message)null));
    }

    /**
     * Start this game manager.
     * @param arg
     */
    public abstract void start(Object arg);
    
    /**
     * Process the message.
     * @param message
     * @param session
     */
    public abstract void process(Message message, Session session);
    
    /**
     * This API will be invoked whenever a player exits from the
     * table.
     * @param session
     */
    public abstract void exit(Session session);
    
    /**
     * This API will be invoked if an exception occurred in the
     * client connectivity, or whule sending/receiving any message/bid.
     * 
     * @param error
     * @param session
     */
    public abstract void error(Throwable error, Session session);
    
    /**
     * Dispose a game manager.
     */
    public abstract void dispose();
    
    /**
     * Check if a stale session exists for the same player.
     * @param session
     * @param player
     */
    private boolean isStaleSession(Session session, Player player) {
        PlayingSession gmSession = getSessionHandler().findByPlayer(player.getId());
        if (gmSession != null) {
            // We are right, a stale session exists.
            // Now check the session id, if they are matching, reject the on-boarding request.
            if (gmSession.getId().equals(session.getId())) {
                throw new IllegalArgumentException(
                        String.format("Player [%s] is trying to rejoin with same session [%s]"
                                , player.getId()
                                , session.getId()));
            }
            else {
                // Player might have been disconnected, so remove the stale session.
                // But do NOT remove the chair assignment. We are going to use the same chair.
                getSessionHandler().removeSession(gmSession.getId());
                return true;
            }
        }
        return false;
    }
}
