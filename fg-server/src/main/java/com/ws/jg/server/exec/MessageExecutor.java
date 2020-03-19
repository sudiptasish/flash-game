package com.ws.jg.server.exec;

import com.ws.jg.common.client.PlayerMgmtClient;
import com.ws.jg.common.client.TableMgmtClient;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.model.Player;
import com.ws.jg.server.core.GameManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public abstract class MessageExecutor {

    private final GameManager gameManager;
    
    protected MessageExecutor(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    /**
     * @return the gameManager
     */
    public GameManager getGameManager() {
        return gameManager;
    }
    
    /**
     * Update the table definition in the remote service, so that
     * other players requesting to join to the same table must see
     * the updated state.
     * 
     * @param table
     */
    protected void updateTableDefinition(GameTable table) {
        TableMgmtClient.get().updateTable(table);
    }
    
    /**
     * Update the player definition in the remote service.
     * The player will have a chair assigned to him/her.
     * 
     * @param player
     */
    protected void updatePlayerDefinition(Player player) {
        PlayerMgmtClient.get().updatePlayer(player);
    }

    /**
     * Execute the task.
     * @param packet
     */
    public abstract void execute(Packet packet);
}
