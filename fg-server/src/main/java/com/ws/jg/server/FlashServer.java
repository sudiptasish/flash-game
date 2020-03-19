package com.ws.jg.server;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.ws.jg.common.client.TableMgmtClient;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.ObjectCreator;
import com.ws.jg.server.core.GameManager;
import com.ws.jg.server.core.MessageDecoder;
import com.ws.jg.server.core.MessageEncoder;
import com.ws.jg.server.test.SimplePlayerGenerator;
import com.ws.jg.server.test.SimpleTableGenerator;

/**
 * 
 * @author Sudiptasish Chanda
 */
@ApplicationScoped
@ServerEndpoint(value = "/flash/table/{table-id}"
    , encoders = {MessageEncoder.class}
    , decoders = {MessageDecoder.class}
)
public final class FlashServer implements GameServer {
    
    private static final Map<String, GameManager> activeGmMapping = new HashMap<>();
    
    static {
        if (Constants.UNIT_TETING_MODE) {
            prepareTestCase();
        }
    }
    
    /**
     * Initialize the dummy game server with a default table
     * and few default players.
     */
    private static void prepareTestCase() {
        SimpleTableGenerator.generateTable(10);
        SimplePlayerGenerator.generatePlayer(4);
    }

    @OnOpen
    public void onBoard(@PathParam("table-id") String tableId, Session session) {
        try {
            GameTable table = TableMgmtClient.get().getTable(tableId);
            GameManager gameManager = activeGmMapping.get(tableId);
            if (gameManager == null) {
                synchronized(tableId) {
                    if (gameManager == null) {
                        gameManager = ObjectCreator.create(GameManager.FLASH_GAME_MANAGER
                                , new Class[] {GameTable.class}
                                , new Object[] {table});
                        
                        activeGmMapping.put(tableId, gameManager);
                    }
                }
            }
            gameManager.onBoard(session);
        }
        catch (RuntimeException e) {
            Logger.log(e.getMessage());
        }
    }

    @OnMessage
    public void receive(@PathParam("table-id") String tableId, Message message, Session session) {
        GameManager gameManager = activeGmMapping.get(tableId);
        if (gameManager == null) {
            throw new IllegalStateException(
                    String.format("Game Manager not found for running table %s", tableId));
        }
        gameManager.process(message, session);
    }

    @OnClose
    public void exit(@PathParam("table-id") String tableId, Session session) {
        GameManager gameManager = activeGmMapping.get(tableId);
        if (gameManager == null) {
            throw new IllegalStateException(
                    String.format("Game Manager not found for running table %s", tableId));
        }
        gameManager.exit(session);
        
        // Check if the game manager has any remaining session(s),
        // otherwise dispose this game manager.
        if (gameManager.getSessionHandler().sessionCount() == 0) {
            gameManager = activeGmMapping.remove(gameManager.getId());
            gameManager.dispose();
        }
    }

    @OnError
    public void error(@PathParam("table-id") String tableId, Throwable error, Session session) {
        GameManager gameManager = activeGmMapping.get(tableId);
        if (gameManager == null) {
            throw new IllegalStateException(
                    String.format("Game Manager not found for running table %s", tableId));
        }
        gameManager.error(error, session);
    }
}
