package com.ws.jg.server.exec;

import com.ws.jg.common.client.BidTrackerClient;
import com.ws.jg.server.core.GameManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class InGameMsgExecutor extends MessageExecutor {

    /**
     * @param gameManager
     */
    public InGameMsgExecutor(GameManager gameManager) {
        super(gameManager);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.exec.MessageExecutor#execute(com.ws.jg.server.exec.Packet)
     */
    @Override
    public void execute(Packet packet) {
        try {
            if (getGameManager().getTable().getCurrentGameId() == null) {
                throw new IllegalStateException("Game for current table has not been started yet");
            }
            // For BID/PACK/SHOW message, we are updating the remote bid store with the
            // current bid.
            // For PACK/SHOW message type, there won't be any bid value associated to it.
            BidTrackerClient.get().postBid(packet.getMessage());
            
            if (getGameManager().getTable().isActive()) {
                getGameManager().getSessionHandler().broadcastCurrentBid(
                        packet.getGmSession()
                        , packet.getMessage());
            }
        }
        catch (RuntimeException e) {
            // Send the error message to other players on board about the
            // exit of our existing player.
            getGameManager().getSessionHandler().broadcastGameError(packet.getGmSession(), e);
        }
    }

}
