package com.ws.jg.server.exec;

import java.sql.Date;

import com.ws.jg.common.client.BidTrackerClient;
import com.ws.jg.common.client.GameMgmtClient;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.common.util.Logger;
import com.ws.jg.server.core.GameManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class ExitMsgExecutor extends MessageExecutor {

    /**
     * @param gameManager
     */
    public ExitMsgExecutor(GameManager gameManager) {
        super(gameManager);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.exec.MessageExecutor#execute(com.ws.jg.server.exec.Packet)
     */
    @Override
    public void execute(Packet packet) {
        // It is somewhat difficult to say whether a player has gracefully exited by
        // clicking on the "Exit" hyperlink, or (s)he was accidentally disconnected.
        // If it is a graceful exit, then do the followings:
        // 1. Release the chair.
        // 2. The player's current session will be disconnected.
        // 3. Update the remote service.
        getGameManager().getSessionHandler().removeSession(packet.getGmSession().getId());
        
        if (getGameManager().getSessionHandler().sessionCount() == 0
                || getGameManager().getSessionHandler().sessionCount() < getGameManager().getTable().getMaxCapacity()) {
            getGameManager().getTable().setFrozen(false);
        }
        if (getGameManager().getSessionHandler().sessionCount() == 0) {
            if (getGameManager().getTable().getCurrentGameId() == null) {
                Logger.log("It seems the game has not yet been started");
            }
            else {
                // If no more session left, that means everybody just quit.
                FlashGame game = GameMgmtClient.get().getGame(getGameManager().getTable().getCurrentGameId());
                if (game == null) {
                    // It is possible that before the game was started, player quit.
                    // Which means the game object was never created.
                    Logger.log("Inconsistent state! No game found for id " +
                            getGameManager().getTable().getCurrentGameId());
                }
                else {
                    game.setStatus(2);  // NOT_CONCLUDED
                    game.setEndTime(new Date(System.currentTimeMillis()));
                    GameMgmtClient.get().updateGame(game);
                    
                    BidTrackerClient.get().finalizeBids(getGameManager().getTable().getCurrentGameId());
                    
                    // Dessociate the game from the underlying table.
                    getGameManager().getTable().setCurrentGameId(null);
                }
            }
        }
        int chairId = packet.getGmSession().getPlayer().getChairId();
        getGameManager().getTable().exit(packet.getGmSession().getPlayer());
                
        updateTableDefinition(getGameManager().getTable());
        updatePlayerDefinition(packet.getGmSession().getPlayer());
        
        // Send the notification to other players on board about the
        // exit of our existing player.
        if (getGameManager().getSessionHandler().sessionCount() > 0) {
            getGameManager().getSessionHandler().broadcastPlayerExited(packet.getGmSession()
                    , packet.getGmSession().getPlayer().getId()
                    , chairId);
        }
    }

}
