package com.ws.jg.server.exec;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.ws.jg.common.client.BidTrackerClient;
import com.ws.jg.common.client.GameMgmtClient;
import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.server.core.FlashUtil;
import com.ws.jg.server.core.GameManager;
import com.ws.jg.server.session.PlayingSession;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class CompleteMsgExecutor extends MessageExecutor {

    /**
     * @param gameManager
     */
    public CompleteMsgExecutor(GameManager gameManager) {
        super(gameManager);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.exec.MessageExecutor#execute(com.ws.jg.server.exec.Packet)
     */
    @Override
    public void execute(Packet packet) {
        // Once a game is complete we need to persist the game and bid info.
        // This will be done by invoking the respective service APIs.
        FlashGame game = GameMgmtClient.get().getGame(getGameManager().getTable().getCurrentGameId());
        if (game == null) {
            throw new IllegalStateException("Inconsistent state! No game found for is " +
                        getGameManager().getTable().getCurrentGameId());
        }
        if (game.getStatus() == 1) {
            return;
        }
        synchronized(game) {
            game = GameMgmtClient.get().getGame(getGameManager().getTable().getCurrentGameId());
            if (game.getStatus() == 1) {
                return;
            }
            game.setStatus(1);  // COMPLETE
            game.setEndTime(new Date(System.currentTimeMillis()));
            GameMgmtClient.get().updateGame(game);
        }
        // Revert back the state of the table to unfrozen, so that it can be
        // used in subsequent game.
        if (getGameManager().getSessionHandler().sessionCount() == 0) {
            getGameManager().getTable().setFrozen(false);
        }
        getGameManager().getTable().setFrozen(false);
        updateTableDefinition(getGameManager().getTable());
        
        BidTrackerClient.get().finalizeBids(getGameManager().getTable().getCurrentGameId());
        
        // Dessociate the game from the underlying table.
        getGameManager().getTable().setCurrentGameId(null);
        
        // Now compute and decide the winner.
        List<Card[]> cardList = new ArrayList<>(getGameManager().getSessionHandler().sessionCount());
        for (PlayingSession gs : getGameManager().getSessionHandler().availableSessions()) {
            cardList.add(gs.getCards());
        }
        int winningIndex = FlashUtil.chooseWinner(cardList);
        
        getGameManager().getSessionHandler().broadcastGameComplete(game.getId()
                , getWinnningSession(winningIndex)
                , winningIndex);
    }
    
    /**
     * Get the winning session info.
     * @param sessionIndex
     * @return
     */
    private PlayingSession getWinnningSession(int sessionIndex) {
        int iterationCount = 0;
        for (PlayingSession gs : getGameManager().getSessionHandler().availableSessions()) {
            if (iterationCount == sessionIndex) {
                return gs;
            }
            iterationCount ++;
        }
        return null;
    }

}
