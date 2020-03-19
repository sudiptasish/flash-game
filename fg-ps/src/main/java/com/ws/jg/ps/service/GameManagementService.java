package com.ws.jg.ps.service;

import java.sql.SQLException;

import com.ws.jg.common.exception.NoSuchGameException;
import com.ws.jg.common.exception.ResourceExistException;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.common.service.AbstractService;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GameManagementService extends AbstractService {

    private final GameManagementDAO gameMgmtDao = new GameManagementDAO();
    
    /**
     * Add this new flash game (that has just been started), to the underlying storage.
     * 
     * @param game
     * @param transactional
     * @throws ResourceExistException
     */
    public void addGame(FlashGame game, boolean transactional) throws ResourceExistException {
        try {
            gameMgmtDao.insert(game);
        }
        catch (SQLException e) {
            Logger.log(e.getMessage());
            if (e.getErrorCode() == 1) {
                throw new ResourceExistException(
                        String.format("Game %s already exists", game.getId()));
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * Update the game object.
     * @param game
     * @throws NoSuchGameException
     */
    public void updateGame(FlashGame game, boolean transactional) throws NoSuchGameException {
        try {
            if (!gameMgmtDao.update(game)) {
                throw new NoSuchGameException(
                        String.format("No such game identified by id [%s] found", game.getId()));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Fetch the game object as identified by this id.
     * @param gameId
     * @return FlashGame
     */
    public FlashGame getGame(String gameId, boolean transactional) throws NoSuchGameException {
        try {
            FlashGame game = gameMgmtDao.select(gameId);
            if (game != null) {
                return game;
            }
            throw new NoSuchGameException(
                    String.format("No such game identified by id [%s] found", gameId));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Remove the game as identified by this game id.
     * @param gameId
     */
    public void removeGame(String gameId, boolean transactional) {
        try {
            gameMgmtDao.delete(gameId);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
