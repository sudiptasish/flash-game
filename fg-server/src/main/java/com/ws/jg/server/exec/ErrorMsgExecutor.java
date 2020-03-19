package com.ws.jg.server.exec;

import com.ws.jg.server.core.GameManager;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class ErrorMsgExecutor extends MessageExecutor {

    /**
     * @param gameManager
     */
    public ErrorMsgExecutor(GameManager gameManager) {
        super(gameManager);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.exec.MessageExecutor#execute(com.ws.jg.server.exec.Packet)
     */
    @Override
    public void execute(Packet packet) {
        // Send the error message to other players on board about the
        // exit of our existing player.
        if (getGameManager().getSessionHandler().sessionCount() > 0) {
            getGameManager().getSessionHandler().broadcastGameError(packet.getGmSession(), packet.getError());
        }
    }

}
