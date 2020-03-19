package com.ws.jg.ps.service;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ws.jg.common.msg.GameMessage;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class LiveBidStore {
    
    private static final LiveBidStore bidStore = new LiveBidStore();
    private int bidCount = 0;
    private AtomicBoolean lock = new AtomicBoolean(false);

    // Map to hold the table id vs list of bid messages (in ascending order of timestamp).
    private ConcurrentMap<String, Queue<GameMessage>> msgMapping = new ConcurrentHashMap<>();
    
    private LiveBidStore() {}
    
    /**
     * Return th esingleton bid store.
     * @return LiveBidStore
     */
    public static LiveBidStore getBidStore() {
        return bidStore;
    }
    
    /**
     * Add this message to the live bid store.
     * @param bidMsg
     */
    public void addBid(GameMessage bidMsg) {
        Queue<GameMessage> msgList = msgMapping.get(bidMsg.getGameId());
        if (msgList == null) {
            msgList = new LinkedList<>();
            msgMapping.put(bidMsg.getGameId(), msgList);
        }
        msgList.add(bidMsg);
        bidCount ++;
    }
    
    /**
     * Return all the bids for this game.
     * @param gameId
     * @return List
     */
    public Queue<GameMessage> getBids(String gameId) {
        return msgMapping.get(gameId);
    }
    
    /**
     * Get the first n bids for this game.
     * @param tableId
     * @param count
     * @return List
     */
    public List<GameMessage> getFirstFewBids(String gameId, int count) {
        List<GameMessage> list = new ArrayList<>(1);
        
        LinkedList<GameMessage> msgList = (LinkedList)msgMapping.get(gameId);
        if (msgList != null) {
            int min = Math.min(msgList.size(), count);
            list = new ArrayList<>(min);
            
            for (int i = 0; i < min; i ++) {
                list.add(msgList.get(i));
            }
        }
        return list;
    }
    
    /**
     * Return the last bid for this game.
     * @param gameId
     * @return BidMessage
     */
    public GameMessage getLatestBid(String gameId) {
        Deque<GameMessage> msgs = (Deque)msgMapping.get(gameId);
        if (msgs != null) {
            return msgs.getLast();
        }
        return null;
    }
    
    /**
     * Clear all the bid(s) that were raised from this game.
     * @param tableId
     */
    public void clearBids(String gameId) {
        msgMapping.remove(gameId);
    }
    
    /**
     * Clear all the bids.
     */
    public void clearAll() {
        msgMapping.clear();
    }
    
    /**
     * @return the bidCount
     */
    public int getBidCount() {
        return bidCount;
    }

    /**
     * Reset the total number of bids.
     */
    public void resetBidCount() {
        bidCount = 0;
    }
    
    public List<GameMessage> drain() {
        if (lock.get()) {
            // Some other thread is already draining the queue, so skip doing it again.
            return null;
        }
        try {
            lock.compareAndSet(false, true);
            List<GameMessage> list = new ArrayList<>(bidCount);
            
            String[] gameIds = msgMapping.keySet().toArray(new String[msgMapping.size()]);
            for (String gameId : gameIds) {
                Queue<GameMessage> bids = msgMapping.get(gameId);
                // Take all bids except the latest.
                for (int i = bids.size() - 1; i >= 1; i --) {
                    list.add(bids.remove());
                    bidCount --;
                }
            }
            return list;
        }
        finally {
            lock.set(false);
        }
    }
    
    public List<GameMessage> drain(String gameId) {
        Queue<GameMessage> msgs = msgMapping.remove(gameId);
        if (msgs != null) {
            return new ArrayList<>(msgs);
        }
        return null;
    }
}
