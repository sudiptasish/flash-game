package com.ws.jg.player.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ws.jg.common.model.Player;
import com.ws.jg.common.util.DatabaseUtil;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class PlayerManagementDAO {
    
    private static final String SELECT_QUERY = "SELECT ID, NAME, PASSWORD, EMAIL, CONTACT, ADDRESS, CREATE_DATE FROM PLAYERS WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO PLAYERS (ID, NAME, PASSWORD, EMAIL, CONTACT, ADDRESS, CREATE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM PLAYERS WHERE ID = ?";
    
    public PlayerManagementDAO() {}

    /**
     * Add this player to the underlying storage (DB).
     * @param player
     * @throws SQLException
     */
    public void addPlayer(Player player) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(INSERT_QUERY);
            pstmt.setString(1, player.getId());
            pstmt.setString(2, player.getName());
            pstmt.setString(3, player.getPassword());
            pstmt.setString(4, player.getEmail());
            pstmt.setString(5, player.getContact());
            pstmt.setString(6, player.getAddress());
            pstmt.setDate(7, player.getCreateDate());
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Inserted %d row(s) to [%s] table", count, "PLAYERS"));            
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
     * Return the player as identified by this id.
     * @param playerId
     * @return Player
     */
    public Player getPlayer(String playerId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(SELECT_QUERY);
            pstmt.setString(1, playerId);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Logger.log(String.format("Selected %d row(s) from [%s] table", 1, "PLAYERS"));
                
                Player player = new Player(rs.getString("ID")
                        , rs.getString("NAME")
                        , rs.getString("PASSWORD")
                        , rs.getString("EMAIL")
                        , rs.getString("CONTACT")
                        , rs.getString("ADDRESS")
                        , rs.getDate("CREATE_DATE"));
                
                return player;
            }
            return null;
        }
        finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Delete the player as identified by this player id.
     * @param playerId
     */
    public void deletePlayer(String playerId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(DELETE_QUERY);
            pstmt.setString(1, playerId);
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Deleted %d row(s) from [%s] table", count, "PLAYERS"));
            
            if (count == 0) {
                throw new SQLException("No player found for id " + playerId);
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
    }
}
