package com.ws.jg.ps.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.ws.jg.common.msg.GameMessage;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class BidTrackerService {
    
    private static final int BID_THRESHOLD = 10000;
    
    private static final LiveBidStore bidStore = LiveBidStore.getBidStore();
    
    private final BidTrackerDAO bidDao = new BidTrackerDAO();
        
    /**
     * Add this message to the live bid store.
     * If the total number of in-memory bids reaches the threshold, then
     * persist them into the db.
     * 
     * @param bidMsg
     * @throws RuntimeException 
     */
    public void captureBid(GameMessage bidMsg, boolean transactional) {
        if (bidMsg.getTime() == null) {
            bidMsg.setTime(new Date(System.currentTimeMillis()));
        }
        bidStore.addBid(bidMsg);
        
        if (transactional && bidStore.getBidCount() >= BID_THRESHOLD) {
            List<GameMessage> bids = bidStore.drain();
            if (bids.isEmpty()) {
                return;                
            }
            try {
                bidDao.insert(bids);
            }
            catch (SQLException e) {
                Logger.log(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Upon completion of a game, all the remaining bids in the 
     * in-memory bidStore are persisted into DB.
     * 
     * @param gameId
     * @param transactional
     */
    public void persistBids(String gameId, boolean transactional) {
        List<GameMessage> bids = bidStore.drain(gameId);
        if (bids == null || bids.isEmpty()) {
            return;
        }
        try {
            bidDao.insert(bids);
        }
        catch (SQLException e) {
            Logger.log(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Return all the bids for this game.
     * 
     * @param gameId
     * @return List
     * @throws RuntimeException 
     */
    public List<GameMessage> getBids(String gameId, boolean transactional) {
        List<GameMessage> msgs = getFirstFewBids(gameId, Integer.MAX_VALUE);
        
        if (transactional) {
            try {
                List<GameMessage> bids = bidDao.select(gameId);
                
                // Now get the remaining bids (if any) from the in-memory bid store.
                bids.addAll(msgs);
                return bids;
            }
            catch (SQLException e) {
                Logger.log(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return msgs;
    }
    
    /**
     * Get the latest bid for this game.
     * @param gameId
     * @param transactional
     * @return BidMessage
     */
    public GameMessage getLastBid(String gameId, boolean transactional) {
        GameMessage lastBid = bidStore.getLatestBid(gameId);
        
        if (lastBid == null && transactional) {
            try {
                List<GameMessage> bids = bidDao.select(gameId);
                if (!bids.isEmpty()) {
                    lastBid = bids.get(bids.size() - 1);
                }
            }
            catch (SQLException e) {
                Logger.log(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return lastBid;
    }
    
    /**
     * Get the first n bids for this game.
     * @param gameId
     * @param count
     * @return List
     */
    public List<GameMessage> getFirstFewBids(String gameId, int count) {
        return bidStore.getFirstFewBids(gameId, count);
    }
    
    /**
     * Clear all the bid(s) that were raised from this game.
     * @param gameId
     */
    public void clearBids(String gameId, boolean transactional) {
        bidStore.clearBids(gameId);
        
        if (transactional) {
            try {
                bidDao.delete(gameId);
            }
            catch (SQLException e) {
                Logger.log(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Clear all the bids.
     */
    public void clearAllBids() {
        bidStore.clearAll();
    }
}
