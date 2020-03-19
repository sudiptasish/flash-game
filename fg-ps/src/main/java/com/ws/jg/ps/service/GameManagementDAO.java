package com.ws.jg.ps.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.common.model.GameDeck;
import com.ws.jg.common.util.DatabaseUtil;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GameManagementDAO {

    private static final String SELECT_QUERY = "SELECT ID, DESCRIPTION, TABLE_ID, PLAYER_COUNT, START_TIME, STATUS, END_TIME FROM FLASH_GAMES WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO FLASH_GAMES (ID, DESCRIPTION, TABLE_ID, PLAYER_COUNT, START_TIME, STATUS, END_TIME) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE FLASH_GAMES SET PLAYER_COUNT = ?, STATUS = ?, END_TIME = ? WHERE ID = ?";
    private static final String DELETE_QUERY = "DELETE FROM FLASH_GAMES WHERE ID = ?";
    
    private static final String INSERT_DECK_QUERY = "INSERT INTO GAME_DECKS (GAME_ID, PLAYER_ID, SUIT, RANK) VALUES (?, ?, ?, ?)";
    private static final String SELECT_DECK_QUERY = "SELECT GAME_ID, PLAYER_ID, SUIT, RANK FROM GAME_DECKS WHERE GAME_ID = ?";
    private static final String DELETE_DECK_QUERY = "DELETE FROM GAME_DECKS WHERE GAME_ID = ?";
    
    public GameManagementDAO() {}
    
    /**
     * Add this new game to the underlying storage (DB).
     * @param player
     * @throws SQLException
     */
    public void insert(FlashGame game) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(INSERT_QUERY);
            pstmt.setString(1, game.getId());
            pstmt.setString(2, game.getDescription());
            pstmt.setString(3, game.getTableId());
            pstmt.setInt(4, game.getPlayerCount());
            pstmt.setDate(5, game.getStartTime());
            pstmt.setInt(6, game.getStatus());
            pstmt.setDate(7, game.getEndTime());
            
            int count = pstmt.executeUpdate();
            Logger.log(String.format("Inserted %d row(s) to [%s] table", count, "FLASH_GAMES"));
            
            DatabaseUtil.closeStatement(pstmt);
            
            // Now insert the card distribution.
            List<GameDeck> cards = game.getCards();
            pstmt = conn.prepareStatement(INSERT_DECK_QUERY);
            
            for (GameDeck card : cards) {
                pstmt.setString(1, card.getGameId());
                pstmt.setString(2, card.getPlayerId());
                pstmt.setString(3, card.getSuit().name());
                pstmt.setString(4, card.getRank().name());
                
                pstmt.addBatch();
            }
            int[] counts = pstmt.executeBatch();
            Logger.log(String.format("Inserted %d row(s) to [%s] table", counts.length, "GAME_DECKS"));
            
            conn.commit();            
        }
        catch (SQLException e) {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw e;
        }
        finally {
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Return the game details as identified by this id.
     * @param gameId
     * @return FlashGame
     */
    public FlashGame select(String gameId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        FlashGame game = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(SELECT_QUERY);
            pstmt.setString(1, gameId);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Logger.log(String.format("Selected %d row(s) from [%s] table", 1, "FLASH_GAME"));
                
                game = new FlashGame(rs.getString("ID")
                        , rs.getString("DESCRIPTION")
                        , rs.getString("TABLE_ID")
                        , rs.getInt("PLAYER_COUNT")
                        , rs.getDate("START_TIME")
                        , rs.getInt("STATUS")
                        , rs.getDate("END_TIME"));
            }
            
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(pstmt);
            
            if (game != null) {
                pstmt = conn.prepareStatement(SELECT_DECK_QUERY);
                pstmt.setString(1, gameId);
                rs = pstmt.executeQuery();
                
                List<GameDeck> cards = new ArrayList<>();
                while (rs.next()) {
                    cards.add(new GameDeck(rs.getString("GAME_ID")
                        , rs.getString("PLAYER_ID")
                        , Enum.valueOf(Card.SUIT.class, rs.getString("SUIT"))
                        , Enum.valueOf(Card.RANK.class, rs.getString("RANK"))));
                }
                game.setCards(cards);
            }
            return game;
        }
        finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Delete the game as identified by this game id.
     * @param gameId
     */
    public void delete(String gameId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(DELETE_QUERY);
            pstmt.setString(1, gameId);            
            int count = pstmt.executeUpdate();
            
            Logger.log(String.format("Deleted %d row(s) from [%s] table", count, "FLASH_GAME"));
            
            if (count == 0) {
                throw new SQLException("No game details found for id " + gameId);
            }
            DatabaseUtil.closeStatement(pstmt);
            
            // Now delete the corresponding deck entries.
            pstmt = conn.prepareStatement(DELETE_DECK_QUERY);
            pstmt.setString(1, gameId);            
            count = pstmt.executeUpdate();
            
            Logger.log(String.format("Deleted %d row(s) from [%s] table", count, "GAME_DECKS"));
            
            conn.commit();            
        }
        catch (SQLException e) {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw e;
        }
        finally {
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Update this flash game details in the DB.
     * @param gameId
     */
    public boolean update(FlashGame game) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(UPDATE_QUERY);
            pstmt.setInt(1, game.getPlayerCount());
            pstmt.setInt(2, game.getStatus());
            pstmt.setDate(3, game.getEndTime());
            pstmt.setString(4, game.getId());
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Updated %d row(s) from [%s] table", count, "FLASH_GAME"));
            
            if (count == 0) {
                return false;
            }
        }
        catch (SQLException e) {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw e;
        }
        finally {
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
        return true;
    }
}
