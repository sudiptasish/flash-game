package com.ws.jg.server.exec;

import com.ws.jg.server.core.GameManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class OnBoardMsgExecutor extends MessageExecutor {

    /**
     * @param gameManager
     */
    public OnBoardMsgExecutor(GameManager gameManager) {
        super(gameManager);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.exec.MessageExecutor#execute(com.ws.jg.server.exec.Packet)
     */
    @Override
    public void execute(Packet packet) {
        getGameManager().getSessionHandler().addPlayingSession(packet.getGmSession());
        
        // Now assign the table (and a chair) to this new player (if none already assigned).
        if (packet.getGmSession().getPlayer().getChairId() == -1) {
            getGameManager().getTable().join(packet.getGmSession().getPlayer());
        }
        // Now make call to remote table mgmt and player mgmt service to update
        // their respective stores with the updated table def and player definition.
        updatePlayerDefinition(packet.getGmSession().getPlayer());
        
        // Update the latest table status (with the player assignment details).
        updateTableDefinition(getGameManager().getTable());
        
        // Send the notification to other players that are already on board,
        // about the arrival of our new player.
        getGameManager().getSessionHandler().broadcastPlayerArrival(packet.getGmSession());
    }

}
