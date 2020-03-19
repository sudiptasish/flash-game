package com.ws.jg.player.service;

import java.sql.Date;
import java.sql.SQLException;

import com.ws.jg.common.exception.NoSuchPlayerException;
import com.ws.jg.common.exception.ResourceExistException;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.service.AbstractService;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.UUIDGenerator;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class PlayerManagementService extends AbstractService {
    
    private static final PlayerStore playerStore = PlayerStore.getPlayerStore();
    
    private final PlayerManagementDAO playerDao = new PlayerManagementDAO();

    /**
     * Register a player to the player store.
     * Registration is a one time process. Only once the registration
     * is complete, should the player be allowed to start a game.
     * 
     * @param player
     * @param transactional
     */
    public Player registerPlayer(Player player, boolean transactional) throws ResourceExistException {
        if (player.getId() == null || player.getId().trim().length() == 0) {
            player.setId(UUIDGenerator.generateUnique());
        }
        if (playerStore.contains(player.getId())) {
            throw new ResourceExistException(
                    String.format("Player %s already exists", player.getId()));
        }
        if (player.getCreateDate() == null) {
            player.setCreateDate(new Date(System.currentTimeMillis()));
        }
        // Add the player to the in-memory data store.
        playerStore.add(player);
        
        // If the transactional attribute is set, then add the player
        // to the underlying storage (DB).
        if (transactional) {
            try {
                playerDao.addPlayer(player);
            }
            catch (SQLException e) {
                playerStore.remove(player.getId());
                
                Logger.log(e.getMessage());
                if (e.getErrorCode() == 1) {
                    throw new ResourceExistException(
                            String.format("Player %s already exists", player.getId()));
                }
                else {
                    throw new RuntimeException(e);
                }
            }
        }
        return player;
    }
    
    /**
     * Update the player info in the player store.
     * @param player
     */
    public void updatePlayer(Player player, boolean transactional) {
        // This is a sql no-op operation.
        playerStore.update(player);
    }
    
    /**
     * Find the player as identified by this unique player id.
     * If no player is found, then this API will throw an error.
     * 
     * @param playerId
     * @param transactional
     * @return Player
     */
    public Player findPlayer(String playerId, boolean transactional) throws NoSuchPlayerException {
        Player player = playerStore.find(playerId);
        if (player == null) {
            // If the transactional attribute is set, then get the player
            // from the underlying storage (DB).
            if (transactional) {
                try {
                    player = playerDao.getPlayer(playerId);
                    if (player != null) {
                        playerStore.add(player);
                    }
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            // If the player object is still null ...
            if (player == null) {
                throw new NoSuchPlayerException(
                        String.format("No such player found for id [%s]", playerId));
            }
        }
        return player;
    }
    
    /**
     * Delete the player from the store as identified by this playerId.
     * @param playerId
     * @param transactional
     */
    public void removePlayer(String playerId, boolean transactional) {
        playerStore.remove(playerId);
        
        // If the transactional attribute is set, then remove the
        // player from the underlying storage (DB) as well.
        if (transactional) {
            try {
                playerDao.deletePlayer(playerId);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
