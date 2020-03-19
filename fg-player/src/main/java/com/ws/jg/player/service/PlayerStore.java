package com.ws.jg.player.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ws.jg.common.model.Player;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class PlayerStore {
    
    private static final PlayerStore _INSTANCE_ = new PlayerStore();

    private final ConcurrentMap<String, Player> playerMapping = new ConcurrentHashMap<>();
    
    private PlayerStore() {}
    
    /**
     * Return the singleton player store.
     * @return PlayerStore
     */
    public static PlayerStore getPlayerStore() {
        return _INSTANCE_;
    }
    
    /**
     * Add a new player to the player store.
     * @param player
     */
    public void add(Player player) {
        playerMapping.put(player.getId(), player);
    }
    
    /**
     * Check to see if a player with same id already exists.
     * @param playerId
     * @return boolean
     */
    public boolean contains(String playerId) {
        return playerMapping.containsKey(playerId);
    }
    
    /**
     * Update the player info.
     * @param player
     */
    public void update(Player player) {
        playerMapping.put(player.getId(), player);
    }
    
    /**
     * Remove a player from the player store.
     * @param playerId
     */
    public void remove(String playerId) {
        playerMapping.remove(playerId);
    }
    
    /**
     * Find and return the player as identified by this id, from the store.
     * 
     * @param playerId
     * @return Player
     */
    public Player find(String playerId) {
        return playerMapping.get(playerId);
    }
}
